<template>
  <div class="bg-surface font-body text-on-surface min-h-screen flex flex-col">
    <!-- TopAppBar -->
    <header class="w-full sticky top-0 z-40 bg-[#e4e7ff] flex items-center justify-between px-6 py-4">
      <div class="flex items-center gap-3">
        <button @click="router.back()" class="active:scale-95 duration-150 text-blue-600">
          <span class="material-symbols-outlined">arrow_back</span>
        </button>
        <h1 class="font-headline font-bold text-lg text-on-surface">每日练习</h1>
      </div>
      <div class="text-blue-600 font-extrabold font-headline">{{ currentIdx + 1 }}/{{ questions.length }}</div>
    </header>

    <main class="flex-1 px-6 pt-4 pb-32">
      <!-- Progress Bar -->
      <div class="mb-8">
        <div class="flex justify-between items-end mb-2">
          <span class="text-sm font-semibold text-on-surface-variant">总进度</span>
          <span class="text-lg font-bold text-primary">{{ progress }}%</span>
        </div>
        <div class="h-3 w-full bg-surface-container-highest rounded-full overflow-hidden">
          <div class="h-full bg-gradient-to-r from-primary to-primary-container rounded-full transition-all duration-500" :style="`width: ${progress}%`"></div>
        </div>
      </div>

      <!-- Question Card -->
      <div v-if="currentQuestion" class="bg-surface-container-lowest rounded-xl shadow-[0_4px_24px_rgba(36,44,81,0.04)] p-6 relative overflow-hidden">
        <div class="absolute top-0 right-0 w-32 h-32 bg-primary/5 rounded-full -mr-16 -mt-16 blur-3xl"></div>
        <div class="flex justify-between items-center mb-6">
          <div class="flex items-center gap-2 bg-surface-container-low px-4 py-2 rounded-full">
            <span class="font-headline font-bold text-on-surface text-sm">{{ currentQuestion.subject }}</span>
          </div>
          <div class="flex gap-2">
            <span class="bg-secondary-container text-on-secondary-container text-[10px] font-bold px-3 py-1 rounded-sm uppercase tracking-wider">{{ currentQuestion.subject }}</span>
            <span class="bg-surface-container text-on-surface-variant text-[10px] font-bold px-3 py-1 rounded-sm uppercase tracking-wider">{{ currentQuestion.difficulty }}</span>
          </div>
        </div>

        <div class="mb-8">
          <p class="text-xl font-bold leading-relaxed text-on-surface font-headline">{{ currentQuestion.content }}</p>
        </div>

        <div class="space-y-3">
          <button
            v-for="(option, key) in currentQuestion.options"
            :key="key"
            @click="selectOption(key)"
            :disabled="showFeedback"
            :class="[
              'w-full flex items-center gap-4 p-4 rounded-lg border-2 transition-colors',
              showFeedback && key === currentQuestion.answer
                ? 'border-green-500 bg-green-100'
                : showFeedback && key === selectedOption && !selectedCorrect
                  ? 'border-red-500 bg-red-100'
                  : selectedOption === key && !showFeedback
                    ? 'border-primary bg-primary-container/10'
                    : 'border-transparent bg-surface-container-low hover:bg-surface-container'
            ]"
          >
            <div :class="[
              'w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm',
              showFeedback && key === currentQuestion.answer
                ? 'bg-green-500 text-white'
                : showFeedback && key === selectedOption && !selectedCorrect
                  ? 'bg-red-500 text-white'
                  : selectedOption === key && !showFeedback
                    ? 'bg-primary text-on-primary'
                    : 'bg-surface-container-highest text-on-surface-variant'
            ]">{{ key }}</div>
            <span :class="[
              'font-medium',
              showFeedback && key === currentQuestion.answer
                ? 'font-bold text-green-700'
                : showFeedback && key === selectedOption && !selectedCorrect
                  ? 'font-bold text-red-700'
                  : selectedOption === key && !showFeedback
                    ? 'font-bold text-on-primary-container'
                    : 'text-on-surface'
            ]">
              {{ option }}
            </span>
            <div v-if="showFeedback && key === currentQuestion.answer" class="ml-auto bg-green-500 rounded-full p-1">
              <span class="material-symbols-outlined text-white text-[16px] font-bold">check</span>
            </div>
            <div v-else-if="showFeedback && key === selectedOption && !selectedCorrect" class="ml-auto bg-red-500 rounded-full p-1">
              <span class="material-symbols-outlined text-white text-[16px] font-bold">close</span>
            </div>
            <div v-else-if="selectedOption === key && !showFeedback" class="ml-auto bg-primary rounded-full p-1">
              <span class="material-symbols-outlined text-white text-[16px] font-bold">check</span>
            </div>
          </button>
        </div>

        <!-- Feedback text -->
        <div v-if="showFeedback" class="mt-4 p-4 rounded-lg text-center font-bold font-headline"
          :class="selectedCorrect ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-red-50 text-red-700 border border-red-200'">
          {{ selectedCorrect ? '回答正确！' : `回答错误，正确答案是 ${currentQuestion.answer}` }}
        </div>
      </div>

      <!-- Navigation -->
      <div class="mt-8 flex gap-4">
        <button
          @click="prevQuestion"
          :disabled="currentIdx === 0"
          class="flex-1 py-4 px-6 rounded-full border-2 border-primary text-primary font-headline font-bold active:scale-95 transition-all disabled:opacity-40 flex items-center justify-center gap-2"
        >上一题</button>
        <button
          @click="nextQuestion"
          class="flex-1 py-4 px-6 rounded-full bg-primary text-on-primary font-headline font-bold shadow-lg shadow-primary/20 active:scale-95 transition-all flex items-center justify-center gap-2"
        >
          {{ currentIdx < questions.length - 1 ? '下一题' : '提交' }}
          <span class="material-symbols-outlined">arrow_forward</span>
        </button>
      </div>
    </main>

    <BottomNav />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BottomNav from '@/components/BottomNav.vue'
import { getTodayPractice, submitPractice } from '@/api/practice'

const router = useRouter()
const questions = ref([])
const currentIdx = ref(0)
const answers = ref({})
const showFeedback = ref(false)
const selectedCorrect = ref(false)
const correctCount = ref(0)

const currentQuestion = computed(() => questions.value[currentIdx.value])
const selectedOption = computed(() => answers.value[currentIdx.value])
const progress = computed(() => Math.round(((currentIdx.value + 1) / (questions.value.length || 10)) * 100))

function selectOption(key) {
  if (showFeedback.value) return
  answers.value[currentIdx.value] = key
  selectedCorrect.value = key === currentQuestion.value.answer
  if (selectedCorrect.value) correctCount.value++
  showFeedback.value = true

  setTimeout(() => {
    showFeedback.value = false
    if (currentIdx.value < questions.value.length - 1) {
      currentIdx.value++
    }
    // On last question, stay so user can click submit
  }, 1000)
}

function prevQuestion() {
  if (showFeedback.value) return
  if (currentIdx.value > 0) {
    // If going back, undo correctCount if that answer was correct
    const prevIdx = currentIdx.value - 1
    if (answers.value[prevIdx] === questions.value[prevIdx].answer) {
      correctCount.value--
    }
    delete answers.value[prevIdx]
    currentIdx.value--
  }
}

async function nextQuestion() {
  if (showFeedback.value) return
  if (currentIdx.value < questions.value.length - 1) {
    currentIdx.value++
  } else {
    const total = questions.value.length
    const score = Math.round((correctCount.value / total) * 100)
    try {
      const result = await submitPractice(answers.value)
      router.push({ path: '/practice/result', state: { result: { ...result, correctCount: correctCount.value } } })
    } catch (e) {
      router.push({ path: '/practice/result', state: { result: { score, correctCount: correctCount.value, total } } })
    }
  }
}

onMounted(async () => {
  try {
    questions.value = await getTodayPractice()
  } catch (e) {
    questions.value = [
      { id: 1, subject: '数学', difficulty: '中等', content: '如果 a = 3，b = 4，求 a² + b² 的值？', options: { A: '20', B: '25', C: '25', D: '30' }, answer: 'B' },
      { id: 2, subject: '英语', difficulty: '简单', content: 'What is the past tense of "go"?', options: { A: 'goed', B: 'went', C: 'gone', D: 'going' }, answer: 'B' },
      { id: 3, subject: '数学', difficulty: '困难', content: '矩阵 A 的行列式 det(A) = 0，则矩阵 A 的秩为？', options: { A: '0', B: '1', C: '不满秩', D: '无法确定' }, answer: 'C' }
    ]
  }
})
</script>
