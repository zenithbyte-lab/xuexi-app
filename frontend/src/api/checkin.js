import http from './index'

export const doCheckin = () => http.post('/checkin')

export const getCheckinStats = () => http.get('/checkin/stats')
