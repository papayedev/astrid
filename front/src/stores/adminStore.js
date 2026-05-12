import { defineStore } from 'pinia'
import { ref } from 'vue'

const API_URL = 'http://localhost:8080/admin'

export const useAdminStore = defineStore('adminStore', () => {
  const hasError = ref(false)
  const hasMessage = ref('')
  const devices = ref([])
  const maxPage = ref(0)

  const getAllDevices = async (page) => {
    const res = await fetch(`${API_URL}/devices?page=${page}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    })
    if (res.ok) {
      const data = await res.json()
      console.log(data)
      devices.value = data.devices
      maxPage.value = data.maxPage
    }
  }

  const deleteDevice = async (deviceId) => {
    hasError.value = true
    hasMessage.value = ''

    const res = await fetch(`${API_URL}/devices/${deviceId}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    })

    if (!res.ok) {
      const data = await res.json()
      hasError.value = true
      hasMessage.value = data.message
    }

    return true
  }

  const createDevice = async (name, serialNumber, pin) => {
    hasError.value = false
    hasMessage.value = ''

    const res = await fetch(`${API_URL}/devices`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
      body: JSON.stringify({
        name,
        serialNumber,
        pin,
      }),
    })

    const data = await res.json()

    if (!res.ok) {
      hasError.value = true
      hasMessage.value = data.message
      return false
    }

    return true
  }

  return {
    createDevice,
    getAllDevices,
    deleteDevice,

    maxPage,
    devices,
    hasError,
    hasMessage,
  }
})
