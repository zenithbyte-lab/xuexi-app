import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  { path: '/login', name: 'login', component: () => import('@/views/LoginView.vue'), meta: { guest: true } },
  { path: '/register', name: 'register', component: () => import('@/views/RegisterView.vue'), meta: { guest: true } },
  { path: '/', name: 'home', component: () => import('@/views/HomeView.vue'), meta: { auth: true } },
  { path: '/courses', name: 'courses', component: () => import('@/views/CourseListView.vue'), meta: { auth: true } },
  { path: '/courses/:id', name: 'courseDetail', component: () => import('@/views/CourseDetailView.vue'), meta: { auth: true } },
  { path: '/courses/:id/play', name: 'videoPlayer', component: () => import('@/views/VideoPlayerView.vue'), meta: { auth: true } },
  { path: '/community', name: 'community', component: () => import('@/views/CommunityView.vue'), meta: { auth: true } },
  { path: '/community/post', name: 'postCreate', component: () => import('@/views/PostCreateView.vue'), meta: { auth: true } },
  { path: '/messages', name: 'messages', component: () => import('@/views/MessageListView.vue'), meta: { auth: true } },
  { path: '/practice', name: 'practice', component: () => import('@/views/PracticeView.vue'), meta: { auth: true } },
  { path: '/practice/result', name: 'practiceResult', component: () => import('@/views/PracticeResultView.vue'), meta: { auth: true } },
  { path: '/profile', name: 'profile', component: () => import('@/views/ProfileView.vue'), meta: { auth: true } },
  { path: '/checkin/success', name: 'checkinSuccess', component: () => import('@/views/CheckinSuccessView.vue'), meta: { auth: true } }
]

const router = createRouter({
  history: createWebHistory('/'),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) {
    next('/login')
  } else if (to.meta.guest && token) {
    next('/')
  } else {
    next()
  }
})

export default router
