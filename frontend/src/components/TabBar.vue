<template>
  <div v-if="tabsStore.hasTabs" class="tab-bar">
    <div
      v-for="tab in tabsStore.tabs"
      :key="tab.id"
      :class="['tab-item', { active: tabsStore.activeTab === tab.id }]"
      @click="$router.push(tab.path)"
    >
      <span class="tab-title">{{ tab.title }}</span>
      <el-icon class="tab-close" @click.stop="handleClose(tab.id)"><Close /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { useTabsStore } from '@/stores/tabs'
import { useRouter } from 'vue-router'
import { Close } from '@element-plus/icons-vue'

const tabsStore = useTabsStore()
const router = useRouter()

function handleClose(id) {
  tabsStore.closeTab(id, router)
}
</script>

<style scoped>
.tab-bar {
  display: flex;
  align-items: center;
  height: 36px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 8px;
  overflow-x: auto;
  white-space: nowrap;
}
.tab-item {
  display: inline-flex;
  align-items: center;
  height: 28px;
  padding: 0 10px;
  margin-right: 4px;
  font-size: 13px;
  color: #606266;
  background: #f4f4f5;
  border-radius: 4px;
  cursor: pointer;
  transition: background .2s;
}
.tab-item.active {
  color: #409EFF;
  background: #ecf5ff;
}
.tab-item:hover { background: #e8e8eb; }
.tab-item.active:hover { background: #d9ecff; }
.tab-title { max-width: 140px; overflow: hidden; text-overflow: ellipsis; }
.tab-close {
  margin-left: 6px;
  font-size: 12px;
  border-radius: 50%;
  padding: 1px;
}
.tab-close:hover { background: #c0c4cc; color: #fff; }
</style>
