import http from './index'

export const getPosts = (type) =>
  http.get('/posts', { params: type ? { type } : {} })

export const createPost = (data) => http.post('/posts', data)

export const getPostDetail = (id) => http.get(`/posts/${id}`)

export const createComment = (id, content) =>
  http.post(`/posts/${id}/comments`, { content })

export const toggleLike = (id) => http.post(`/posts/${id}/like`)
