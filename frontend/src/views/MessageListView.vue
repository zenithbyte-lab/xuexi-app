<template>
  <div class="bg-surface text-on-surface min-h-screen pb-32">
    <header class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 flex items-center justify-between px-4 h-16 bg-[#f7f5ff]/80 backdrop-blur-xl">
      <button class="material-symbols-outlined text-on-surface p-2 rounded-full hover:bg-surface-container transition-colors">menu</button>
      <h1 class="font-headline font-bold text-lg">消息中心</h1>
      <button class="material-symbols-outlined text-on-surface p-2 rounded-full hover:bg-surface-container transition-colors">search</button>
    </header>

    <main class="pt-16 px-4 space-y-6">
      <!-- Loading State -->
      <div v-if="loading" class="pt-8 flex flex-col items-center gap-3">
        <div class="w-8 h-8 border-3 border-primary border-t-transparent rounded-full animate-spin"></div>
        <p class="text-sm text-on-surface-variant">加载中...</p>
      </div>

      <template v-else>
        <!-- 系统通知 -->
        <section class="pt-4">
          <h2 class="text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-3 px-1">系统通知</h2>
          <div class="space-y-2">
            <div v-for="n in notifications" :key="n.id" @click="openMsg(n)"
              class="bg-surface-container-lowest rounded-xl p-4 flex items-center gap-4 cursor-pointer active:scale-[0.98] transition-transform">
              <div class="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0" :class="getIconBg(n)">
                <span class="material-symbols-outlined" :class="getIconColor(n)" style="font-variation-settings:'FILL' 1">{{ n.icon || 'notifications' }}</span>
              </div>
              <div class="flex-1 min-w-0">
                <div class="flex justify-between items-center mb-0.5">
                  <span class="font-headline font-bold text-sm">{{ n.title }}</span>
                  <span class="text-[11px] text-on-surface-variant ml-2">{{ n.timeAgo }}</span>
                </div>
                <p class="text-sm text-on-surface-variant truncate">{{ n.content }}</p>
              </div>
              <span v-if="!n.isRead" class="w-2.5 h-2.5 bg-primary rounded-full flex-shrink-0"></span>
            </div>
          </div>
        </section>

        <!-- 学习小组 -->
        <section>
          <h2 class="text-xs font-bold text-on-surface-variant uppercase tracking-wider mb-3 px-1">学习小组</h2>
          <div class="space-y-2">
            <div v-for="c in chats" :key="c.id" @click="openMsg(c)"
              class="bg-surface-container-lowest rounded-xl p-4 flex items-center gap-4 cursor-pointer active:scale-[0.98] transition-transform border border-white/40">
              <div class="relative flex-shrink-0">
                <div class="w-14 h-14 rounded-xl flex items-center justify-center font-bold text-lg" :class="getChatAvatarBg(c)">
                  {{ c.title[0] }}
                </div>
                <div v-if="!c.isRead" class="absolute -top-1 -right-1 min-w-[20px] h-5 bg-error text-white text-[10px] flex items-center justify-center rounded-full font-bold border-2 border-surface px-1">
                  1
                </div>
              </div>
              <div class="flex-1 min-w-0">
                <div class="flex justify-between items-center mb-1">
                  <h3 class="font-headline font-bold text-base truncate">{{ c.title }}</h3>
                  <span class="text-[11px] text-on-surface-variant ml-2">{{ c.timeAgo }}</span>
                </div>
                <p class="text-sm text-on-surface-variant truncate">{{ c.content }}</p>
              </div>
            </div>
          </div>
        </section>

        <section class="relative overflow-hidden bg-primary rounded-xl p-6 text-on-primary mb-4">
          <div class="relative z-10">
            <h3 class="font-headline font-extrabold text-lg mb-2">发现更多学习小组</h3>
            <p class="text-xs text-on-primary/80 mb-4">加入同城小组，与志同道合的伙伴一起开启高效学习之旅。</p>
            <button class="bg-white text-primary px-5 py-2 rounded-full text-xs font-bold active:scale-95 transition-transform">立即探索</button>
          </div>
          <div class="absolute top-0 right-0 w-32 h-32 bg-primary-container/30 blur-3xl"></div>
          <span class="material-symbols-outlined absolute right-4 top-1/2 -translate-y-1/2 opacity-20 text-7xl" style="font-variation-settings:'FILL' 1">groups</span>
        </section>
      </template>
    </main>

    <Teleport to="body">
      <div v-if="selectedMsg" class="fixed inset-0 z-[200]" style="max-width:390px;left:50%;transform:translateX(-50%)">
        <div class="absolute inset-0 bg-black/40" @click="selectedMsg = null"></div>
        <div class="absolute bottom-0 left-0 right-0 bg-surface rounded-t-2xl p-6 space-y-4">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0" :class="getModalIconClasses()">
              <span v-if="selectedMsg.icon" class="material-symbols-outlined" style="font-variation-settings:'FILL' 1">{{ selectedMsg.icon }}</span>
              <span v-else class="font-bold text-lg">{{ selectedMsg.title[0] }}</span>
            </div>
            <div class="flex-1">
              <h3 class="font-headline font-bold">{{ selectedMsg.title }}</h3>
              <p class="text-xs text-on-surface-variant">{{ selectedMsg.timeAgo }}</p>
            </div>
            <button @click="selectedMsg = null" class="material-symbols-outlined text-on-surface-variant">close</button>
          </div>
          <p class="text-sm leading-relaxed bg-surface-container rounded-xl p-4">{{ selectedMsg.content }}</p>
        </div>
      </div>
    </Teleport>

    <BottomNav />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import BottomNav from '@/components/BottomNav.vue'
import { getMessages, markAsRead } from '@/api/message'

const selectedMsg = ref(null)
const loading = ref(true)
const allMessages = ref([])

// Fallback mock data
const mockMessages = [
  { id: 1, type: 'reward', title: '打卡成功', icon: 'check_circle', content: '恭喜你连续打卡第 7 天！已获得「坚持达人」徽章和 50 积分奖励。继续保持！', timeAgo: '1小时前', isRead: false },
  { id: 2, type: 'system', title: '学习提醒', icon: 'notifications', content: '您今天还有一份每日练习未完成。保持学习习惯，每天积累，量变引起质变！', timeAgo: '昨天', isRead: false },
  { id: 3, type: 'system', title: '课程更新', icon: 'auto_stories', content: '您关注的课程《UI设计基础》已更新第 4 章「交互设计原则」，快去学习吧！', timeAgo: '3天前', isRead: true },
  { id: 4, type: 'chat', title: '数学学习小组', icon: 'groups', content: '大家记得看新的例题，明天的随堂测验会从第三章出题，重点复习矩阵运算部分。', timeAgo: '2小时前', isRead: false },
  { id: 5, type: 'chat', title: '王老师', icon: 'person', content: '你的作业已经批改了，整体思路很好，有几个细节下次课重点讲解。继续加油！', timeAgo: '3小时前', isRead: false }
]

// Split by type
const notifications = computed(() =>
  allMessages.value.filter(m => m.type === 'system' || m.type === 'reward')
)
const chats = computed(() =>
  allMessages.value.filter(m => m.type === 'chat')
)

// Style helpers
const iconStyleMap = {
  check_circle: { bg: 'bg-tertiary-container', color: 'text-tertiary' },
  notifications: { bg: 'bg-primary-container', color: 'text-primary' },
  auto_stories: { bg: 'bg-secondary-container', color: 'text-secondary' },
  groups: { bg: 'bg-primary-container', color: 'text-primary' },
  person: { bg: 'bg-secondary-container', color: 'text-secondary' }
}
const defaultIconStyle = { bg: 'bg-primary-container', color: 'text-primary' }

function getIconBg(msg) {
  const s = iconStyleMap[msg.icon] || defaultIconStyle
  return s.bg
}
function getIconColor(msg) {
  const s = iconStyleMap[msg.icon] || defaultIconStyle
  return s.color
}

const chatAvatarColors = ['bg-primary-container text-primary', 'bg-secondary-container text-secondary', 'bg-tertiary-container text-tertiary']
function getChatAvatarBg(c) {
  const idx = chats.value.indexOf(c) % chatAvatarColors.length
  return chatAvatarColors[idx]
}

function getModalIconClasses() {
  if (!selectedMsg.value) return ''
  const s = iconStyleMap[selectedMsg.value.icon] || defaultIconStyle
  return s.bg + ' ' + s.color
}

async function openMsg(m) {
  selectedMsg.value = m
  if (!m.isRead) {
    m.isRead = true
    try {
      await markAsRead(m.id)
    } catch {
      // ignore - already marked locally
    }
  }
}

onMounted(async () => {
  try {
    const data = await getMessages()
    if (data && data.length > 0) {
      allMessages.value = data
    } else {
      allMessages.value = mockMessages
    }
  } catch {
    allMessages.value = mockMessages
  } finally {
    loading.value = false
  }
})
</script>
