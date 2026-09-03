<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增周报</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="weekStart" label="开始日" width="110" /><el-table-column prop="weekEnd" label="结束日" width="110" />
      <el-table-column prop="content" label="本周工作" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="600px" @close="resetForm">
      <el-form :model="form" ref="fRef" label-width="80px">
        <el-row :gutter="16"><el-col :span="12"><el-form-item label="开始日"><el-date-picker v-model="form.weekStart" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="结束日"><el-date-picker v-model="form.weekEnd" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col></el-row>
        <el-form-item label="本周工作"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="下周计划"><el-input v-model="form.planNext" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="问题风险"><el-input v-model="form.issues" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getWeekReports, createWeekReport, updateWeekReport, deleteWeekReport } from '@/api/weekReport'
const props=defineProps({projectId:Number});const list=ref([]);const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({weekStart:'',weekEnd:'',content:'',planNext:'',issues:''})
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deleteWeekReport(props.projectId,r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await getWeekReports(props.projectId)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{weekStart:'',weekEnd:'',content:'',planNext:'',issues:''});fRef.value?.resetFields()}
const submit=async()=>{isEdit.value?await updateWeekReport(props.projectId,editId.value,form):await createWeekReport(props.projectId,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteWeekReport(props.projectId,row.id);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(fetch)
</script>
