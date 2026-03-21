<template>
  <nav class="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 flex justify-around items-center px-4 pb-6 pt-2 bg-white/80 backdrop-blur-xl rounded-t-[3rem] shadow-[0_-4px_24px_rgba(36,44,81,0.04)]">
    <RouterLink
      v-for="item in navItems"
      :key="item.to"
      :to="item.to"
      custom
      v-slot="{ navigate, isActive }"
    >
      <button
        @click="navigate"
        :class="[
          'flex flex-col items-center justify-center px-4 py-2 rounded-2xl transition-all duration-200',
          isActive
            ? 'text-[#3B82F6] bg-[#e4e7ff]'
            : 'text-[#242c51]/60 hover:text-[#3B82F6]'
        ]"
      >
        <span
          class="material-symbols-outlined text-[24px]"
          :style="isActive ? `font-variation-settings: 'FILL' 1` : ''"
        >{{ item.icon }}</span>
        <span class="font-inter text-[10px] font-semibold tracking-wide mt-1">{{ item.label }}</span>
      </button>
    </RouterLink>
  </nav>
</template>

<script setup>
import { RouterLink } from 'vue-router'

const navItems = [
  { to: '/', icon: 'home', label: '首页' },
  { to: '/courses', icon: 'menu_book', label: '课程' },
  { to: '/community', icon: 'group', label: '社群' },
  { to: '/practice', icon: 'edit_square', label: '做题' },
  { to: '/profile', icon: 'person', label: '我的' }
]
</script>
