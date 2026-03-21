import { defineStore } from 'pinia'
import { ref } from 'vue'
import { doCheckin, getCheckinStats } from '@/api/checkin'

export const useCheckinStore = defineStore('checkin', () => {
  const stats = ref({ streakDays: 0, checkedToday: false, monthCalendar: [] })

  async function checkin() {
    const data = await doCheckin()
    stats.value.checkedToday = true
    stats.value.streakDays = data.streakDays
    return data
  }

  async function fetchStats() {
    const data = await getCheckinStats()
    stats.value = data
    return data
  }

  return { stats, checkin, fetchStats }
})
