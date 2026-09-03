<template>
  <div>
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="query">
        <el-form-item label="关键字">
          <el-input v-model="query.keyword" placeholder="项目名称" clearable @clear="search" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 130px">
            <el-option label="规划中" value="planning" />
            <el-option label="进行中" value="in_progress" />
            <el-option label="已完成" value="completed" />
            <el-option label="已关闭" value="closed" />
            <el-option label="已暂停" value="on_hold" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <div class="table-toolbar">
      <el-button type="primary" @click="showForm = true; editingProject = null">
        <el-icon><Plus /></el-icon>新增项目
      </el-button>
      <el-button v-if="selectedRows.length > 0" type="danger" @click="batchDelete">
        <el-icon><Delete /></el-icon>批量删除 ({{ selectedRows.length }})
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-card>
      <el-table :data="tableData" stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column type="selection" width="45" />
        <el-table-column prop="projectName" label="项目名称" min-width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="tabsStore.openTab(row.id, row.projectName, `/project/detail/${row.id}`); $router.push(`/project/detail/${row.id}`)">
              {{ row.projectName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="projectManagerName" label="项目经理" width="100" />
        <el-table-column prop="contractAmount" label="合同金额" width="120">
          <template #default="{ row }">{{ row.contractAmount?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" @click="showForm = true; editingProject = row">编辑</el-button>
            <el-button text type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 16px; display: flex; justify-content: flex-end">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="search"
          @current-change="search"
        />
      </div>
    </el-card>

    <!-- 项目表单弹窗 -->
    <ProjectForm
      v-if="showForm"
      :data="editingProject"
      :user-list="userList"
      @close="showForm = false"
      @success="fetchData(); showForm = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTabsStore } from '@/stores/tabs'
const tabsStore = useTabsStore()
import { getProjectPage, deleteProject } from '@/api/project'
import { getUserPage } from '@/api/user'
import ProjectForm from '@/components/ProjectForm.vue'

const selectedRows = ref([])
const handleSelectionChange = (rows) => { selectedRows.value = rows }
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个项目?`, '批量删除', { type: 'warning' })
    await Promise.all(selectedRows.value.map(r => deleteProject(r.id)))
    ElMessage.success('批量删除成功')
    selectedRows.value = []
    fetchData()
  } catch {}
}

const query = reactive({ page: 1, size: 10, keyword: '', status: '' })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const userList = ref([])

const showForm = ref(false)
const editingProject = ref(null)

const statusType = (status) => {
  const map = { planning: 'info', in_progress: 'primary', completed: 'success', closed: '', on_hold: 'warning' }
  return map[status] || 'info'
}

const statusLabel = (status) => {
  const map = { planning: '规划中', in_progress: '进行中', completed: '已完成', closed: '已关闭', on_hold: '已暂停' }
  return map[status] || status
}

const fetchData = async () => {
  loading.value = true
  try {
    const data = await getProjectPage(query)
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const fetchUsers = async () => {
  try {
    const data = await getUserPage({ page: 1, size: 1000 })
    userList.value = data.records || []
  } catch {}
}

const search = () => {
  query.page = 1
  fetchData()
}

const reset = () => {
  query.keyword = ''
  query.status = ''
  search()
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目 "${row.projectName}" 吗？`, '确认删除', { type: 'warning' })
    await deleteProject(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch {}
}

onMounted(() => {
  fetchData()
  fetchUsers()
})
</script>
