<template>
  <div style="display: flex; align-items: center; justify-content: space-between; width: 100%">
    <div style="display: flex; align-items: center">
      <el-icon :size="22" style="cursor: pointer" @click="appStore.toggleSidebar()">
        <Fold v-if="!appStore.sidebarCollapsed" />
        <Expand v-else />
      </el-icon>
      <span style="margin-left: 12px; font-size: 16px">{{ route.meta?.title || '' }}</span>
    </div>

    <div style="display: flex; align-items: center">
      <el-dropdown trigger="click">
        <div style="display: flex; align-items: center; cursor: pointer">
          <el-avatar :size="32" style="margin-right: 8px">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <span>{{ userStore.realName || userStore.username }}</span>
          <el-icon style="margin-left: 4px"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item disabled>
              <el-icon><User /></el-icon>
              {{ userStore.username }}
            </el-dropdown-item>
            <el-dropdown-item disabled>
              <el-icon><Key /></el-icon>
              角色: {{ userStore.roleCode }}
            </el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
  }).catch(() => {})
}
</script>
