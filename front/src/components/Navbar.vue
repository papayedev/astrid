<template>
  <div class="menu">
    <nav>
      <ul>
        <li><strong>{{ role }} - Panel</strong></li>
      </ul>
      <ul v-if="role === 'VISITOR' || role === 'ADMIN'">
        <li><button @click="logoutAction">Déconnexion</button></li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import {useUserStore} from "@/stores/userStore.js";
import router from "@/router/index.js";

defineProps(['role'])

const { logout } = useUserStore()

const logoutAction = () => {
  logout()
  router.push('/login')
}
</script>
