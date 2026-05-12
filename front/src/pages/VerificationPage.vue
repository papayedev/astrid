<template>
  <div class="container">
    <h1>Verification d'email</h1>
    <AlertComponent alertType="error" v-if="hasError">{{ hasMessage }}</AlertComponent>
    <div class="grid">
      <Logo width="50%" height="50%" />
      <div class="form-content">
        <form @submit.prevent.stop>
          <fieldset>
            <label>
              Email
              <input
                v-model="form.email"
                type="email"
                name="email"
                placeholder="Email"
                autocomplete="email"
              />
            </label>
            <label>
              Verification code
              <input
                v-model="form.code"
                type="text"
                name="code"
                placeholder="Code de vérification"
                autocomplete="code"
              />
            </label>
          </fieldset>
          <input type="submit" value="Vérification" @click="verificationAction" />
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
const { verification } = useUserStore()

const form = reactive({
  code: '',
  email: '',
})

const verificationAction = async () => {
  const verified = await verification(form.email, form.code)
  if (verified) {
    router.push('/login')
  }
}
</script>
