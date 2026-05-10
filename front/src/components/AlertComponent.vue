<template>
  <div :class="alertTypeClass">
    <slot></slot>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps(['alertType'])

const alertTypeClass = computed(() => {
  switch (props.alertType) {
    case 'warning':
      return 'alert-warning'
    case 'success':
      return 'alert-success'
    default:
      return 'alert-error'
  }
})
</script>

<style>
.alert-error {
  background: #611515;
  padding: 1rem;
  border-radius: 0.5rem;
  margin: 2rem;
}
.alert-success {
  background: #186112;
  padding: 1rem;
  border-radius: 0.5rem;
  margin: 2rem;
}
.alert-warning {
  background: #616012;
  padding: 1rem;
  border-radius: 0.5rem;
  margin: 2rem;
}
</style>
