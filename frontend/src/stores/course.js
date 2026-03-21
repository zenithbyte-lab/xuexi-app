import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCourses, getCourseDetail } from '@/api/course'

export const useCourseStore = defineStore('course', () => {
  const courses = ref([])
  const currentCourse = ref(null)
  const activeCategory = ref('全部')

  async function fetchCourses(category) {
    const data = await getCourses(category === '全部' ? null : category)
    courses.value = data
    return data
  }

  async function fetchCourseDetail(id) {
    const data = await getCourseDetail(id)
    currentCourse.value = data
    return data
  }

  return { courses, currentCourse, activeCategory, fetchCourses, fetchCourseDetail }
})
