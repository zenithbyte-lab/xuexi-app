<template>
  <div class="bg-surface text-on-surface min-h-screen flex flex-col items-center justify-center px-6">
    <!-- Confetti-like background -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-4 h-4 bg-primary/20 rounded-full animate-bounce" style="animation-delay: 0.1s"></div>
      <div class="absolute top-1/3 right-1/3 w-6 h-6 bg-tertiary/20 rounded-full animate-bounce" style="animation-delay: 0.3s"></div>
      <div class="absolute top-1/2 left-1/3 w-3 h-3 bg-secondary/20 rounded-full animate-bounce" style="animation-delay: 0.5s"></div>
    </div>

    <div class="relative z-10 flex flex-col items-center gap-8 w-full max-w-sm">
      <!-- Success Icon -->
      <div class="w-32 h-32 rounded-full bg-gradient-to-br from-primary to-primary-container flex items-center justify-center shadow-2xl shadow-primary/30">
        <span class="material-symbols-outlined text-6xl text-on-primary" style="font-variation-settings: 'FILL' 1">check_circle</span>
      </div>

      <!-- Text -->
      <div class="text-center">
        <h1 class="font-headline text-3xl font-extrabold text-on-surface mb-2">打卡成功！</h1>
        <p class="text-on-surface-variant">已连续打卡 <span class="text-primary font-bold text-lg">{{ streakDays }}</span> 天</p>
        <p class="text-on-surface-variant text-sm mt-1">坚持就是胜利，继续加油！</p>
      </div>

      <!-- Stats Card -->
      <div class="w-full bg-surface-container-lowest rounded-xl p-6 shadow-sm">
        <div class="grid grid-cols-3 gap-4 text-center">
          <div>
            <span class="text-2xl font-extrabold text-primary font-headline">{{ streakDays }}</span>
            <p class="text-xs text-on-surface-variant mt-1">连续天数</p>
          </div>
          <div>
            <span class="text-2xl font-extrabold text-tertiary font-headline">+10</span>
            <p class="text-xs text-on-surface-variant mt-1">获得积分</p>
          </div>
          <div>
            <span class="text-2xl font-extrabold text-secondary font-headline">Lv.8</span>
            <p class="text-xs text-on-surface-variant mt-1">当前等级</p>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="w-full space-y-3">
        <RouterLink to="/" class="block">
          <button class="w-full bg-primary text-on-primary py-4 rounded-full font-headline font-bold text-lg shadow-lg shadow-primary/20 active:scale-95 transition-all">
            继续学习
          </button>
        </RouterLink>
        <RouterLink to="/community/post" class="block">
          <button class="w-full border-2 border-primary text-primary py-4 rounded-full font-headline font-bold text-lg active:scale-95 transition-all">
            分享到社群
          </button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useCheckinStore } from '@/stores/checkin'

const checkinStore = useCheckinStore()
const streakDays = computed(() => checkinStore.stats.streakDays || 7)
</script>
