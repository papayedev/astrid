<template>
  <div style="margin-top: 1rem; margin-bottom: 5rem">
    <button @click="showModal = true">{{ buttonTitle }}</button>

    <dialog v-if="showModal" open>
      <article>
        <h2>{{ title }}</h2>
        <p>
          {{ description }}
        </p>
        <slot name="errors"></slot>
        <slot name="article"></slot>
        <footer>
          <slot name="footer"></slot>
          <button @click="showModal = false">Fermer</button>
        </footer>
      </article>
    </dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps(['buttonTitle', 'title', 'description'])

const showModal = ref(false)

const closeModal = () => {
  showModal.value = false
}

defineExpose({
  closeModal,
})
</script>
<style>
dialog::backdrop {
  background: rgba(0, 0, 0, 0.5);
}

dialog {
  padding: 2rem;
  border-radius: 8px;
  max-width: 500px;
}
</style>
