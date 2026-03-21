<template>
  <div class="bg-surface font-body text-on-surface min-h-screen flex flex-col">
    <header class="w-full px-4 pt-4 flex items-center justify-between max-w-md mx-auto bg-transparent">
      <span class="material-symbols-outlined text-blue-600 cursor-pointer" @click="router.back()">arrow_back</span>
      <div class="text-2xl font-bold text-blue-700 tracking-tight font-headline">学学</div>
      <div class="w-8"></div>
    </header>

    <main class="flex-grow flex flex-col px-8 pt-12 max-w-md mx-auto w-full">
      <div class="mb-12">
        <h1 class="font-headline font-bold text-4xl text-on-surface tracking-tight mb-2">创建账号</h1>
        <p class="text-on-surface-variant">开始你的学习之旅</p>
      </div>

      <div class="space-y-6">
        <div class="space-y-2">
          <label class="text-xs font-semibold text-on-surface-variant ml-4">昵称</label>
          <input
            v-model="form.nickname"
            type="text"
            placeholder="请输入昵称"
            class="w-full bg-surface-container-low border-none rounded-xl py-4 px-6 focus:ring-2 focus:ring-primary/20 focus:bg-surface-container-lowest transition-all outline-none text-on-surface placeholder:text-outline"
          />
        </div>

        <div class="space-y-2">
          <label class="text-xs font-semibold text-on-surface-variant ml-4">手机号</label>
          <input
            v-model="form.phone"
            type="text"
            placeholder="请输入手机号"
            class="w-full bg-surface-container-low border-none rounded-xl py-4 px-6 focus:ring-2 focus:ring-primary/20 focus:bg-surface-container-lowest transition-all outline-none text-on-surface placeholder:text-outline"
          />
        </div>

        <div class="space-y-2">
          <label class="text-xs font-semibold text-on-surface-variant ml-4">密码</label>
          <div class="relative">
            <input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码（至少6位）"
              class="w-full bg-surface-container-low border-none rounded-xl py-4 px-6 focus:ring-2 focus:ring-primary/20 focus:bg-surface-container-lowest transition-all outline-none text-on-surface placeholder:text-outline"
            />
            <button
              @click="showPassword = !showPassword"
              class="absolute right-4 top-1/2 -translate-y-1/2 text-outline-variant hover:text-primary transition-colors"
            >
              <span class="material-symbols-outlined">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
            </button>
          </div>
        </div>

        <p v-if="error" class="text-error text-sm text-center">{{ error }}</p>

        <button
          @click="handleRegister"
          :disabled="loading"
          class="w-full bg-primary text-on-primary py-4 rounded-full font-headline font-bold text-lg shadow-lg shadow-primary/20 active:scale-95 transition-all disabled:opacity-60"
        >
          {{ loading ? '注册中...' : '立即注册' }}
        </button>
      </div>

      <div class="text-center mt-auto pb-32 pt-8">
        <p class="text-on-surface-variant text-sm">
          已有账号？
          <RouterLink to="/login" class="text-primary font-bold hover:underline">立即登录</RouterLink>
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { register } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({ nickname: '', phone: '', password: '' })
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')

async function handleRegister() {
  if (!form.nickname || !form.phone || !form.password) {
    error.value = '请填写所有字段'
    return
  }
  if (form.password.length < 6) {
    error.value = '密码至少6位'
    return
  }
  loading.value = true
  error.value = ''
  try {
    const res = await register(form.phone, form.password, form.nickname)
    userStore.setToken(res.token)
    router.push('/')
  } catch (e) {
    error.value = e.message || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>
