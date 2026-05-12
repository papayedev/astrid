import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'

const API_URL = 'http://localhost:8080/visitors'

export const useVisitorStore = defineStore('visitorStore', () => {
  const device = reactive({
    id: '',
    name: '',
    serialNumber: '',
  })

  const hasError = ref(false)
  const hasMessage = ref('')

  const linkDevice = async (name, serialNumber, pin) => {
    hasError.value = false
    hasMessage.value = ''

    const res = await fetch(`${API_URL}/devices/link`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
      body: JSON.stringify({ name, serialNumber, pin }),
    })

    if (!res.ok) {
      const data = await res.json()

      hasError.value = true
      hasMessage.value = data.message

      return false
    }

    return true
  }

  const getDevice = async () => {
    const res = await fetch(`${API_URL}/devices`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    })

    if (!res.ok) {
      return false
    }

    const data = await res.json()

    device.id = data.id
    device.name = data.name
    device.serialNumber = data.serialNumber

    return true
  }

  return {
    device,
    hasError,
    hasMessage,

    linkDevice,
    getDevice,
  }
})
