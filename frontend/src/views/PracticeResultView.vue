<template>
  <div class="bg-surface text-on-surface min-h-screen flex flex-col items-center justify-center px-6 pb-32">
    <TopBar title="练习结果" />

    <div class="mt-20 w-full max-w-sm space-y-8">
      <!-- Score Circle -->
      <div class="flex flex-col items-center gap-4">
        <div class="relative w-40 h-40">
          <svg class="w-full h-full -rotate-90">
            <circle cx="70" cy="70" r="60" fill="transparent" class="text-surface-container-highest" stroke="currentColor" stroke-width="10"></circle>
            <circle cx="70" cy="70" r="60" fill="transparent" class="text-primary" stroke="currentColor" stroke-width="10"
              :stroke-dasharray="`${scorePct * 3.77} 377`"
              stroke-linecap="round"></circle>
          </svg>
          <div class="absolute inset-0 flex flex-col items-center justify-center">
            <span class="text-4xl font-extrabold font-headline text-primary">{{ result.score }}</span>
            <span class="text-sm text-on-surface-variant">分</span>
          </div>
        </div>

        <div>
          <h2 class="text-2xl font-extrabold font-headline text-center text-on-surface">
            {{ getGrade(result.score) }}
          </h2>
          <p class="text-center text-on-surface-variant text-sm mt-1">
            答对 {{ result.correctCount }} / {{ result.total }} 题
          </p>
        </div>
      </div>

      <!-- Stats Grid -->
      <div class="grid grid-cols-3 gap-3">
        <div class="bg-surface-container-lowest p-4 rounded-xl text-center shadow-sm">
          <span class="text-2xl font-extrabold font-headline text-primary">{{ result.score }}</span>
          <p class="text-xs text-on-surface-variant mt-1">得分</p>
        </div>
        <div class="bg-surface-container-lowest p-4 rounded-xl text-center shadow-sm border-b-4 border-tertiary">
          <span class="text-2xl font-extrabold font-headline text-tertiary">{{ result.correctCount }}</span>
          <p class="text-xs text-on-surface-variant mt-1">正确</p>
        </div>
        <div class="bg-surface-container-lowest p-4 rounded-xl text-center shadow-sm">
          <span class="text-2xl font-extrabold font-headline text-secondary">{{ result.timeSec ? formatTime(result.timeSec) : '-' }}</span>
          <p class="text-xs text-on-surface-variant mt-1">用时</p>
        </div>
      </div>

      <!-- Leaderboard -->
      <div class="space-y-3">
        <h3 class="font-headline font-bold text-base flex items-center gap-2">
          <span class="material-symbols-outlined text-tertiary" style="font-variation-settings:'FILL' 1">leaderboard</span>
          今日排行榜
        </h3>
        <div class="bg-surface-container-lowest rounded-xl overflow-hidden shadow-sm">
          <div v-for="(player, i) in leaderboard" :key="player.rank || i"
            :class="['flex items-center gap-4 px-4 py-3', i < leaderboard.length - 1 ? 'border-b border-surface-container' : '', player.isMe ? 'bg-primary-container/20' : '']">
            <div :class="['w-7 h-7 rounded-full flex items-center justify-center font-extrabold text-sm flex-shrink-0',
              i === 0 ? 'bg-[#FFD700] text-white' : i === 1 ? 'bg-[#C0C0C0] text-white' : i === 2 ? 'bg-[#CD7F32] text-white' : 'bg-surface-container text-on-surface-variant']">
              {{ player.rank || i + 1 }}
            </div>
            <img v-if="player.avatarUrl" :src="player.avatarUrl" class="w-9 h-9 rounded-full flex-shrink-0 object-cover" />
            <div v-else :class="['w-9 h-9 rounded-full flex items-center justify-center font-bold text-sm flex-shrink-0',
              player.isMe ? 'bg-primary text-on-primary' : getAvatarStyle(i).bg + ' ' + getAvatarStyle(i).color]">
              {{ player.nickname?.[0] || '?' }}
            </div>
            <span class="flex-1 font-medium text-sm" :class="player.isMe ? 'text-primary font-bold' : ''">
              {{ player.nickname }}{{ player.isMe ? ' (我)' : '' }}
            </span>
            <span class="font-extrabold text-sm" :class="player.isMe ? 'text-primary' : 'text-on-surface'">{{ player.score }}分</span>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="space-y-3">
        <RouterLink to="/practice">
          <button class="w-full bg-primary text-on-primary py-4 rounded-full font-headline font-bold text-lg shadow-lg shadow-primary/20 active:scale-95 transition-all">
            再练一次
          </button>
        </RouterLink>
        <RouterLink to="/">
          <button class="w-full border-2 border-primary text-primary py-4 rounded-full font-headline font-bold text-lg active:scale-95 transition-all">
            返回首页
          </button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import TopBar from '@/components/TopBar.vue'
import { getLeaderboard } from '@/api/practice'

const route = useRoute()
const result = computed(() => history.state?.result || { score: 80, correctCount: 8, total: 10, timeSec: 420 })
const scorePct = computed(() => result.value.score || 0)

const avatarColors = [
  { bg: 'bg-secondary-container', color: 'text-secondary' },
  { bg: 'bg-tertiary-container', color: 'text-tertiary' },
  { bg: 'bg-primary-container', color: 'text-primary' },
  { bg: 'bg-surface-container-high', color: 'text-on-surface-variant' },
]

const fallbackLeaderboard = [
  { rank: 1, nickname: '学习达人小李', avatarUrl: null, score: 100, isMe: false },
  { rank: 2, nickname: '数学王者', avatarUrl: null, score: 90, isMe: false },
  { rank: 3, nickname: '我', avatarUrl: null, score: result.value.score || 80, isMe: true },
  { rank: 4, nickname: '设计喵酱', avatarUrl: null, score: 70, isMe: false },
  { rank: 5, nickname: '码农小张', avatarUrl: null, score: 60, isMe: false },
]

const leaderboard = ref(fallbackLeaderboard)

onMounted(async () => {
  try {
    const data = await getLeaderboard()
    if (data && data.length > 0) {
      leaderboard.value = data
    }
  } catch {
    // keep fallback data
  }
})

function getAvatarStyle(index) {
  const style = avatarColors[index % avatarColors.length]
  return style
}

function getGrade(score) {
  if (score >= 90) return '优秀！太棒了！🎉'
  if (score >= 70) return '良好，继续努力！'
  if (score >= 60) return '及格，加油！'
  return '需要加强练习'
}

function formatTime(sec) {
  const m = Math.floor(sec / 60)
  const s = sec % 60
  return `${m}:${String(s).padStart(2, '0')}`
}
</script>
