<template>
  <div class="bg-surface text-on-background min-h-screen pb-32">
    <TopBar title="课程详情" />

    <main class="max-w-xl mx-auto pt-16 pb-32" v-if="course">
      <!-- Hero Section -->
      <section class="px-4 mt-4">
        <div class="relative w-full aspect-video rounded-xl overflow-hidden shadow-sm bg-gradient-to-br from-primary to-primary-container flex items-center justify-center">
          <span class="material-symbols-outlined text-7xl text-on-primary/30">menu_book</span>
          <div class="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent"></div>
        </div>
      </section>

      <!-- Course Header -->
      <section class="px-6 mt-6">
        <div class="flex flex-wrap gap-2 mb-3">
          <span class="px-3 py-1 bg-secondary-container text-on-secondary-container text-xs font-semibold rounded-sm">{{ course.category }}</span>
          <span class="px-3 py-1 bg-secondary-container text-on-secondary-container text-xs font-semibold rounded-sm">{{ course.difficulty }}</span>
          <span class="px-3 py-1 bg-surface-container-highest text-on-surface-variant text-xs font-semibold rounded-sm">共{{ course.totalChapters }}节课</span>
        </div>
        <h2 class="font-headline text-3xl font-extrabold text-on-surface tracking-tight leading-tight">{{ course.title }}</h2>

        <!-- Instructor Info -->
        <div class="mt-6 flex items-center justify-between p-4 bg-surface-container-lowest rounded-lg border border-outline-variant/10">
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-xl bg-primary flex items-center justify-center text-on-primary font-bold text-lg">
              {{ course.teacher?.[0] }}
            </div>
            <div>
              <p class="font-bold text-on-surface">{{ course.teacher }}</p>
              <div class="flex items-center gap-1">
                <span class="material-symbols-outlined text-sm text-[#FFB800]" style="font-variation-settings: 'FILL' 1">star</span>
                <span class="text-xs font-semibold text-on-surface-variant">4.8</span>
                <span class="text-xs text-outline ml-1">{{ course.studentCount || 2340 }}人已学</span>
              </div>
            </div>
          </div>
          <button class="text-primary font-semibold text-sm px-4 py-2 hover:bg-primary/5 rounded-full transition-colors">查看主页</button>
        </div>
      </section>

      <!-- Description -->
      <section class="px-6 mt-8">
        <h3 class="font-headline text-lg font-bold mb-3">课程简介</h3>
        <div class="relative">
          <p class="text-on-surface-variant leading-relaxed text-sm">{{ course.description }}</p>
          <button class="mt-2 text-primary text-xs font-bold flex items-center gap-1">
            展开更多 <span class="material-symbols-outlined text-xs">expand_more</span>
          </button>
        </div>
      </section>

      <!-- Lesson List -->
      <section class="px-6 mt-10">
        <div class="flex items-center justify-between mb-4">
          <h3 class="font-headline text-lg font-bold">课程目录</h3>
          <span class="text-xs text-outline font-medium">更新至 {{ course.totalChapters }} 节</span>
        </div>
        <div class="space-y-3">
          <div
            v-for="(chapter, idx) in course.chapters"
            :key="chapter.id"
            @click="chapter.unlocked ? goPlay(chapter) : null"
            :class="[
              'flex items-center gap-4 p-4 rounded-lg transition-all',
              idx === currentChapterIdx
                ? 'bg-primary/5 border border-primary/20'
                : chapter.unlocked
                  ? 'bg-surface-container-lowest cursor-pointer hover:bg-surface-container-low'
                  : 'bg-surface-container-low/50 opacity-60'
            ]"
          >
            <div :class="[
              'w-10 h-10 flex-shrink-0 flex items-center justify-center rounded-full',
              chapter.completed
                ? 'bg-tertiary-container'
                : idx === currentChapterIdx
                  ? 'bg-primary'
                  : 'bg-surface-container-highest'
            ]">
              <span :class="[
                'material-symbols-outlined',
                chapter.completed ? 'text-on-tertiary-container' : idx === currentChapterIdx ? 'text-white' : 'text-on-surface-variant text-sm'
              ]" :style="idx === currentChapterIdx ? `font-variation-settings: 'FILL' 1` : ''">
                {{ chapter.completed ? 'check' : idx === currentChapterIdx ? 'play_arrow' : chapter.unlocked ? 'play_circle' : 'lock' }}
              </span>
            </div>
            <div class="flex-grow">
              <p :class="['text-sm font-semibold', idx === currentChapterIdx ? 'text-primary font-bold' : 'text-on-surface']">
                {{ idx + 1 }}. {{ chapter.title }}
              </p>
              <p class="text-xs text-outline">{{ formatDuration(chapter.durationSec) }}</p>
            </div>
            <div v-if="chapter.completed" class="text-tertiary text-xs font-bold">已完成</div>
            <p v-else-if="idx === currentChapterIdx" class="text-[10px] text-primary font-bold">正在学习</p>
          </div>
        </div>
      </section>
    </main>

    <!-- Bottom Action Bar -->
    <footer v-if="course" class="fixed bottom-0 left-0 w-full px-6 py-4 flex justify-between items-center gap-4 max-w-xl mx-auto bg-white/80 backdrop-blur-xl rounded-t-[3rem] z-50 shadow-[0_-4px_24px_rgba(36,44,81,0.04)]">
      <div class="flex items-center gap-2 text-[#242c51] font-medium">
        <div class="relative w-10 h-10 flex items-center justify-center">
          <svg class="w-full h-full transform -rotate-90">
            <circle class="text-surface-container-highest" cx="20" cy="20" fill="transparent" r="16" stroke="currentColor" stroke-width="3"></circle>
            <circle class="text-[#3B82F6]" cx="20" cy="20" fill="transparent" r="16" stroke="currentColor"
              :stroke-dasharray="`${progressPct} ${100 - progressPct}`"
              stroke-dashoffset="0" stroke-width="3"></circle>
          </svg>
          <span class="absolute text-[10px] font-bold">{{ progressPct }}%</span>
        </div>
        <div class="flex flex-col">
          <span class="font-semibold text-sm">进度</span>
          <span class="text-[10px] text-outline">已完成 {{ completedCount }}/{{ course.totalChapters }} 节</span>
        </div>
      </div>
      <button
        @click="goPlay(currentChapter)"
        class="flex flex-row items-center justify-center bg-gradient-to-r from-[#0057bd] to-[#6e9fff] text-white rounded-full px-8 py-3 w-48 hover:opacity-90 transition-opacity active:scale-[0.98]"
      >
        <span class="material-symbols-outlined mr-2">play_arrow</span>
        <span class="font-semibold text-sm">继续学习</span>
      </button>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TopBar from '@/components/TopBar.vue'
import { getCourseDetail } from '@/api/course'

const route = useRoute()
const router = useRouter()
const course = ref(null)

const currentChapterIdx = computed(() => {
  if (!course.value) return 0
  return course.value.chapters?.findIndex(c => !c.completed) ?? 0
})

const currentChapter = computed(() => course.value?.chapters?.[currentChapterIdx.value])

const completedCount = computed(() => course.value?.chapters?.filter(c => c.completed).length || 0)

const progressPct = computed(() => {
  if (!course.value?.totalChapters) return 0
  return Math.round((completedCount.value / course.value.totalChapters) * 100)
})

function formatDuration(sec) {
  if (!sec) return '-'
  const m = Math.floor(sec / 60)
  const s = sec % 60
  return `${m}:${String(s).padStart(2, '0')}`
}

function goPlay(chapter) {
  if (!chapter) return
  router.push({ path: `/courses/${route.params.id}/play`, query: { chapter: chapter.id } })
}

onMounted(async () => {
  try {
    course.value = await getCourseDetail(route.params.id)
  } catch (e) {
    course.value = {
      id: route.params.id,
      title: '线性代数基础',
      category: '数学',
      difficulty: '中级',
      teacher: '王老师',
      description: '本课程系统讲解线性代数核心概念，涵盖向量空间、矩阵运算、行列式、特征值与特征向量等关键知识点。',
      totalChapters: 20,
      studentCount: 2340,
      chapters: [
        { id: 1, title: '向量与空间', durationSec: 900, completed: true, unlocked: true },
        { id: 2, title: '矩阵运算', durationSec: 1350, completed: false, unlocked: true },
        { id: 3, title: '行列式', durationSec: 1125, completed: false, unlocked: false },
        { id: 4, title: '特征值与特征向量', durationSec: 1500, completed: false, unlocked: false }
      ]
    }
  }
})
</script>
