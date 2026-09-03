<template>
  <el-container class="layout-container">
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '220px'">
      <Sidebar />
    </el-aside>
    <el-container>
      <el-header>
        <Navbar />
      </el-header>
      <el-main>
        <TabBar />
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useAppStore } from '@/stores/app'
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
import TabBar from '@/components/TabBar.vue'

const appStore = useAppStore()
</script>
