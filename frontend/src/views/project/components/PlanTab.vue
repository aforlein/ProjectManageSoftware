<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增计划</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="planName" label="计划名称" min-width="150" />
      <el-table-column prop="startDate" label="开始" width="110" /><el-table-column prop="endDate" label="结束" width="110" />
      <el-table-column label="进度" width="100"><template #default="{row}"><el-progress :percentage="row.progress||0" /></template></el-table-column>
      <el-table-column prop="status" label="状态" width="100"><template #default="{row}"><el-tag :type="row.status==='completed'?'success':row.status==='in_progress'?'primary':'info'" size="small">{{ row.status==='completed'?'已完成':row.status==='in_progress'?'进行中':'待开始' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="500px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="名称" prop="planName"><el-input v-model="form.planName" /></el-form-item>
        <el-row :gutter="16"><el-col :span="12"><el-form-item label="开始"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="结束"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col></el-row>
        <el-form-item label="进度"><el-slider v-model="form.progress" :max="100" show-input /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="待开始" value="pending" /><el-option label="进行中" value="in_progress" /><el-option label="已完成" value="completed" /></el-select></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getPlans, createPlan, updatePlan, deletePlan } from '@/api/plan'
const props = defineProps({ projectId: Number }); const list=ref([]); const visible=ref(false); const isEdit=ref(false); const editId=ref(null); const fRef=ref(null)
const form=reactive({planName:'',startDate:'',endDate:'',status:'pending',progress:0,description:''})
const rules={planName:[{required:true,message:'请输入名称'}]}
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deletePlan(props.projectId,r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await getPlans(props.projectId)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{planName:'',startDate:'',endDate:'',status:'pending',progress:0,description:''});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await updatePlan(props.projectId,editId.value,form):await createPlan(props.projectId,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deletePlan(props.projectId,row.id);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(fetch)
</script>
