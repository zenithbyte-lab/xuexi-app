<template>
  <div class="bg-surface text-on-surface min-h-screen pb-32">
    <header class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 bg-[#f7f5ff]/80 backdrop-blur-xl flex justify-between items-center px-6 py-4">
      <button @click="router.back()" class="active:scale-95 duration-200 hover:bg-blue-50/50 rounded-full transition-all p-2">
        <span class="material-symbols-outlined text-blue-600">arrow_back</span>
      </button>
      <h1 class="font-headline font-bold text-lg tracking-tight text-on-surface">我的</h1>
      <button class="active:scale-95 duration-200 hover:bg-blue-50/50 rounded-full transition-all p-2">
        <span class="material-symbols-outlined text-blue-600">settings</span>
      </button>
    </header>

    <main class="pt-20 px-6 space-y-8">
      <!-- Profile Header -->
      <section class="flex flex-col items-center mt-4">
        <div class="relative">
          <div class="w-24 h-24 rounded-xl overflow-hidden bg-surface-container-highest shadow-xl border-4 border-surface-container-lowest">
            <div v-if="!user?.avatarUrl" class="w-full h-full bg-gradient-to-br from-primary to-secondary flex items-center justify-center text-on-primary font-bold text-3xl">
              {{ user?.nickname?.[0] || '学' }}
            </div>
            <img v-else :src="user.avatarUrl" class="w-full h-full object-cover" />
          </div>
          <div class="absolute -bottom-2 -right-2 bg-primary text-on-primary px-3 py-1 rounded-full text-[10px] font-bold shadow-lg">
            {{ userLevel }}
          </div>
        </div>
        <div class="mt-4 text-center">
          <h2 class="text-2xl font-extrabold tracking-tight text-on-surface">{{ user?.nickname || '同学' }}</h2>
          <p class="text-on-surface-variant text-sm mt-1">立志成为终身学习者</p>
        </div>
        <button @click="showEditModal = true" class="mt-4 px-8 py-2.5 bg-primary text-on-primary rounded-full font-semibold text-sm shadow-md active:scale-95 transition-transform">
          编辑资料
        </button>
      </section>

      <!-- Stats Row -->
      <section class="grid grid-cols-3 gap-3">
        <div class="bg-surface-container-lowest p-4 rounded-lg flex flex-col items-center justify-center text-center shadow-sm">
          <span class="text-primary font-headline font-bold text-xl">{{ user?.completedCourses || 0 }}</span>
          <span class="text-on-surface-variant text-[10px] mt-1">已完成课程</span>
        </div>
        <div class="bg-surface-container-lowest p-4 rounded-lg flex flex-col items-center justify-center text-center shadow-sm border-b-4 border-tertiary">
          <span class="text-tertiary font-headline font-bold text-xl">{{ checkinStats.streakDays || 0 }}</span>
          <span class="text-on-surface-variant text-[10px] mt-1">连续打卡天</span>
        </div>
        <div class="bg-surface-container-lowest p-4 rounded-lg flex flex-col items-center justify-center text-center shadow-sm">
          <span class="text-secondary font-headline font-bold text-xl">{{ user?.points || 0 }}</span>
          <span class="text-on-surface-variant text-[10px] mt-1">当前积分</span>
        </div>
      </section>

      <!-- Achievements -->
      <section class="space-y-4">
        <div class="flex justify-between items-center">
          <h3 class="text-lg font-bold tracking-tight">我的勋章</h3>
          <span class="text-primary text-xs font-semibold cursor-pointer">查看全部</span>
        </div>
        <div class="bg-surface-container-low p-6 rounded-xl flex justify-around items-center">
          <div v-for="badge in badges" :key="badge.label" class="flex flex-col items-center gap-2">
            <div :class="['w-14 h-14 rounded-full flex items-center justify-center shadow-inner', badge.unlocked ? badge.bg : 'bg-surface-container-highest opacity-50']">
              <span :class="['material-symbols-outlined text-3xl', badge.unlocked ? badge.color : 'text-outline-variant']" style="font-variation-settings: 'FILL' 1">{{ badge.icon }}</span>
            </div>
            <span :class="['text-[10px] font-bold', badge.unlocked ? '' : 'text-outline-variant']">{{ badge.label }}</span>
          </div>
        </div>
      </section>

      <!-- Learning Calendar (Heatmap) -->
      <section class="space-y-4">
        <h3 class="text-lg font-bold tracking-tight">学习日历</h3>
        <div class="bg-surface-container-lowest p-5 rounded-xl shadow-sm overflow-hidden">
          <div class="grid grid-cols-7 gap-1.5">
            <div
              v-for="(active, i) in calendarData"
              :key="i"
              :class="[
                'aspect-square rounded-sm',
                active === 2 ? 'bg-primary' : active === 1 ? 'bg-primary-container' : 'bg-surface-container-highest'
              ]"
            ></div>
          </div>
          <div class="mt-4 flex justify-between items-center text-[10px] text-on-surface-variant font-medium">
            <span>本月已学习 {{ activeDays }} 天</span>
            <div class="flex gap-1 items-center">
              <span>少</span>
              <div class="w-2 h-2 bg-surface-container-highest rounded-sm"></div>
              <div class="w-2 h-2 bg-primary-container rounded-sm"></div>
              <div class="w-2 h-2 bg-primary rounded-sm"></div>
              <span>多</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Menu List -->
      <section class="bg-surface-container-low rounded-xl overflow-hidden">
        <div v-for="item in menuItems" :key="item.label"
          @click="handleMenu(item)"
          :class="[
            'flex items-center justify-between p-4 transition-colors cursor-pointer',
            item.danger ? 'hover:bg-error-container/10 mt-2' : 'hover:bg-surface-container-high'
          ]"
        >
          <div class="flex items-center gap-4">
            <span :class="`material-symbols-outlined ${item.danger ? 'text-error' : 'text-on-surface-variant'}`">{{ item.icon }}</span>
            <span :class="`text-sm font-medium ${item.danger ? 'text-error' : ''}`">{{ item.label }}</span>
          </div>
          <div v-if="!item.danger" class="flex items-center gap-2">
            <span v-if="item.badge" class="w-2 h-2 bg-error rounded-full"></span>
            <span class="material-symbols-outlined text-outline-variant text-sm">chevron_right</span>
          </div>
        </div>
      </section>
    </main>

    <BottomNav />

    <!-- Edit Profile Modal -->
    <Teleport to="body">
      <Transition name="fade">
        <div v-if="showEditModal" class="fixed inset-0 z-[100] flex items-end justify-center" @click.self="showEditModal = false">
          <div class="absolute inset-0 bg-black/40" @click="showEditModal = false"></div>
          <div class="relative w-full max-w-[390px] bg-surface-container-lowest rounded-t-2xl p-6 space-y-5 animate-slide-up">
            <div class="flex justify-between items-center">
              <h3 class="text-lg font-bold">编辑资料</h3>
              <button @click="showEditModal = false" class="p-1 rounded-full hover:bg-surface-container-high transition-colors">
                <span class="material-symbols-outlined text-on-surface-variant">close</span>
              </button>
            </div>
            <div class="space-y-2">
              <label class="text-sm font-medium text-on-surface-variant">昵称</label>
              <input
                v-model="editNickname"
                type="text"
                maxlength="20"
                class="w-full px-4 py-3 rounded-xl bg-surface-container-high text-on-surface text-sm outline-none focus:ring-2 focus:ring-primary/30 transition-all"
                placeholder="输入新昵称"
              />
            </div>
            <button
              @click="saveProfile"
              :disabled="editSaving"
              class="w-full py-3 bg-primary text-on-primary rounded-full font-semibold text-sm shadow-md active:scale-95 transition-transform disabled:opacity-50"
            >
              {{ editSaving ? '保存中...' : '保存' }}
            </button>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import BottomNav from '@/components/BottomNav.vue'
import { useUserStore } from '@/stores/user'
import { useCheckinStore } from '@/stores/checkin'
import { updateProfile } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const checkinStore = useCheckinStore()

const user = computed(() => userStore.user)
const checkinStats = computed(() => checkinStore.stats)

// --- Level badge computed from points ---
const userLevel = computed(() => {
  const pts = user.value?.points || 0
  if (pts < 100) return 'Lv.1 新手'
  if (pts < 500) return 'Lv.2 初学'
  if (pts < 1000) return 'Lv.3 进阶'
  if (pts < 2000) return 'Lv.4 达人'
  if (pts < 5000) return 'Lv.5 学霸'
  return 'Lv.6 大师'
})

// --- Badges computed from user stats ---
const badges = computed(() => {
  const completed = user.value?.completedCourses || 0
  const streak = checkinStats.value?.streakDays || 0
  return [
    {
      label: '学习达人',
      icon: 'workspace_premium',
      bg: 'bg-surface-container-highest',
      color: 'text-primary',
      unlocked: completed >= 5
    },
    {
      label: '打卡30天',
      icon: 'calendar_month',
      bg: 'bg-tertiary-container',
      color: 'text-tertiary',
      unlocked: streak >= 30
    },
    {
      label: '课程达人',
      icon: 'menu_book',
      bg: 'bg-secondary-container',
      color: 'text-secondary',
      unlocked: completed >= 10
    }
  ]
})

// --- Calendar from checkin store with fallback ---
const calendarData = computed(() => {
  const mc = checkinStats.value?.monthCalendar
  if (mc && mc.length > 0) return mc
  // Fallback: empty calendar for current month
  const now = new Date()
  const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate()
  return Array(daysInMonth).fill(0)
})
const activeDays = computed(() => calendarData.value.filter(d => d > 0).length)

// --- Edit profile modal ---
const showEditModal = ref(false)
const editNickname = ref('')
const editSaving = ref(false)

watch(showEditModal, (val) => {
  if (val) {
    editNickname.value = user.value?.nickname || ''
  }
})

async function saveProfile() {
  if (!editNickname.value.trim()) return
  editSaving.value = true
  try {
    await updateProfile({ nickname: editNickname.value.trim() })
    await userStore.fetchProfile()
    showEditModal.value = false
  } catch (e) {
    console.error('保存失败', e)
  } finally {
    editSaving.value = false
  }
}

// --- Menu ---
const menuItems = [
  { label: '账号设置', icon: 'manage_accounts' },
  { label: '消息通知', icon: 'notifications', badge: true },
  { label: '隐私设置', icon: 'security' },
  { label: '帮助与反馈', icon: 'help' },
  { label: '退出登录', icon: 'logout', danger: true }
]

onMounted(async () => {
  try {
    await Promise.all([userStore.fetchProfile(), checkinStore.fetchStats()])
  } catch (e) {
    console.error(e)
  }
})

function handleMenu(item) {
  if (item.danger) {
    if (confirm('确定要退出登录吗？')) {
      userStore.logout()
      router.push('/login')
    }
  }
}
</script>
