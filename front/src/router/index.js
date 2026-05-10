import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '@/pages/RegisterPage.vue'
import VerificationPage from '@/pages/VerificationPage.vue'
import DashboardPage from '@/pages/DashboardPage.vue'
import ForgotPasswordPage from '@/pages/ForgotPasswordPage.vue'
import ResetPasswordPage from '@/pages/ResetPasswordPage.vue'

import { useUserStore } from '@/stores/userStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: LoginPage },
    { path: '/register', component: RegisterPage },
    { path: '/verification', component: VerificationPage },

    {
      path: '/dashboard',
      component: DashboardPage,
      meta: { requiresAuth: true },
    },

    { path: '/forgot/password', component: ForgotPasswordPage },
    { path: '/reset/password', component: ResetPasswordPage },
  ],
})

router.beforeEach(async (to) => {
  const userStore = useUserStore()

  const isAuth = await userStore.checkAuth()

  if (to.meta.requiresAuth && !isAuth) {
    return '/login'
  }

  if (to.path === '/login' && isAuth) {
    return '/dashboard'
  }
})

export default router
