<template>
  <div>
    <div class="logo">
      <el-icon :size="20"><Monitor /></el-icon>
      <span v-show="!appStore.sidebarCollapsed" style="margin-left: 8px">项目管理系统</span>
    </div>
    <el-menu
      :default-active="activeMenu"
      :collapse="appStore.sidebarCollapsed"
      :collapse-transition="false"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      router
    >
      <el-menu-item index="/dashboard">
        <el-icon><DataAnalysis /></el-icon>
        <template #title>项目看板</template>
      </el-menu-item>
      <el-menu-item index="/project">
        <el-icon><FolderOpened /></el-icon>
        <template #title>项目列表</template>
      </el-menu-item>
      <el-menu-item index="/map">
        <el-icon><MapLocation /></el-icon>
        <template #title>项目分布</template>
      </el-menu-item>

      <el-sub-menu index="quick-entry">
        <template #title>
          <el-icon><Grid /></el-icon>
          <span>快捷入口</span>
        </template>
        <el-menu-item index="/risk">风险管理</el-menu-item>
        <el-menu-item index="/issue">问题日志</el-menu-item>
      </el-sub-menu>

      <template v-if="userStore.isAdmin()">
        <el-divider style="margin: 8px 0; border-color: rgba(255,255,255,0.1)" />

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
</script>
