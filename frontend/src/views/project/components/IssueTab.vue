<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增问题</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="issueTitle" label="标题" min-width="180" />
      <el-table-column prop="issueType" label="类型" width="100" /><el-table-column prop="priority" label="优先级" width="90"><template #default="{row}"><el-tag :type="row.priority==='urgent'?'danger':row.priority==='high'?'warning':'info'" size="small">{{row.priority}}</el-tag></template></el-table-column>
      <el-table-column prop="status" label="状态" width="90" /><el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="550px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="标题" prop="issueTitle"><el-input v-model="form.issueTitle" /></el-form-item>
        <el-row :gutter="16"><el-col :span="8"><el-form-item label="类型"><el-select v-model="form.issueType" style="width:100%"><el-option label="范围问题" value="scope" /><el-option label="进度问题" value="schedule" /><el-option label="资源问题" value="resource" /><el-option label="沟通问题" value="communication" /><el-option label="其他问题" value="other" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="优先级"><el-select v-model="form.priority" style="width:100%"><el-option label="紧急" value="urgent" /><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="未开始" value="open" /><el-option label="处理中" value="in_progress" /><el-option label="已解决" value="resolved" /><el-option label="已关闭" value="closed" /></el-select></el-form-item></el-col></el-row>
        <el-form-item label="负责人"><el-select v-model="form.ownerId" style="width:100%" clearable><el-option v-for="u in userList" :key="u.id" :label="u.realName||u.username" :value="u.id" /></el-select></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.issueDescription" type="textarea" /></el-form-item>
        <el-form-item label="根因"><el-input v-model="form.rootCause" type="textarea" /></el-form-item>
        <el-form-item label="解决方案"><el-input v-model="form.solution" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { getUserPage } from '@/api/user'
const props=defineProps({projectId:Number});const base=`/project/${props.projectId}/issue`
const list=ref([]);const userList=ref([]);const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({issueTitle:'',issueType:'scope',priority:'medium',status:'open',issueDescription:'',rootCause:'',solution:'',ownerId:null})
const rules={issueTitle:[{required:true,message:'请输入标题'}]}
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>request.delete(`${base}/${r.id}`)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await request.get(base)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{issueTitle:'',issueType:'scope',priority:'medium',status:'open',issueDescription:'',rootCause:'',solution:'',ownerId:null});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await request.put(`${base}/${editId.value}`,form):await request.post(base,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await request.delete(`${base}/${row.id}`);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(()=>{fetch();getUserPage({page:1,size:1000}).then(d=>{userList.value=d.records||[]}).catch(()=>{})})
</script>
