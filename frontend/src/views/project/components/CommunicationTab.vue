<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增沟通记录</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="communicationDate" label="日期" width="110" /><el-table-column prop="type" label="方式" width="90" />
      <el-table-column prop="participant" label="参与人" width="150" /><el-table-column prop="content" label="内容" min-width="200" />
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="500px" @close="resetForm">
      <el-form :model="form" ref="fRef" label-width="80px">
        <el-form-item label="日期"><el-date-picker v-model="form.communicationDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="方式"><el-select v-model="form.type" style="width:100%"><el-option label="会议" value="meeting" /><el-option label="电话" value="phone" /><el-option label="邮件" value="email" /><el-option label="微信" value="wechat" /></el-select></el-form-item>
        <el-form-item label="参与人"><el-input v-model="form.participant" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.notes" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getCommunications, createCommunication, updateCommunication, deleteCommunication } from '@/api/communication'
const props=defineProps({projectId:Number});const list=ref([]);const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({communicationDate:'',type:'meeting',participant:'',content:'',notes:''})
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deleteCommunication(props.projectId,r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await getCommunications(props.projectId)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{communicationDate:'',type:'meeting',participant:'',content:'',notes:''});fRef.value?.resetFields()}
const submit=async()=>{isEdit.value?await updateCommunication(props.projectId,editId.value,form):await createCommunication(props.projectId,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteCommunication(props.projectId,row.id);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(fetch)
</script>
