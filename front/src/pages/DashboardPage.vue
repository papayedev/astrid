<template>
  <Layout :role="role" />
</template>

<script setup>
import { storeToRefs } from 'pinia'
import Layout from '@/layouts/Layout.vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()
const { role } = storeToRefs(userStore)
</script>
