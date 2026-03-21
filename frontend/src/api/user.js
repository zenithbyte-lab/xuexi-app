import http from './index'

export const getProfile = () => http.get('/user/profile')

export const updateProfile = (data) => http.put('/user/profile', data)
