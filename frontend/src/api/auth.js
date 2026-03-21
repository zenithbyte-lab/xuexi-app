import http from './index'

export const login = (phone, password) =>
  http.post('/auth/login', { phone, password })

export const register = (phone, password, nickname) =>
  http.post('/auth/register', { phone, password, nickname })
