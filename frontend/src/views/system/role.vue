<template>
  <div>
    <div class="table-toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon>新增角色
      </el-button>
    </div>

    <el-card>
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="180" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button text type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="450px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" :disabled="isEdit" placeholder="如: ADMIN" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入角色描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRolePage, createRole, updateRole, deleteRole } from '@/api/role'

const tableData = ref([])
const loading = ref(false)

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, roleName: '', roleCode: '', description: '' })

const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    tableData.value = await getRolePage()
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  isEdit.value = !!row
  if (row) {
    form.id = row.id
    form.roleName = row.roleName
    form.roleCode = row.roleCode
    form.description = row.description
  }
  dialogVisible.value = true
}

const resetForm = () => {
  form.id = null
  form.roleName = ''
  form.roleCode = ''
  form.description = ''
  formRef.value?.resetFields()
}

const submitForm = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateRole(form.id, { roleName: form.roleName, roleCode: form.roleCode, description: form.description })
      ElMessage.success('编辑成功')
    } else {
      await createRole({ roleName: form.roleName, roleCode: form.roleCode, description: form.description })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除角色 "${row.roleName}" 吗？`, '确认删除', { type: 'warning' })
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch {}
}

onMounted(() => {
  fetchData()
})
</script>
