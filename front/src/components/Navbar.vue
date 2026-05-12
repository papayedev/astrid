<template>
  <div class="menu">
    <nav>
      <ul>
        <Logo width="15%" height="15%" />
      </ul>
      <ul>
        <li><button class="red" @click="logoutAction">Déconnexion</button></li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore.js'
import router from '@/router/index.js'
import Logo from '@/components/Logo.vue'

defineProps(['role'])

const { logout } = useUserStore()

const logoutAction = () => {
  logout()
  router.push('/login')
}
</script>

<style>
.red {
  background-color: red;
  border-color: red;
}
</style>
