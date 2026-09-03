<template>
  <div>
    <el-breadcrumb style="margin-bottom: 16px">
      <el-breadcrumb-item :to="{ path: '/project' }">项目列表</el-breadcrumb-item>
      <el-breadcrumb-item>{{ project?.projectName || '项目详情' }}</el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="项目概况" name="overview">
        <ProjectOverview :project="project" />
      </el-tab-pane>
      <el-tab-pane label="里程碑计划" name="milestone">
        <MilestoneTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="详细计划" name="plan">
        <PlanTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="迭代任务" name="task">
        <TaskTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="问题日志" name="issue">
        <IssueTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="风险登记册" name="risk">
        <RiskTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="知识库" name="knowledge">
        <KnowledgeTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="沟通记录" name="communication">
        <CommunicationTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="周报" name="weekReport">
        <WeekReportTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="部署记录" name="deployRecord">
        <DeployRecordTab :project-id="projectId" />
      </el-tab-pane>
      <el-tab-pane label="关系人" name="stakeholder">
        <StakeholderTab :project-id="projectId" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProject } from '@/api/project'
import ProjectOverview from './components/ProjectOverview.vue'
import MilestoneTab from './components/MilestoneTab.vue'
import PlanTab from './components/PlanTab.vue'
import TaskTab from './components/TaskTab.vue'
import IssueTab from './components/IssueTab.vue'
import RiskTab from './components/RiskTab.vue'
import KnowledgeTab from './components/KnowledgeTab.vue'
import CommunicationTab from './components/CommunicationTab.vue'
import WeekReportTab from './components/WeekReportTab.vue'
import DeployRecordTab from './components/DeployRecordTab.vue'
import StakeholderTab from './components/StakeholderTab.vue'

const route = useRoute()
const projectId = computed(() => Number(route.params.id))
const project = ref(null)
const activeTab = ref('overview')

onMounted(async () => {
  try {
    project.value = await getProject(projectId.value)
  } catch {}
})
</script>
