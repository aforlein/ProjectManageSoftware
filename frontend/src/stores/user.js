import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getToken, setToken, removeToken, getUser, setUser } from '@/utils/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const userInfo = ref(getUser() || {})

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value.username || '')
  const realName = computed(() => userInfo.value.realName || '')
  const roleCode = computed(() => userInfo.value.roleCode || '')

  function setLoginInfo(newToken, info) {
    token.value = newToken
    setToken(newToken)
    userInfo.value = info
    setUser(info)
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    removeToken()
    router.push('/login')
  }

  function isAdmin() {
    return userInfo.value.roleCode === 'ADMIN'
  }

  function hasRole(role) {
    return userInfo.value.roleCode === role
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    username,
    realName,
    roleCode,
    setLoginInfo,
    logout,
    isAdmin,
    hasRole
  }
})
