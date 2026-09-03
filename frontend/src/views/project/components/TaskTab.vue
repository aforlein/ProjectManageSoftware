<template>
  <div>
    <el-button type="primary" size="small" @click="openDialog()" style="margin-bottom:12px">新增任务</el-button>
    <el-button v-if="selectedRows.length > 0" type="danger" size="small" @click="batchDelete" style="margin-left:8px">批量删除 ({{ selectedRows.length }})</el-button>
    <el-table :data="list" stripe @selection-change="handleSelectionChange">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column type="selection" width="45" />
      <el-table-column prop="taskName" label="任务名称" min-width="150" />
      <el-table-column label="任务类型" width="90"><template #default="{row}"><el-tag size="small">{{ typeLabel(row.taskType) }}</el-tag></template></el-table-column>
      <el-table-column label="模块" width="100"><template #default="{row}">{{ moduleName(row.moduleId) }}</template></el-table-column>
      <el-table-column prop="priority" label="优先级" width="80"><template #default="{row}"><el-tag :type="row.priority==='high'?'danger':row.priority==='medium'?'warning':'info'" size="small">{{ row.priority==='high'?'高':row.priority==='medium'?'中':'低' }}</el-tag></template></el-table-column>
      <el-table-column prop="status" label="状态" width="90"><template #default="{row}"><el-tag :type="row.status==='done'?'success':row.status==='in_progress'?'primary':'info'" size="small">{{ row.status==='done'?'已完成':row.status==='in_progress'?'进行中':'待开始' }}</el-tag></template></el-table-column>
      <el-table-column prop="endDate" label="截止日期" width="110" />
      <el-table-column prop="storyPoints" label="工时" width="60" />
      <el-table-column label="操作" width="150"><template #default="{row}"><el-button text type="primary" @click="openDialog(row)">编辑</el-button><el-button text type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="visible" :title="isEdit?'编辑':'新增'" width="550px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="fRef" label-width="80px">
        <el-form-item label="名称" prop="taskName"><el-input v-model="form.taskName" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="任务类型"><el-select v-model="form.taskType" style="width:100%"><el-option label="优化" value="optimization" /><el-option label="需求" value="requirement" /><el-option label="缺陷" value="defect" /><el-option label="依赖" value="dependency" /><el-option label="其他" value="other" /></el-select></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="模块分类">
              <el-select v-model="form.moduleId" style="width:100%" clearable placeholder="选择模块">
                <el-option v-for="m in modules" :key="m.id" :label="m.moduleName" :value="m.id" />
              </el-select>
              <el-button text type="primary" size="small" @click="showModuleDialog=true" style="margin-top:4px">管理分类</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16"><el-col :span="12"><el-form-item label="优先级"><el-select v-model="form.priority" style="width:100%"><el-option label="高" value="high" /><el-option label="中" value="medium" /><el-option label="低" value="low" /></el-select></el-form-item></el-col><el-col :span="12"><el-form-item label="状态"><el-select v-model="form.status" style="width:100%"><el-option label="待开始" value="todo" /><el-option label="进行中" value="in_progress" /><el-option label="已完成" value="done" /></el-select></el-form-item></el-col></el-row>
        <el-form-item label="截止日期"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="工时(小时)"><el-input-number v-model="form.storyPoints" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="负责人"><el-select v-model="form.assigneeId" style="width:100%" clearable><el-option v-for="u in userList" :key="u.id" :label="u.realName||u.username" :value="u.id" /></el-select></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template>
    </el-dialog>

    <!-- 模块分类管理弹窗 -->
    <el-dialog v-model="showModuleDialog" title="管理模块分类" width="420px">
      <el-table :data="modules" stripe max-height="300">
        <el-table-column prop="moduleName" label="模块名称" />
        <el-table-column label="操作" width="120">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="editModule(row)">编辑</el-button>
            <el-button text type="danger" size="small" @click="removeModule(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px;display:flex;gap:8px">
        <el-input v-model="newModuleName" placeholder="输入新模块名称" size="small" />
        <el-button type="primary" size="small" @click="addModule">添加</el-button>
      </div>
      <template #footer><el-button @click="showModuleDialog=false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'
import { getTasks, createTask, updateTask, deleteTask } from '@/api/task'
import { getTaskModules, createTaskModule, updateTaskModule, deleteTaskModule } from '@/api/taskModule'
import { getUserPage } from '@/api/user'
const props=defineProps({projectId:Number});const list=ref([]);const modules=ref([]);const userList=ref([])
const visible=ref(false);const isEdit=ref(false);const editId=ref(null);const fRef=ref(null)
const form=reactive({taskName:'',taskType:'requirement',moduleId:null,assigneeId:null,priority:'medium',status:'todo',endDate:'',storyPoints:0,description:''})
const rules={taskName:[{required:true,message:'请输入名称'}]}
const selectedRows=ref([])
const handleSelectionChange=(rows)=>{selectedRows.value=rows}
const batchDelete=async()=>{try{await ElMessageBox.confirm('确定删除选中的记录?','批量删除',{type:'warning'});await Promise.all(selectedRows.value.map(r=>deleteTask(props.projectId,r.id)));ElMessage.success('批量删除成功');selectedRows.value=[];fetch()}catch{}}
const typeLabel=(v)=>({optimization:'优化',requirement:'需求',defect:'缺陷',dependency:'依赖',other:'其他'})[v]||v
const moduleName=(id)=>{const m=modules.value.find(x=>x.id===id);return m?m.moduleName:''}

const fetch=async()=>{list.value=await getTasks(props.projectId)}
const fetchModules=async()=>{modules.value=await getTaskModules(props.projectId)}
const openDialog=(row)=>{isEdit.value=!!row;if(row){editId.value=row.id;Object.assign(form,row)};visible.value=true}
const resetForm=()=>{editId.value=null;Object.assign(form,{taskName:'',taskType:'requirement',moduleId:null,assigneeId:null,priority:'medium',status:'todo',endDate:'',storyPoints:0,description:''});fRef.value?.resetFields()}
const submit=async()=>{if(!await fRef.value.validate().catch(()=>false))return;isEdit.value?await updateTask(props.projectId,editId.value,form):await createTask(props.projectId,form);ElMessage.success(isEdit.value?'编辑成功':'新增成功');visible.value=false;fetch()}
const handleDelete=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteTask(props.projectId,row.id);ElMessage.success('删除成功');fetch()}catch{}}

// 模块分类管理
const showModuleDialog=ref(false);const newModuleName=ref('');const editingModuleId=ref(null)
const addModule=async()=>{if(!newModuleName.value.trim())return;if(editingModuleId.value){await updateTaskModule(props.projectId,editingModuleId.value,{moduleName:newModuleName.value.trim()});editingModuleId.value=null}else{await createTaskModule(props.projectId,{moduleName:newModuleName.value.trim()})};newModuleName.value='';fetchModules()}
const editModule=(row)=>{newModuleName.value=row.moduleName;editingModuleId.value=row.id}
const removeModule=async(row)=>{try{await ElMessageBox.confirm('确定删除?','提示',{type:'warning'});await deleteTaskModule(props.projectId,row.id);ElMessage.success('删除成功');fetchModules()}catch(e){ElMessage.error(e.message||'删除失败')}}

onMounted(()=>{fetch();fetchModules();getUserPage({page:1,size:1000}).then(d=>{userList.value=d.records||[]}).catch(()=>{})})
</script>
