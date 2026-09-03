<template>
  <el-dialog v-model="visible" :title="isEdit ? '编辑项目' : '新增项目'" width="650px" @close="$emit('close')">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入项目名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="form.customerName" placeholder="请输入客户名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="项目地点">
        <el-input v-model="form.location" placeholder="请输入项目地点地址" />
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
              <el-option label="规划中" value="planning" />
              <el-option label="进行中" value="in_progress" />
              <el-option label="已完成" value="completed" />
              <el-option label="已关闭" value="closed" />
              <el-option label="已暂停" value="on_hold" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目经理" prop="projectManagerId">
            <el-select v-model="form.projectManagerId" placeholder="请选择" style="width: 100%">
              <el-option v-for="u in userList" :key="u.id" :label="u.realName || u.username" :value="u.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合同金额">
            <el-input-number v-model="form.contractAmount" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预估成本">
            <el-input-number v-model="form.estimatedCost" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期">
            <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期">
            <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="11">
          <el-form-item label="经度">
            <el-input-number v-model="form.longitude" :precision="6" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="纬度">
            <el-input-number v-model="form.latitude" :precision="6" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="2" style="display:flex;align-items:center;padding-top:4px">
          <el-button @click="showMapPicker = true" :icon="MapLocation" size="small" title="在地图上选点" />
        </el-col>
      </el-row>
      <el-form-item label="项目范围">
        <el-input v-model="form.projectScope" type="textarea" :rows="2" placeholder="请输入项目范围" />
      </el-form-item>
      <el-form-item label="项目目标">
        <el-input v-model="form.projectTarget" type="textarea" :rows="2" placeholder="请输入项目目标" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>

  <MapPicker v-if="showMapPicker" @confirm="onMapPick" @close="showMapPicker = false" />
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { MapLocation } from '@element-plus/icons-vue'
import { createProject, updateProject } from '@/api/project'
import MapPicker from '@/components/MapPicker.vue'

const props = defineProps({
  data: Object,       // null for create, project object for edit
  userList: Array     // list of users for manager selector
})

const emit = defineEmits(['close', 'success'])

const visible = ref(true)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const showMapPicker = ref(false)

function onMapPick({ longitude, latitude, location }) {
  form.longitude = longitude
  form.latitude = latitude
  if (location) form.location = location
  showMapPicker.value = false
}

const form = reactive({
  projectName: '',
  customerName: '',
  location: '',
  status: 'planning',
  projectManagerId: null,
  contractAmount: 0,
  estimatedCost: 0,
  startDate: '',
  endDate: '',
  longitude: null,
  latitude: null,
  projectScope: '',
  projectTarget: ''
})

const rules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  projectManagerId: [{ required: true, message: '请选择项目经理', trigger: 'change' }]
}

watch(() => props.data, (val) => {
  if (val) {
    isEdit.value = true
    Object.assign(form, val)
  }
}, { immediate: true })

watch(visible, (val) => {
  if (!val) emit('close')
})

const submitForm = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateProject(props.data.id, form)
      ElMessage.success('编辑成功')
    } else {
      await createProject(form)
      ElMessage.success('新增成功')
    }
    emit('success')
    visible.value = false
  } finally {
    submitting.value = false
  }
}
</script>
