import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

const API_URL = 'http://localhost:8080'

export const useUserStore = defineStore('userStore', () => {
  const hasError = ref(false)
  const hasMessage = ref('')

  const accessToken = ref(localStorage.getItem('accessToken'))
  const refreshToken = ref(localStorage.getItem('refreshToken'))

  const email = ref('')
  const role = ref('')

  const isAuthenticated = computed(() => !!accessToken.value)

  const jsonHeaders = {
    'Content-Type': 'application/json',
  }

  const setError = (message) => {
    hasError.value = true
    hasMessage.value = message
  }

  const clearError = () => {
    hasError.value = false
    hasMessage.value = ''
  }

  const logout = async () => {
    accessToken.value = null
    refreshToken.value = null

    email.value = ''
    role.value = ''

    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
  }

  const getUser = async () => {
    try {
      const res = await fetch(`${API_URL}/users`, {
        method: 'GET',
        headers: {
          ...jsonHeaders,
          Authorization: `Bearer ${accessToken.value}`,
        },
      })

      if (!res.ok) return false

      const data = await res.json()

      email.value = data.emailAddress
      role.value = data.role

      return true
    } catch {
      return false
    }
  }

  const refresh = async () => {
    try {
      if (!refreshToken.value) {
        await logout()
        return false
      }

      const res = await fetch(`${API_URL}/auth/refresh`, {
        method: 'POST',
        headers: jsonHeaders,
        body: JSON.stringify({
          refreshToken: refreshToken.value,
        }),
      })

      if (!res.ok) {
        await logout()
        return false
      }

      const data = await res.json()

      accessToken.value = data.accessToken
      localStorage.setItem('accessToken', data.accessToken)

      return true
    } catch {
      await logout()
      return false
    }
  }

  const checkAuth = async () => {
    clearError()

    try {
      if (!accessToken.value) return false

      const userOk = await getUser()
      if (userOk) return true

      const refreshed = await refresh()
      if (!refreshed) return false

      return await getUser()
    } catch {
      await logout()
      return false
    }
  }

  const register = async (email, password) => {
    clearError()

    try {
      const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: jsonHeaders,
        body: JSON.stringify({ email, password }),
      })

      if (!res.ok) {
        setError('Une de vos informations est incorrecte')
        return false
      }

      return true
    } catch {
      setError('Une erreur est survenue')
      return false
    }
  }

  const verification = async (email, code) => {
    clearError()

    try {
      const res = await fetch(`${API_URL}/auth/activate`, {
        method: 'POST',
        headers: jsonHeaders,
        body: JSON.stringify({
          email,
          verificationCode: code,
        }),
      })

      if (!res.ok) {
        setError('Code de vérification invalide')
        return false
      }

      return true
    } catch {
      setError('Une erreur est survenue')
      return false
    }
  }

  const login = async (email, password) => {
    clearError()

    try {
      const res = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: jsonHeaders,
        body: JSON.stringify({ email, password }),
      })

      if (res.status === 403) return 403

      if (!res.ok) {
        setError('Email ou mot de passe incorrect')
        return 500
      }

      const data = await res.json()

      accessToken.value = data.accessToken
      refreshToken.value = data.refreshToken

      localStorage.setItem('accessToken', data.accessToken)
      localStorage.setItem('refreshToken', data.refreshToken)

      return 200
    } catch {
      setError('Une erreur est survenue')
      return 500
    }
  }

  return {
    hasError,
    hasMessage,

    accessToken,
    refreshToken,

    email,
    role,

    isAuthenticated,

    getUser,
    checkAuth,
    register,
    verification,
    login,
    logout,
    refresh,
  }
})
