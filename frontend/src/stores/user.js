import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getProfile } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  function setToken(t) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }

  async function fetchProfile() {
    const data = await getProfile()
    user.value = data
    return data
  }

  return { user, token, setToken, logout, fetchProfile }
})
