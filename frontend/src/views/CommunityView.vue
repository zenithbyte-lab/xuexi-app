<template>
  <div class="bg-surface text-on-surface min-h-screen">
    <header class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] z-50 flex items-center justify-between px-6 h-16 bg-[#f7f5ff]/80 backdrop-blur-xl">
      <button class="material-symbols-outlined text-on-surface p-2 rounded-full hover:bg-surface-container transition-colors">search</button>
      <h1 class="font-headline font-bold text-lg text-on-surface">社群</h1>
      <div class="flex items-center gap-2">
        <button class="material-symbols-outlined text-on-surface p-2 rounded-full hover:bg-surface-container transition-colors">notifications</button>
        <RouterLink to="/community/post">
          <button class="flex items-center justify-center bg-primary text-on-primary rounded-full p-2 active:scale-95 transition-transform shadow-md shadow-primary/20">
            <span class="material-symbols-outlined text-[20px]">add</span>
          </button>
        </RouterLink>
      </div>
    </header>

    <main class="pt-16 pb-24 px-4">
      <!-- Category Tabs -->
      <div class="flex items-center gap-3 overflow-x-auto no-scrollbar py-4 -mx-4 px-4" style="touch-action:pan-x">
        <button
          v-for="cat in categories" :key="cat.value"
          @click="activeType = cat.value"
          :class="['whitespace-nowrap px-5 py-2.5 rounded-full font-headline font-bold text-sm transition-colors flex-shrink-0',
            activeType === cat.value ? 'bg-primary text-on-primary shadow-lg shadow-primary/20' : 'bg-surface-container text-on-surface-variant']"
        >{{ cat.label }}</button>
      </div>

      <!-- Feed -->
      <div v-if="loading" class="flex justify-center py-12">
        <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
      </div>
      <div v-else class="space-y-5">
        <article v-for="post in posts" :key="post.id" class="bg-surface-container-lowest rounded-xl p-5 shadow-sm">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center gap-3">
              <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-primary to-secondary flex items-center justify-center text-on-primary font-bold text-lg flex-shrink-0">
                {{ post.nickname?.[0] || '匿' }}
              </div>
              <div>
                <h3 class="font-headline font-bold text-[15px]">{{ post.nickname }}</h3>
                <p class="text-[12px] text-on-surface-variant">{{ post.timeAgo }}</p>
              </div>
            </div>
            <div :class="['flex items-center px-3 py-1 rounded-full text-[11px] font-bold',
              isCheckin(post.type) ? 'bg-tertiary-container/30 text-on-tertiary-container' : 'bg-secondary-container/30 text-on-secondary-container']">
              <span v-if="isCheckin(post.type)" class="material-symbols-outlined text-tertiary text-[14px] mr-1" style="font-variation-settings:'FILL' 1">check_circle</span>
              {{ postTypeLabel(post.type) }}
            </div>
          </div>
          <p class="text-on-surface leading-relaxed mb-4">{{ post.content }}</p>
          <div v-if="post.imageUrl" class="w-full aspect-video rounded-lg overflow-hidden mb-4">
            <img :src="post.imageUrl" class="w-full h-full object-cover" />
          </div>
          <div class="flex items-center justify-between pt-3 border-t border-surface-container">
            <div class="flex items-center gap-5">
              <button @click="toggleLikePost(post)" class="flex items-center gap-1.5 text-on-surface-variant active:scale-90 transition-transform">
                <span class="material-symbols-outlined text-[20px]"
                  :style="post.liked ? `font-variation-settings:'FILL' 1` : ''"
                  :class="post.liked ? 'text-error' : ''">favorite</span>
                <span class="text-[13px] font-medium">{{ post.likeCount }}</span>
              </button>
              <button @click="openComments(post)" class="flex items-center gap-1.5 text-on-surface-variant active:scale-90 transition-transform">
                <span class="material-symbols-outlined text-[20px]">chat_bubble</span>
                <span class="text-[13px] font-medium">{{ post.commentCount }}</span>
              </button>
            </div>
            <button @click="sharePost(post)" class="text-on-surface-variant active:scale-90 transition-transform">
              <span class="material-symbols-outlined text-[20px]">share</span>
            </button>
          </div>
        </article>
        <div v-if="!loading && posts.length === 0" class="text-center py-16 text-on-surface-variant">
          <span class="material-symbols-outlined text-5xl block mb-3">forum</span>
          <p>暂无帖子</p>
        </div>
      </div>
    </main>

    <!-- Comment Bottom Sheet -->
    <Teleport to="body">
      <div v-if="commentPostId !== null" class="fixed inset-0 z-[200] flex flex-col justify-end" style="max-width:390px;left:50%;transform:translateX(-50%)">
        <div class="absolute inset-0 bg-black/40" @click="closeComments"></div>
        <div class="relative bg-surface rounded-t-2xl flex flex-col" style="max-height:70vh">
          <div class="flex items-center justify-between px-6 py-4 border-b border-surface-container flex-shrink-0">
            <h3 class="font-headline font-bold text-base">评论 ({{ activePost?.commentCount || 0 }})</h3>
            <button @click="closeComments" class="material-symbols-outlined text-on-surface-variant">close</button>
          </div>
          <div class="flex-1 overflow-y-auto px-4 py-4 space-y-4" style="min-height:80px">
            <div v-if="commentsLoading" class="flex justify-center py-8">
              <div class="w-6 h-6 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
            <div v-else-if="comments.length === 0" class="text-center py-8 text-on-surface-variant text-sm">暂无评论，来抢沙发吧！</div>
            <div v-for="c in comments" :key="c.id" class="flex gap-3">
              <div class="w-9 h-9 rounded-full bg-gradient-to-br from-secondary to-primary flex items-center justify-center text-on-primary font-bold text-sm flex-shrink-0">
                {{ c.nickname?.[0] || '匿' }}
              </div>
              <div>
                <div class="flex items-baseline gap-2">
                  <span class="font-bold text-sm">{{ c.nickname }}</span>
                  <span class="text-[11px] text-on-surface-variant">{{ c.timeAgo }}</span>
                </div>
                <p class="text-sm mt-0.5">{{ c.content }}</p>
              </div>
            </div>
          </div>
          <div class="px-4 py-3 border-t border-surface-container flex items-center gap-3 flex-shrink-0">
            <input v-model="commentInput" @keyup.enter="submitComment" placeholder="写下你的评论..."
              class="flex-1 bg-surface-container rounded-full px-4 py-2.5 text-sm outline-none placeholder:text-on-surface-variant" />
            <button @click="submitComment" :disabled="!commentInput.trim()"
              class="w-9 h-9 bg-primary text-on-primary rounded-full flex items-center justify-center disabled:opacity-40 active:scale-95 transition-transform flex-shrink-0">
              <span class="material-symbols-outlined text-[18px]">send</span>
            </button>
          </div>
        </div>
      </div>
    </Teleport>

    <BottomNav />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import BottomNav from '@/components/BottomNav.vue'
import { getPosts, toggleLike, createComment } from '@/api/community'
import http from '@/api/index'

const categories = [
  { label: '推荐', value: '' },
  { label: '学习打卡', value: '打卡' },
  { label: '提问', value: '提问' },
  { label: '经验分享', value: '分享' }
]
const activeType = ref('')
const posts = ref([])
const loading = ref(false)
const commentPostId = ref(null)
const activePost = ref(null)
const comments = ref([])
const commentsLoading = ref(false)
const commentInput = ref('')

const isCheckin = (t) => t === '打卡' || t === 'checkin'

function postTypeLabel(type) {
  return { checkin: '已打卡', 打卡: '已打卡', question: '提问', 提问: '提问', share: '经验分享', 分享: '经验分享' }[type] || '分享'
}

async function toggleLikePost(post) {
  post.liked = !post.liked
  post.likeCount += post.liked ? 1 : -1
  try { await toggleLike(post.id) } catch {
    post.liked = !post.liked
    post.likeCount += post.liked ? 1 : -1
  }
}

async function openComments(post) {
  activePost.value = post
  commentPostId.value = post.id
  commentsLoading.value = true
  comments.value = []
  try {
    comments.value = await http.get(`/posts/${post.id}/comments`)
  } catch { comments.value = [] }
  finally { commentsLoading.value = false }
}

function closeComments() {
  commentPostId.value = null; activePost.value = null
  commentInput.value = ''; comments.value = []
}

async function submitComment() {
  const text = commentInput.value.trim()
  if (!text) return
  try {
    const c = await createComment(commentPostId.value, text)
    comments.value.push(c)
    if (activePost.value) activePost.value.commentCount++
    const p = posts.value.find(p => p.id === commentPostId.value)
    if (p) p.commentCount++
    commentInput.value = ''
  } catch { alert('评论失败，请重试') }
}

async function sharePost(post) {
  const text = `${post.nickname}: ${post.content}`
  if (navigator.share) { await navigator.share({ title: '学学App', text }).catch(() => {}) }
  else { await navigator.clipboard.writeText(text).catch(() => {}); alert('内容已复制到剪贴板') }
}

async function loadPosts() {
  loading.value = true
  try {
    posts.value = await getPosts(activeType.value)
  } catch {
    posts.value = [
      { id: 1, nickname: '学习达人小李', timeAgo: '2小时前', type: '打卡', content: '今天完成了线性代数第三章的学习，感觉收获很大！重点攻克了矩阵逆变换的几何意义。', likeCount: 12, commentCount: 3, liked: false },
      { id: 2, nickname: '设计喵酱', timeAgo: '4小时前', type: '分享', content: '分享一些关于配色系统的笔记，如何利用对比色提升界面的高级感。大家最近都在学什么？', likeCount: 28, commentCount: 7, liked: false },
      { id: 3, nickname: '码农小张', timeAgo: '2天前', type: '提问', content: 'Python中的装饰器到底怎么理解？感觉一直没搞明白，求大佬解答', likeCount: 5, commentCount: 4, liked: false }
    ]
  } finally { loading.value = false }
}

watch(activeType, loadPosts)
onMounted(loadPosts)
</script>
