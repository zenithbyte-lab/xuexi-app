<template>
  <div class="bg-surface text-on-surface min-h-screen pb-32">
    <!-- TopAppBar -->
    <header class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 flex justify-between items-center px-6 py-4 bg-[#f7f5ff]/80 backdrop-blur-xl">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 rounded-full overflow-hidden border-2 border-white/50 bg-surface-container shadow-sm">
          <img
            v-if="user?.avatarUrl"
            :src="user.avatarUrl"
            alt="头像"
            class="w-full h-full object-cover"
          />
          <div v-else class="w-full h-full bg-primary flex items-center justify-center text-on-primary font-bold text-lg">
            {{ user?.nickname?.[0] || '学' }}
          </div>
        </div>
        <h1 class="font-headline text-2xl font-bold text-[#242c51]">
          {{ greeting }}，{{ user?.nickname || '同学' }}
        </h1>
      </div>
      <RouterLink to="/messages">
        <button class="w-10 h-10 flex items-center justify-center hover:bg-[#e4e7ff] rounded-full transition-colors">
          <span class="material-symbols-outlined text-[#3B82F6]">notifications</span>
        </button>
      </RouterLink>
    </header>

    <main class="mt-24 px-6 space-y-6">
      <!-- Daily Check-in Card -->
      <section class="relative overflow-hidden bg-gradient-to-br from-primary to-primary-container rounded-xl p-6 text-on-primary shadow-lg shadow-primary/10">
        <div class="absolute top-0 right-0 w-32 h-32 bg-white/5 rounded-full -mr-10 -mt-10 blur-2xl"></div>
        <div class="flex justify-between items-center relative z-10">
          <div class="space-y-1">
            <p class="text-on-primary/80 font-medium text-sm">连续打卡</p>
            <h2 class="text-3xl font-extrabold tracking-tight">
              已连续打卡{{ checkinStats.streakDays }}天
            </h2>
          </div>
          <button
            @click="handleCheckin"
            :disabled="checkinStats.checkedToday || checkingIn"
            :class="[
              'px-6 py-2.5 rounded-full font-bold text-sm shadow-sm active:scale-95 transition-transform duration-200',
              checkinStats.checkedToday
                ? 'bg-white/30 text-white/70 cursor-not-allowed'
                : 'bg-surface-container-lowest text-primary'
            ]"
          >
            {{ checkinStats.checkedToday ? '已打卡' : '打卡' }}
          </button>
        </div>
      </section>

      <!-- Learning Progress Card -->
      <section class="bg-surface-container-lowest rounded-lg p-6 space-y-4">
        <div class="flex justify-between items-end">
          <div class="space-y-1">
            <span class="text-on-surface-variant text-sm font-medium uppercase tracking-wider">学习进度</span>
            <h3 class="text-lg font-bold">今日学习时长: <span class="text-primary">{{ todayMinutes }}m</span> / {{ targetMinutes }}m</h3>
          </div>
          <span class="text-2xl font-black font-headline text-primary-dim">{{ progressPercent }}%</span>
        </div>
        <div class="w-full h-3 bg-surface-container-highest rounded-full overflow-hidden">
          <div class="h-full bg-primary rounded-full" :style="{ width: progressPercent + '%' }"></div>
        </div>
      </section>

      <!-- Today's Tasks -->
      <section class="space-y-4">
        <div class="flex justify-between items-center">
          <h2 class="font-headline text-xl font-bold text-on-surface">今日任务</h2>
          <RouterLink to="/courses" class="text-primary font-semibold text-sm">查看全部</RouterLink>
        </div>
        <div class="space-y-3">
          <div
            v-for="task in tasks"
            :key="task.id"
            @click="router.push(task.route)"
            class="bg-surface-container-lowest p-4 rounded-lg flex items-center justify-between group hover:bg-surface-container-low transition-colors duration-200 cursor-pointer"
            :class="{ 'border-l-4 border-primary': task.active }"
          >
            <div class="flex items-center gap-4">
              <div :class="`w-12 h-12 ${task.iconBg} flex items-center justify-center rounded-full ${task.iconColor}`">
                <span class="material-symbols-outlined">{{ task.icon }}</span>
              </div>
              <div>
                <h4 class="font-bold text-on-surface">{{ task.title }}</h4>
                <p class="text-xs text-on-surface-variant flex items-center gap-1 mt-1">
                  <span class="material-symbols-outlined text-[14px]">schedule</span>
                  {{ task.duration }}
                </p>
              </div>
            </div>
            <button
              :class="[
                'w-8 h-8 rounded-lg border-2 flex items-center justify-center transition-colors',
                task.done
                  ? 'border-primary bg-primary'
                  : 'border-outline-variant group-hover:border-primary'
              ]"
            >
              <span
                class="material-symbols-outlined text-[18px]"
                :class="task.done ? 'text-white' : 'text-transparent group-hover:text-primary'"
                :style="task.done ? `font-variation-settings: 'FILL' 1` : ''"
              >check</span>
            </button>
          </div>
        </div>
      </section>

      <!-- Bento Grid Insights -->
      <section class="grid grid-cols-2 gap-4 pb-8">
        <div class="col-span-1 bg-surface-container-high p-5 rounded-lg space-y-3">
          <span class="material-symbols-outlined text-secondary">trending_up</span>
          <p class="text-sm font-bold leading-tight">本周学习高峰</p>
          <p class="text-xs text-on-surface-variant">已学习 {{ courseStore.courses.length }} 门课程</p>
        </div>
        <div class="col-span-1 bg-white p-5 rounded-lg border border-surface-container space-y-3">
          <span class="material-symbols-outlined text-tertiary">emoji_events</span>
          <p class="text-sm font-bold leading-tight">{{ rewardCountdown }}天后获得奖励</p>
          <div class="flex -space-x-2">
            <div class="w-6 h-6 rounded-full bg-surface-container border border-white"></div>
            <div class="w-6 h-6 rounded-full bg-surface-container border border-white"></div>
          </div>
        </div>
      </section>
    </main>

    <BottomNav />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import BottomNav from '@/components/BottomNav.vue'
import { useUserStore } from '@/stores/user'
import { useCheckinStore } from '@/stores/checkin'
import { useCourseStore } from '@/stores/course'

const router = useRouter()
const userStore = useUserStore()
const checkinStore = useCheckinStore()
const courseStore = useCourseStore()

const user = computed(() => userStore.user)
const checkinStats = computed(() => checkinStore.stats)
const checkingIn = ref(false)

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const taskIcons = [
  { icon: 'functions', iconBg: 'bg-secondary-container', iconColor: 'text-on-secondary-container' },
  { icon: 'translate', iconBg: 'bg-tertiary-container', iconColor: 'text-on-tertiary-container' },
  { icon: 'palette', iconBg: 'bg-primary-container/20', iconColor: 'text-primary' }
]

const defaultTasks = [
  { id: 1, title: '浏览课程列表', icon: 'menu_book', iconBg: 'bg-secondary-container', iconColor: 'text-on-secondary-container', duration: '10 mins', done: false, active: false, route: '/courses' }
]

const tasks = computed(() => {
  const list = courseStore.courses
  if (!list || list.length === 0) return defaultTasks
  return list.slice(0, 3).map((c, i) => {
    const style = taskIcons[i % taskIcons.length]
    const progress = c.progress ?? 0
    const done = progress >= 100
    return {
      id: c.id,
      title: c.title || c.name,
      icon: style.icon,
      iconBg: style.iconBg,
      iconColor: style.iconColor,
      duration: `${c.totalChapters ?? 10} 章节`,
      done,
      active: !done && progress > 0,
      route: `/courses/${c.id}`
    }
  })
})

const targetMinutes = computed(() => {
  const count = courseStore.courses.length
  return count > 0 ? count * 20 : 60
})

const todayMinutes = computed(() => {
  const list = courseStore.courses
  if (!list || list.length === 0) return 0
  const totalProgress = list.reduce((sum, c) => sum + (c.progress ?? 0), 0)
  const avgProgress = totalProgress / list.length
  return Math.round((avgProgress / 100) * targetMinutes.value)
})

const progressPercent = computed(() => {
  if (targetMinutes.value === 0) return 0
  return Math.min(100, Math.round((todayMinutes.value / targetMinutes.value) * 100))
})

const rewardCountdown = computed(() => {
  const streak = checkinStats.value?.streakDays ?? 0
  const nextMilestone = streak < 7 ? 7 : streak < 14 ? 14 : streak < 30 ? 30 : (Math.ceil(streak / 30) * 30)
  return Math.max(1, nextMilestone - streak)
})

async function handleCheckin() {
  if (checkinStats.value.checkedToday || checkingIn.value) return
  checkingIn.value = true
  try {
    await checkinStore.checkin()
    router.push('/checkin/success')
  } catch (e) {
    console.error(e)
  } finally {
    checkingIn.value = false
  }
}

onMounted(async () => {
  try {
    await Promise.all([
      userStore.fetchProfile(),
      checkinStore.fetchStats(),
      courseStore.fetchCourses()
    ])
  } catch (e) {
    console.error(e)
  }
})
</script>
