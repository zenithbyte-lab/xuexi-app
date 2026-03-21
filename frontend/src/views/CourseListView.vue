<template>
  <div class="bg-surface min-h-screen pb-32">
    <!-- TopAppBar -->
    <header class="bg-[#f7f5ff] rounded-b-[3rem] sticky top-0 z-40 shadow-[0_24px_24px_rgba(36,44,81,0.04)] flex items-center justify-between px-6 py-4 w-full">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-primary-container flex items-center justify-center overflow-hidden">
          <div class="w-full h-full bg-primary flex items-center justify-center text-on-primary font-bold">学</div>
        </div>
        <span class="font-headline font-bold text-2xl text-[#3B82F6] tracking-tight">卓越学者</span>
      </div>
      <button class="w-10 h-10 flex items-center justify-center rounded-full hover:bg-white/50 active:scale-95 transition-all">
        <span class="material-symbols-outlined text-[#242c51]">search</span>
      </button>
    </header>

    <main class="px-6 pt-6 space-y-8">
      <!-- Search & Filter -->
      <section class="flex items-center gap-3">
        <div class="flex-1 bg-surface-container-low rounded-full px-5 py-3.5 flex items-center gap-3 focus-within:bg-surface-container-lowest focus-within:ring-1 focus-within:ring-primary/20 transition-all">
          <span class="material-symbols-outlined text-outline">search</span>
          <input
            v-model="searchQuery"
            class="bg-transparent border-none focus:ring-0 w-full text-on-surface placeholder:text-outline/60 font-medium outline-none"
            placeholder="搜索课程..."
            type="text"
          />
        </div>
        <button class="w-[52px] h-[52px] bg-primary text-on-primary rounded-full flex items-center justify-center shadow-lg active:scale-95 transition-all">
          <span class="material-symbols-outlined">tune</span>
        </button>
      </section>

      <!-- Category Tabs -->
      <section class="overflow-x-auto no-scrollbar -mx-6 px-6">
        <div class="flex items-center gap-2">
          <button
            v-for="cat in categories"
            :key="cat"
            @click="activeCategory = cat"
            :class="[
              'px-6 py-2.5 rounded-full font-semibold text-sm whitespace-nowrap transition-colors',
              activeCategory === cat
                ? 'bg-primary text-on-primary shadow-md'
                : 'bg-surface-container text-on-surface-variant hover:bg-surface-container-high'
            ]"
          >{{ cat }}</button>
        </div>
      </section>

      <!-- Featured Banner -->
      <section class="relative group">
        <div class="relative w-full h-56 rounded-xl overflow-hidden shadow-xl bg-gradient-to-br from-primary to-primary-container">
          <div class="absolute inset-0 flex flex-col justify-center px-8 text-on-primary">
            <span class="text-xs font-bold uppercase tracking-widest opacity-80 mb-2">精选推荐</span>
            <h2 class="text-2xl font-extrabold max-w-[200px] leading-tight mb-4">精通现代UI设计</h2>
            <RouterLink to="/courses/1">
              <button class="w-fit px-6 py-2.5 bg-surface-container-lowest text-primary rounded-full font-bold text-sm shadow-lg active:scale-95 transition-all">
                开始学习
              </button>
            </RouterLink>
          </div>
        </div>
      </section>

      <!-- Course Grid -->
      <section>
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-xl font-bold text-on-surface tracking-tight">热门课程</h3>
          <button class="text-primary font-bold text-sm">查看全部</button>
        </div>
        <div v-if="loading" class="flex justify-center py-12">
          <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
        </div>
        <div v-else class="grid grid-cols-2 gap-4">
          <RouterLink
            v-for="course in filteredCourses"
            :key="course.id"
            :to="`/courses/${course.id}`"
          >
            <div class="bg-surface-container-lowest p-3 rounded-lg shadow-[0_8px_24px_rgba(36,44,81,0.04)] group">
              <div class="aspect-square rounded-xl overflow-hidden mb-3 relative bg-surface-container-high flex items-center justify-center">
                <span class="material-symbols-outlined text-4xl text-primary">{{ getCourseIcon(course.category) }}</span>
                <div class="absolute top-2 right-2 bg-surface-container-lowest/90 backdrop-blur-md px-2 py-1 rounded-lg flex items-center gap-1">
                  <span class="material-symbols-outlined text-[14px] text-yellow-500" style="font-variation-settings: 'FILL' 1">star</span>
                  <span class="text-[10px] font-bold">4.8</span>
                </div>
              </div>
              <h4 class="font-bold text-sm leading-tight text-on-surface mb-1 group-hover:text-primary transition-colors">{{ course.title }}</h4>
              <p class="text-[11px] text-outline mb-3">{{ course.teacher }}</p>
              <div class="space-y-1.5">
                <div class="flex justify-between items-center text-[10px] font-bold text-outline">
                  <span>已完成 {{ course.progress || 0 }}%</span>
                </div>
                <div class="h-1.5 w-full bg-surface-container-highest rounded-full overflow-hidden">
                  <div class="h-full bg-tertiary rounded-full" :style="`width: ${course.progress || 0}%`"></div>
                </div>
              </div>
            </div>
          </RouterLink>
        </div>
      </section>
    </main>

    <BottomNav />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import BottomNav from '@/components/BottomNav.vue'
import { getCourses } from '@/api/course'

const categories = ['全部', '数学', '英语', '设计', '编程']
const activeCategory = ref('全部')
const searchQuery = ref('')
const courses = ref([])
const loading = ref(false)

const filteredCourses = computed(() => {
  let list = courses.value
  if (searchQuery.value) {
    list = list.filter(c => c.title.includes(searchQuery.value) || c.teacher.includes(searchQuery.value))
  }
  if (activeCategory.value !== '全部') {
    list = list.filter(c => c.category === activeCategory.value)
  }
  return list
})

function getCourseIcon(category) {
  const map = { '数学': 'functions', '英语': 'translate', '设计': 'palette', '编程': 'code' }
  return map[category] || 'menu_book'
}

onMounted(async () => {
  loading.value = true
  try {
    courses.value = await getCourses()
  } catch (e) {
    // fallback mock data
    courses.value = [
      { id: 1, title: '高等代数', teacher: '王老师', category: '数学', progress: 45 },
      { id: 2, title: 'UI设计基础', teacher: '李老师', category: '设计', progress: 82 },
      { id: 3, title: '全民Python', teacher: '张老师', category: '编程', progress: 15 },
      { id: 4, title: '英语诗歌', teacher: '陈老师', category: '英语', progress: 0 }
    ]
  } finally {
    loading.value = false
  }
})
</script>
