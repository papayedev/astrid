<template>
  <div class="container">
    <h1>Demande de mot de passe oublié</h1>
    <AlertComponent alertType="error" v-if="hasError">{{ hasMessage }}</AlertComponent>
    <div class="grid">
      <Logo width="50%" height="50%" />
      <div class="form-content">
        <form @submit.prevent.stop>
          <fieldset>
            <label>
              Adresse Email
              <input
                v-model="form.email"
                type="email"
                name="email"
                placeholder="Email"
                autocomplete="email"
              />
            </label>
          </fieldset>
          <input type="submit" value="Faire la demande" @click="requestPasswordAction" />
        </form>
      </div>
    </div>
  </div>
</template>

<style>
.container {
  margin-top: 6rem;
  text-align: center;
}
h1 {
  margin: 2rem;
}
</style>

<script setup>
import AlertComponent from '@/components/AlertComponent.vue'
import { useUserStore } from '@/stores/userStore.js'
import { storeToRefs } from 'pinia'
import { reactive } from 'vue'
import router from '@/router/index.js'
import Logo from '@/components/Logo.vue'

const { hasError, hasMessage } = storeToRefs(useUserStore())
const { requestPassword } = useUserStore()

const form = reactive({
  email: '',
})

const requestPasswordAction = async () => {
  const success = await requestPassword(form.email)
  if (success) {
    router.push('/reset/password')
  }
}
</script>
