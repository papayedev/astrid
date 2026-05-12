<script setup>
import { storeToRefs } from 'pinia'
import { useVisitorStore } from '@/stores/visitorStore.js'
import Modal from '@/components/Modal.vue'
import { onMounted, reactive, ref } from 'vue'
import AlertComponent from '@/components/AlertComponent.vue'
import Map from '@/components/Map.vue'

const { linkDevice, getDevice } = useVisitorStore()
const { device, hasError, hasMessage } = storeToRefs(useVisitorStore())

const deviceForm = reactive({
  name: '',
  serialNumber: '',
  pin: '',
})
const positions = ref([
  { latitude: '50.6927', longitude: '3.1778' },
  { latitude: '50.6935', longitude: '3.1800' },
  { latitude: '50.6942', longitude: '3.1820' },
  { latitude: '50.6950', longitude: '3.1850' },
])
const linkModal = ref(null)
const linkDeviceAction = async () => {
  const success = await linkDevice(deviceForm.name, deviceForm.serialNumber, deviceForm.pin)
  if (success) {
    linkModal.value.closeModal()
    await getDevice()
  }
}

setInterval(() => {
  // TODO
}, 5000)

onMounted(async () => await getDevice())
</script>

<template>
  <div v-if="device.id !== ''" class="device">
    <div class="information">
      <h2>Appareil enregistré avec les informations suivante :</h2>
      <p>
        <br />
        ID : {{ device.id }}
        <br />
        Nom : {{ device.name }}
        <br />
        Numéro de série : {{ device.serialNumber }}
        <br />
      </p>
    </div>
    <div style="margin-top: 2rem; margin-bottom: 5rem" class="map">
      <Map :positions="positions" />
    </div>
  </div>

  <Modal
    v-if="device.id === '' || device.id === null"
    ref="linkModal"
    buttonTitle="Enregistrer un appareil"
    title="Enregistrer un appareil"
    description="Permet d'enregistrer votre appareil"
  >
    <template #errors>
      <AlertComponent alertType="error" v-if="hasError">{{ hasMessage }}</AlertComponent>
    </template>

    <template #article>
      <form @submit.prevent.stop>
        <label for="name">Nom:</label>
        <input
          v-model="deviceForm.name"
          type="text"
          name="name"
          id="name"
          placeholder="Nom de votre animal"
        />
        <label for="serialNumber">Numéro de série:</label>
        <input
          v-model="deviceForm.serialNumber"
          type="text"
          name="serialNumber"
          id="serialNumber"
          placeholder="Numéro de série"
        />
        <label for="pin">Code PIN :</label>
        <input v-model="deviceForm.pin" type="text" name="pin" id="pin" placeholder="Code PIN" />
      </form>
    </template>

    <template #footer>
      <button @click="linkDeviceAction">Enregistrer</button>
    </template>
  </Modal>
</template>
