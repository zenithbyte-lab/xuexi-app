<template>
  <div class="bg-surface text-on-surface min-h-screen pb-32">
    <TopBar title="发布动态" />

    <main class="pt-16 px-6 space-y-6">
      <!-- Type Selection -->
      <div class="pt-4 flex gap-3 overflow-x-auto no-scrollbar">
        <button
          v-for="t in types"
          :key="t.value"
          @click="form.type = t.value"
          :class="[
            'whitespace-nowrap px-5 py-2 rounded-full text-sm font-semibold transition-colors',
            form.type === t.value
              ? 'bg-primary text-on-primary'
              : 'bg-surface-container text-on-surface-variant'
          ]"
        >{{ t.label }}</button>
      </div>

      <!-- Content Input -->
      <div class="bg-surface-container-lowest rounded-xl p-4">
        <textarea
          v-model="form.content"
          placeholder="分享你的学习心得、提出问题..."
          rows="6"
          class="w-full bg-transparent border-none outline-none text-on-surface placeholder:text-outline/60 resize-none text-sm leading-relaxed"
        ></textarea>
      </div>

      <!-- Image Upload Placeholder -->
      <div class="flex gap-3">
        <button class="w-20 h-20 bg-surface-container rounded-xl flex flex-col items-center justify-center gap-1 text-on-surface-variant hover:bg-surface-container-high transition-colors">
          <span class="material-symbols-outlined">add_photo_alternate</span>
          <span class="text-xs">添加图片</span>
        </button>
      </div>

      <p v-if="error" class="text-error text-sm">{{ error }}</p>

      <button
        @click="handleSubmit"
        :disabled="loading || !form.content.trim()"
        class="w-full bg-primary text-on-primary py-4 rounded-full font-headline font-bold text-lg shadow-lg shadow-primary/20 active:scale-95 transition-all disabled:opacity-60"
      >
        {{ loading ? '发布中...' : '发布' }}
      </button>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import TopBar from '@/components/TopBar.vue'
import { createPost } from '@/api/community'

const router = useRouter()
const types = [
  { label: '经验分享', value: 'share' },
  { label: '学习打卡', value: 'checkin' },
  { label: '提问', value: 'question' }
]
const form = reactive({ type: 'share', content: '' })
const loading = ref(false)
const error = ref('')

async function handleSubmit() {
  if (!form.content.trim()) return
  loading.value = true
  error.value = ''
  try {
    await createPost(form)
    router.push('/community')
  } catch (e) {
    error.value = '发布失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>
