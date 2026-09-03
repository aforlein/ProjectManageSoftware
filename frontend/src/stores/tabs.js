import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

export const useTabsStore = defineStore('tabs', () => {
  const tabs = ref([])
  const activeTab = ref(null)

  // 打开或聚焦页签
  function openTab(id, title, path) {
    const existing = tabs.value.find(t => t.id === id)
    if (existing) {
      activeTab.value = id
    } else {
      tabs.value.push({ id, title, path })
      activeTab.value = id
    }
  }

  // 关闭页签
  function closeTab(id, router) {
    const idx = tabs.value.findIndex(t => t.id === id)
    if (idx === -1) return

    tabs.value.splice(idx, 1)

    if (activeTab.value === id) {
      if (tabs.value.length > 0) {
        // 激活相邻页签
        const next = tabs.value[Math.min(idx, tabs.value.length - 1)]
        activeTab.value = next.id
        if (router) router.push(next.path)
      } else {
        activeTab.value = null
        if (router) router.push('/project')
      }
    }
  }

  // 根据路由设置激活页签
  function setActiveByPath(path) {
    const tab = tabs.value.find(t => path.includes(t.path) || t.path.includes(path))
    if (tab) activeTab.value = tab.id
  }

  const hasTabs = computed(() => tabs.value.length > 0)

  return { tabs, activeTab, openTab, closeTab, setActiveByPath, hasTabs }
})
