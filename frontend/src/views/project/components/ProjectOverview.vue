<template>
  <el-descriptions :column="2" border v-if="project">
    <el-descriptions-item label="项目名称">{{ project.projectName }}</el-descriptions-item>
    <el-descriptions-item label="客户名称">{{ project.customerName }}</el-descriptions-item>
    <el-descriptions-item label="项目地点">{{ project.location || '-' }}</el-descriptions-item>
    <el-descriptions-item label="项目状态">
      <el-tag :type="statusType">{{ statusLabel }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="项目经理">{{ project.projectManagerName }}</el-descriptions-item>
    <el-descriptions-item label="合同金额">{{ project.contractAmount?.toLocaleString() }} 元</el-descriptions-item>
    <el-descriptions-item label="预估成本">{{ project.estimatedCost?.toLocaleString() }} 元</el-descriptions-item>
    <el-descriptions-item label="开始日期">{{ project.startDate }}</el-descriptions-item>
    <el-descriptions-item label="结束日期">{{ project.endDate }}</el-descriptions-item>
    <el-descriptions-item label="创建时间">{{ project.createTime }}</el-descriptions-item>
    <el-descriptions-item label="坐标">
      {{ project.longitude }}, {{ project.latitude }}
    </el-descriptions-item>
    <el-descriptions-item label="项目范围" :span="2">{{ project.projectScope || '-' }}</el-descriptions-item>
    <el-descriptions-item label="项目目标" :span="2">{{ project.projectTarget || '-' }}</el-descriptions-item>
  </el-descriptions>
  <el-empty v-else description="项目信息加载中..." />
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({ project: Object })

const statusMap = { planning: ['规划中', 'info'], in_progress: ['进行中', 'primary'], completed: ['已完成', 'success'], closed: ['已关闭', ''], on_hold: ['已暂停', 'warning'] }
const statusLabel = computed(() => statusMap[props.project?.status]?.[0] || props.project?.status || '-')
const statusType = computed(() => statusMap[props.project?.status]?.[1] || 'info')
</script>
