<template>
  <div class="bg-surface font-body text-on-surface min-h-screen flex flex-col">
    <!-- TopAppBar -->
    <header class="w-full px-4 pt-4 flex items-center justify-between max-w-md mx-auto bg-transparent">
      <span class="material-symbols-outlined text-blue-600 cursor-pointer" @click="router.push('/')">close</span>
      <div class="text-2xl font-bold text-blue-700 tracking-tight font-headline">学学</div>
      <div class="w-8"></div>
    </header>

    <main class="flex-grow flex flex-col px-8 pt-12 max-w-md mx-auto w-full">
      <div class="mb-12">
        <h1 class="font-headline font-bold text-4xl text-on-surface tracking-tight mb-2">欢迎回来</h1>
        <p class="text-on-surface-variant">继续你的学习之旅，探索无限可能</p>
      </div>

      <div class="space-y-6">
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
              placeholder="请输入密码"
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

        <div class="flex justify-end">
          <a class="text-sm font-medium text-primary hover:opacity-80 transition-opacity cursor-pointer">忘记密码</a>
        </div>

        <p v-if="error" class="text-error text-sm text-center">{{ error }}</p>

        <button
          @click="handleLogin"
          :disabled="loading"
          class="w-full bg-primary text-on-primary py-4 rounded-full font-headline font-bold text-lg shadow-lg shadow-primary/20 active:scale-95 transition-all disabled:opacity-60"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </div>

      <div class="relative my-12">
        <div class="absolute inset-0 flex items-center">
          <div class="w-full border-t border-outline-variant/20"></div>
        </div>
        <div class="relative flex justify-center text-sm">
          <span class="px-4 bg-surface text-outline-variant font-medium">或</span>
        </div>
      </div>

      <div class="text-center mt-auto pb-32">
        <p class="text-on-surface-variant text-sm">
          没有账号？
          <RouterLink to="/register" class="text-primary font-bold hover:underline">立即注册</RouterLink>
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { RouterLink } from 'vue-router'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({ phone: '', password: '' })
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  if (!form.phone || !form.password) {
    error.value = '请输入手机号和密码'
    return
  }
  loading.value = true
  error.value = ''
  try {
    const res = await login(form.phone, form.password)
    userStore.setToken(res.token)
    router.push('/')
  } catch (e) {
    error.value = e.message || '登录失败，请检查账号密码'
  } finally {
    loading.value = false
  }
}
</script>
