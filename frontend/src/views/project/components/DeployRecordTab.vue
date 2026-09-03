<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增部署记录</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="deployVersion" label="版本" width="120" /><el-table-column prop="deployEnv" label="环境" width="100" />
      <el-table-column prop="deployDate" label="部署时间" width="170" /><el-table-column prop="status" label="状态" width="90"><template #default="{row}"><el-tag :type="row.status==='success'?'success':row.status==='failed'?'danger':'info'" size="small">{{row.status}}</el-tag></template></el-table-column>
      <el-table-column prop="deployNotes" label="说明" min-width="150" />
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="500px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="版本" prop="deployVersion"><el-input v-model="form.deployVersion" /></el-form-item>
        <el-form-item label="环境"><el-select v-model="form.deployEnv" style="width:100%"><el-option label="开发" value="dev" /><el-option label="测试" value="test" /><el-option label="预发" value="staging" /><el-option label="生产" value="prod" /></el-select></el-form-item>
        <el-form-item label="部署时间"><el-date-picker v-model="form.deployDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="待部署" value="pending" /><el-option label="成功" value="success" /><el-option label="失败" value="failed" /></el-select></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.deployNotes" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeployRecords, createDeployRecord, updateDeployRecord, deleteDeployRecord } from '@/api/deployRecord'
const props=defineProps({projectId:Number});const list=ref([]);const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({deployVersion:'',deployEnv:'dev',deployDate:'',status:'pending',deployNotes:''});const rules={deployVersion:[{required:true}]}
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deleteDeployRecord(props.projectId,r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await getDeployRecords(props.projectId)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{deployVersion:'',deployEnv:'dev',deployDate:'',status:'pending',deployNotes:''});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await updateDeployRecord(props.projectId,editId.value,form):await createDeployRecord(props.projectId,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteDeployRecord(props.projectId,row.id);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(fetch)
</script>
