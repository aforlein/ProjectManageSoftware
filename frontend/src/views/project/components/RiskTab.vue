<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增风险</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="riskTitle" label="标题" min-width="180" />
      <el-table-column prop="riskLevel" label="等级" width="80"><template #default="{row}"><el-tag :type="row.riskLevel==='high'?'danger':row.riskLevel==='medium'?'warning':'info'" size="small">{{row.riskLevel==='high'?'高':row.riskLevel==='medium'?'中':'低'}}</el-tag></template></el-table-column>
      <el-table-column prop="riskType" label="类型" width="100" /><el-table-column prop="status" label="状态" width="90" />
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="550px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="标题" prop="riskTitle"><el-input v-model="form.riskTitle" /></el-form-item>
        <el-row :gutter="16"><el-col :span="8"><el-form-item label="等级"><el-select v-model="form.riskLevel" style="width:100%"><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="类型"><el-select v-model="form.riskType" style="width:100%"><el-option label="技术" value="tech" /><el-option label="资源" value="resource" /><el-option label="进度" value="schedule" /></el-select></el-form-item></el-col><el-col :span="8"><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="已识别" value="identified" /><el-option label="已评估" value="assessed" /><el-option label="缓解中" value="mitigated" /><el-option label="关闭" value="closed" /></el-select></el-form-item></el-col></el-row>
        <el-form-item label="描述"><el-input v-model="form.riskDescription" type="textarea" /></el-form-item>
        <el-form-item label="应对策略"><el-input v-model="form.responseStrategy" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
const props=defineProps({projectId:Number});const base=`/project/${props.projectId}/risk`
const list=ref([]);const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({riskTitle:'',riskLevel:'medium',riskType:'tech',status:'identified',riskDescription:'',responseStrategy:'',contingencyPlan:''})
const rules={riskTitle:[{required:true,message:'请输入标题'}]}
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>request.delete(`${base}/${r.id}`)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const fetch=async()=>{list.value=await request.get(base)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{riskTitle:'',riskLevel:'medium',riskType:'tech',status:'identified',riskDescription:'',responseStrategy:'',contingencyPlan:''});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await request.put(`${base}/${editId.value}`,form):await request.post(base,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await request.delete(`${base}/${row.id}`);ElMessage.success('删除成功');fetch()}catch{}}
onMounted(fetch)
</script>
