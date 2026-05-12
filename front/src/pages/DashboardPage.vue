<template>
  <Layout :role="role">
    <div v-if="role === 'VISITOR'" class="container fluid">
      <VisitorComponent />
    </div>
    <div v-if="role === 'ADMIN'" class="container fluid">
      <AdminComponents />
    </div>
  </Layout>
</template>

<script setup>
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import Layout from '@/layouts/Layout.vue'
import AdminComponents from '@/components/AdminComponents.vue'
import router from '@/router/index.js'
import VisitorComponent from '@/components/VisitorComponent.vue'

const { checkAuth } = useUserStore()
const { role } = storeToRefs(useUserStore())

setInterval(async () => {
  const success = await checkAuth()
  if (!success) {
    router.push('/login')
  }
}, 5000)
</script>
