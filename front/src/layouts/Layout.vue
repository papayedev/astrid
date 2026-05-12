<template>
  <Navbar :role="role" />
  <slot></slot>
</template>

<script setup>
import Navbar from '@/components/Navbar.vue'
defineProps({
  role: {
    type: String,
    default: '',
  },
})
</script>

<style scoped>
.menu {
  margin-left: 1rem;
  margin-right: 1rem;
}
</style>
