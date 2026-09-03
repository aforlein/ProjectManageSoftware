<template>
  <div>
    <el-card class="search-card">
      <el-form :inline="true" :model="query">
        <el-form-item label="关键字"><el-input v-model="query.keyword" placeholder="问题标题" clearable /></el-form-item>
        <el-form-item label="优先级"><el-select v-model="query.priority" placeholder="全部" clearable style="width:110px"><el-option label="紧急" value="urgent" /><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="query.status" placeholder="全部" clearable style="width:130px"><el-option label="未开始" value="open" /><el-option label="处理中" value="in_progress" /><el-option label="已解决" value="resolved" /><el-option label="已关闭" value="closed" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="search">查询</el-button><el-button @click="reset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <div class="table-toolbar"><el-button type="primary" @click="openDialog()">新增问题</el-button><el-button v-if="selectedRows.length > 0" type="danger" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button></div>
    <el-card>
      <el-table :data="tableData" stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column type="selection" width="45" />
        <el-table-column prop="issueTitle" label="标题" min-width="180" />
        <el-table-column prop="issueType" label="类型" width="90" />
        <el-table-column label="优先级" width="80"><template #default="{row}"><el-tag :type="row.priority==='urgent'?'danger':row.priority==='high'?'warning':'info'" size="small">{{row.priority}}</el-tag></template></el-table-column>
        <el-table-column prop="status" label="状态" width="90" />
        <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
      </el-table>
      <div style="margin-top:16px;display:flex;justify-content:flex-end"><el-pagination v-model:current-page="query.page" v-model:page-size="query.size" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @size-change="search" @current-change="search" /></div>
    </el-card>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="550px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="标题" prop="issueTitle"><el-input v-model="form.issueTitle" /></el-form-item>
        <el-row :gutter="16"><el-col :span="8"><el-form-item label="类型"><el-select v-model="form.issueType" style="width:100%"><el-option label="范围问题" value="scope" /><el-option label="进度问题" value="schedule" /><el-option label="资源问题" value="resource" /><el-option label="沟通问题" value="communication" /><el-option label="其他问题" value="other" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="优先级"><el-select v-model="form.priority" style="width:100%"><el-option label="紧急" value="urgent" /><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="未开始" value="open" /><el-option label="处理中" value="in_progress" /><el-option label="已解决" value="resolved" /><el-option label="关闭" value="closed" /></el-select></el-form-item></el-col></el-row>
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
import { getIssuePage, createIssue, updateIssue, deleteIssue } from '@/api/issue'
import { getUserPage } from '@/api/user'
const query=reactive({page:1,size:10,keyword:'',priority:'',status:''});const tableData=ref([]);const total=ref(0);const loading=ref(false);const userList=ref([])
const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({issueTitle:'',issueType:'scope',priority:'medium',status:'open',issueDescription:'',rootCause:'',solution:'',ownerId:null});const rules={issueTitle:[{required:true}]}
const fetchData=async()=>{loading.value=true;try{const d=await getIssuePage(query);tableData.value=d.records;total.value=d.total}finally{loading.value=false}}
const search=()=>{query.page=1;fetchData()};const reset=()=>{query.keyword='';query.priority='';query.status='';search()}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{issueTitle:'',issueType:'scope',priority:'medium',status:'open',issueDescription:'',rootCause:'',solution:'',ownerId:null});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await updateIssue(editId.value,form):await createIssue(form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetchData()}
const selectedRows=ref([]);const handleSelectionChange=(rows)=>{selectedRows.value=rows};const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deleteIssue(r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetchData()}catch{}}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteIssue(row.id);ElMessage.success('删除成功');fetchData()}catch{}}
onMounted(()=>{fetchData();getUserPage({page:1,size:1000}).then(d=>{userList.value=d.records||[]}).catch(()=>{})})
</script>
