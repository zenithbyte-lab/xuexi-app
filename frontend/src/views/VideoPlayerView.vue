<template>
  <div class="bg-surface text-on-surface min-h-screen pb-32">
    <TopBar :title="currentChapter?.title || '视频播放'" />

    <main class="pt-16 pb-32">
      <!-- Video Player -->
      <section class="relative w-full aspect-video bg-black overflow-hidden">
        <div class="w-full h-full bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
          <span class="material-symbols-outlined text-6xl text-white/20">play_circle</span>
        </div>
        <!-- Controls Overlay -->
        <div class="absolute inset-0 flex flex-col justify-between p-4" style="background: linear-gradient(180deg, rgba(0,0,0,0) 0%, rgba(0,0,0,0.8) 100%)">
          <div class="flex justify-end">
            <span class="bg-black/40 backdrop-blur-md text-white text-xs px-2 py-1 rounded-md font-medium">1.0x</span>
          </div>
          <div class="flex flex-col gap-2">
            <div class="relative w-full h-2 bg-white/30 rounded-full overflow-hidden cursor-pointer" @click="seekProgress">
              <div class="absolute top-0 left-0 h-full bg-primary-container" :style="`width: ${playProgress}%`"></div>
            </div>
            <div class="flex items-center justify-between text-white">
              <div class="flex items-center gap-4">
                <button @click="playing = !playing" class="flex items-center justify-center">
                  <span class="material-symbols-outlined text-3xl" style="font-variation-settings: 'FILL' 1">
                    {{ playing ? 'pause' : 'play_arrow' }}
                  </span>
                </button>
                <span class="text-xs font-medium font-headline">{{ formatTime(currentTime) }} / {{ formatTime(totalTime) }}</span>
              </div>
              <div class="flex items-center gap-4">
                <span class="material-symbols-outlined text-xl">volume_up</span>
                <span class="material-symbols-outlined text-xl">fullscreen</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Video Info -->
      <section class="px-6 py-8 bg-surface-container-lowest">
        <div class="flex flex-col gap-2">
          <span class="text-primary font-semibold text-sm tracking-wide">{{ course?.title }}</span>
          <h2 class="text-2xl font-bold font-headline text-on-surface leading-tight">{{ currentChapter?.title }}</h2>
          <div class="flex items-center gap-4 mt-2">
            <div class="flex items-center gap-1 text-on-surface-variant text-xs">
              <span class="material-symbols-outlined text-sm">visibility</span>
              <span>1.2k 次观看</span>
            </div>
            <div class="flex items-center gap-1 text-on-surface-variant text-xs">
              <span class="material-symbols-outlined text-sm">thumb_up</span>
              <span>458 赞</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Tabs -->
      <section class="mt-2 bg-surface-container-lowest sticky top-16 z-40">
        <div class="flex px-6 border-b border-surface-variant/30">
          <button
            v-for="tab in tabs"
            :key="tab"
            @click="activeTab = tab"
            :class="[
              'py-4 px-4 text-sm font-medium transition-colors relative',
              activeTab === tab ? 'text-primary font-bold' : 'text-on-surface-variant hover:text-primary'
            ]"
          >
            {{ tab }}
            <div v-if="activeTab === tab" class="absolute bottom-0 left-0 w-full h-1 bg-primary rounded-t-full"></div>
          </button>
        </div>
      </section>

      <!-- Tab Content: Chapter List -->
      <section v-if="activeTab === '目录'" class="px-4 py-6 flex flex-col gap-3">
        <div
          v-for="(chapter, idx) in chapters"
          :key="chapter.id"
          @click="selectChapter(chapter)"
          :class="[
            'p-4 rounded-lg flex items-center justify-between cursor-pointer transition-all duration-300',
            chapter.id === currentChapterId
              ? 'bg-primary-container/20 border border-primary/10 shadow-sm'
              : 'bg-surface-container-low hover:bg-surface-variant/40'
          ]"
        >
          <div class="flex items-center gap-4">
            <div :class="[
              'w-8 h-8 flex items-center justify-center font-headline font-bold text-sm',
              chapter.id === currentChapterId ? 'text-primary' : 'text-on-surface-variant'
            ]">{{ String(idx + 1).padStart(2, '0') }}</div>
            <div class="flex flex-col">
              <span :class="['text-sm', chapter.id === currentChapterId ? 'text-primary font-bold' : 'text-on-surface font-medium']">
                {{ chapter.title }}
              </span>
              <span v-if="chapter.id !== currentChapterId" class="text-on-surface-variant text-xs mt-1">{{ formatTime(chapter.durationSec) }}</span>
              <span v-else class="text-primary/70 text-xs mt-1">正在播放 • {{ formatTime(chapter.durationSec) }}</span>
            </div>
          </div>
          <div v-if="chapter.id === currentChapterId" class="w-8 h-8 bg-primary rounded-full flex items-center justify-center shadow-lg shadow-primary/20">
            <span class="material-symbols-outlined text-white text-lg">equalizer</span>
          </div>
          <span v-else-if="!chapter.unlocked" class="material-symbols-outlined text-on-surface-variant/50" style="font-variation-settings: 'FILL' 1">lock</span>
          <span v-else class="material-symbols-outlined text-on-surface-variant/50">play_circle</span>
        </div>
      </section>

      <!-- Tab Content: Notes -->
      <section v-else-if="activeTab === '笔记'" class="px-6 py-12 flex flex-col items-center gap-4 text-center">
        <span class="material-symbols-outlined text-5xl text-on-surface-variant/30">edit_note</span>
        <p class="text-on-surface-variant text-sm">暂无笔记，点击右下角添加</p>
      </section>

      <!-- Tab Content: Comments -->
      <section v-else-if="activeTab === '评论'" class="px-6 py-12 flex flex-col items-center gap-4 text-center">
        <span class="material-symbols-outlined text-5xl text-on-surface-variant/30">chat_bubble_outline</span>
        <p class="text-on-surface-variant text-sm">暂无评论</p>
      </section>
    </main>

    <!-- FAB -->
    <button class="fixed bottom-28 right-6 flex items-center gap-2 px-6 py-4 bg-primary text-on-primary rounded-full shadow-xl shadow-primary/30 z-50 hover:scale-105 active:scale-95 transition-all">
      <span class="material-symbols-outlined">edit_note</span>
      <span class="font-bold text-sm tracking-wide">记笔记</span>
    </button>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount } from 'vue'
import { useRoute } from 'vue-router'
import TopBar from '@/components/TopBar.vue'
import { getCourseDetail, updateProgress } from '@/api/course'

const route = useRoute()
const course = ref(null)
const playing = ref(false)
const currentTime = ref(0)
const totalTime = ref(0)
const activeTab = ref('目录')
const tabs = ['目录', '笔记', '评论']
const currentChapterId = ref(null)
let playInterval = null

const playProgress = computed(() => (totalTime.value > 0 ? (currentTime.value / totalTime.value) * 100 : 0))
const chapters = computed(() => course.value?.chapters || [])
const currentChapter = computed(() => chapters.value.find(c => c.id === currentChapterId.value))

function startPlaying() {
  stopPlaying()
  playInterval = setInterval(() => {
    if (currentTime.value < totalTime.value) {
      currentTime.value++
    } else {
      playing.value = false
    }
  }, 1000)
}

function stopPlaying() {
  if (playInterval) {
    clearInterval(playInterval)
    playInterval = null
  }
}

watch(playing, (val) => {
  if (val) {
    if (currentTime.value >= totalTime.value) currentTime.value = 0
    startPlaying()
  } else {
    stopPlaying()
  }
})

function seekProgress(event) {
  const rect = event.currentTarget.getBoundingClientRect()
  const ratio = Math.max(0, Math.min(1, (event.clientX - rect.left) / rect.width))
  currentTime.value = Math.floor(ratio * totalTime.value)
}

function formatTime(sec) {
  if (!sec) return '0:00'
  const m = Math.floor(sec / 60)
  const s = sec % 60
  return `${m}:${String(s).padStart(2, '0')}`
}

function selectChapter(chapter) {
  if (!chapter.unlocked) return
  currentChapterId.value = chapter.id
  currentTime.value = 0
  totalTime.value = chapter.durationSec || 0
  if (playing.value) {
    startPlaying()
  }
}

async function saveProgress() {
  if (!course.value || !currentChapterId.value) return
  const pct = totalTime.value > 0 ? Math.round((currentTime.value / totalTime.value) * 100) : 0
  try {
    await updateProgress(route.params.id, {
      currentChapter: currentChapterId.value,
      progressPct: pct
    })
  } catch (e) {
    // silent — user is leaving
  }
}

onMounted(async () => {
  try {
    course.value = await getCourseDetail(route.params.id)
  } catch (e) {
    course.value = {
      id: route.params.id,
      title: '线性代数基础',
      chapters: [
        { id: 1, title: '第1节：线性方程组', durationSec: 1935, unlocked: true, completed: true },
        { id: 2, title: '第2节：矩阵运算', durationSec: 2700, unlocked: true, completed: false },
        { id: 3, title: '第3节：特征值与特征向量', durationSec: 3500, unlocked: false, completed: false },
        { id: 4, title: '第4节：向量空间', durationSec: 2465, unlocked: false, completed: false }
      ]
    }
  }
  const chapterQuery = route.query.chapter
  if (chapterQuery) {
    currentChapterId.value = Number(chapterQuery)
  } else {
    currentChapterId.value = chapters.value.find(c => !c.completed)?.id || chapters.value[0]?.id
  }
  // Initialize totalTime from the selected chapter
  if (currentChapter.value) {
    totalTime.value = currentChapter.value.durationSec || 0
  }
})

onBeforeUnmount(() => {
  stopPlaying()
  saveProgress()
})
</script>
