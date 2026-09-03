<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value">{{ stats.totalProjects }}</div>
          <div class="stat-label">项目总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value" style="color:#409EFF">{{ stats.inProgressProjects }}</div>
          <div class="stat-label">进行中项目</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value" style="color:#67C23A">{{ stats.completedProjects }}</div>
          <div class="stat-label">已完成项目</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value" style="color:#F56C6C">{{ stats.highRiskCount }}</div>
          <div class="stat-label">高风险项目</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 优先任务 + 风险预警 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card>
          <template #header><span style="font-weight:bold">优先任务</span></template>
          <el-table :data="stats.priorityTasks || []" stripe>
            <el-table-column prop="taskName" label="任务" min-width="150" />
            <el-table-column prop="projectName" label="所属项目" min-width="120" />
            <el-table-column prop="endDate" label="截止日期" width="110" />
            <el-table-column prop="assigneeName" label="负责人" width="80" />
          </el-table>
          <el-empty v-if="!stats.priorityTasks?.length" description="暂无优先任务" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span style="font-weight:bold; color:#F56C6C">风险预警</span></template>
          <el-table :data="stats.highRisks || []" stripe>
            <el-table-column prop="riskTitle" label="风险" min-width="150" />
            <el-table-column prop="projectName" label="所属项目" min-width="120" />
            <el-table-column label="等级" width="80">
              <template #default><el-tag type="danger" size="small">高</el-tag></template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!stats.highRisks?.length" description="暂无高风险项" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStats } from '@/api/dashboard'

const stats = ref({ totalProjects: 0, inProgressProjects: 0, completedProjects: 0, highRiskCount: 0, priorityTasks: [], highRisks: [] })

onMounted(async () => {
  try {
    stats.value = await getDashboardStats()
  } catch {}
})
</script>
