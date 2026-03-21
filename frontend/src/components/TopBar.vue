<template>
  <header class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 flex items-center justify-between px-4 h-16 bg-[#f7f5ff]/80 backdrop-blur-xl">
    <button
      v-if="showBack"
      @click="handleBack"
      class="w-10 h-10 flex items-center justify-center rounded-full hover:bg-[#e4e7ff]/50 transition-colors active:scale-95 duration-200"
    >
      <span class="material-symbols-outlined text-[#3B82F6]">arrow_back</span>
    </button>
    <div v-else class="w-10"></div>

    <h1 class="font-headline font-bold text-lg text-on-surface">{{ title }}</h1>

    <div class="w-10 flex justify-end">
      <slot name="right" />
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  title: { type: String, default: '' },
  showBack: { type: Boolean, default: true }
})

const router = useRouter()

function handleBack() {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}
</script>
