import http from './index'

export const getTodayPractice = () => http.get('/practice/today')

export const submitPractice = (answers) =>
  http.post('/practice/submit', { answers })

export const getLeaderboard = () => http.get('/practice/leaderboard')
