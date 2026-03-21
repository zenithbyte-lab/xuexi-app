import http from './index'

export const getMessages = () => http.get('/messages')
export const getUnreadCount = () => http.get('/messages/unread-count')
export const markAsRead = (id) => http.put(`/messages/${id}/read`)
