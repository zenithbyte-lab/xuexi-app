import http from './index'

export const getCourses = (category) =>
  http.get('/courses', { params: category ? { category } : {} })

export const getCourseDetail = (id) => http.get(`/courses/${id}`)

export const updateProgress = (id, data) =>
  http.put(`/courses/${id}/progress`, data)
