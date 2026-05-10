<template>
  <div class="container">
    <h1>Modifier son mot de passe</h1>
    <AlertComponent alertType="error" v-if="hasError"
      >{{ hasMessage }}</AlertComponent
    >
    <div class="grid">
      <Logo/>
      <div class="form-content">
        <form @submit.prevent.stop>
          <fieldset>
            <label>
              Adresse Email
              <input v-model="form.email" type="email" name="email" placeholder="Email" autocomplete="email" />
            </label>
            <label>
              Code de Vérification
              <input v-model="form.verificationCode" type="text" name="verificationCode" placeholder="Code de vérification" autocomplete="verificationCode" />
            </label>
            <label>
              Mot de passe
              <input v-model="form.password" type="password" name="password" placeholder="****" autocomplete="password" />
            </label>
          </fieldset>
          <input type="submit" value="Faire la demande" @click="changePasswordAction" />
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

const { hasError, hasMessage } = storeToRefs(useUserStore())
const { changePassword } = useUserStore()
import router from "@/router/index.js";
import Logo from "@/components/Logo.vue";

const form = reactive({
  email: '',
  verificationCode: '',
  password: ''
})

const changePasswordAction = async () => {
    const success = await changePassword(form.email, form.verificationCode, form.password)
    if (success) {
        router.push("/login")
    }
}
</script>
