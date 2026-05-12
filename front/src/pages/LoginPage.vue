<template>
  <div class="container">
    <h1>Content de te revoir !</h1>
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
            <label>
              Mot de passe
              <input
                v-model="form.password"
                type="password"
                name="password"
                placeholder="*****"
                autocomplete="current-password"
              />
            </label>
          </fieldset>
          <input type="submit" value="Connexion" @click="loginAction" />
        </form>
        <div class="activate-link"><a href="/forgot/password">Mot de passe oublié</a></div>
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
const { login } = useUserStore()

const form = reactive({
  email: '',
  password: '',
})

const loginAction = async () => {
  const response = await login(form.email, form.password)
  if (response === 200) {
    router.push('/dashboard')
  } else if (response === 403) {
    router.push('/verification')
  }
}
</script>
