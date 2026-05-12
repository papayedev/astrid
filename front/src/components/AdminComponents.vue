<script setup>
import { storeToRefs } from 'pinia'
import Modal from '@/components/Modal.vue'
import { onMounted, reactive, ref, watchEffect } from 'vue'
import AlertComponent from '@/components/AlertComponent.vue'
import { useAdminStore } from '@/stores/adminStore.js'

const { hasError, hasMessage } = storeToRefs(useAdminStore())
const { deleteDevice, createDevice, getAllDevices } = useAdminStore()
const { devices, maxPage } = storeToRefs(useAdminStore())

onMounted(async () => await getAllDevices(page.value))

const page = ref(0)

watchEffect(() => {
  getAllDevices(page.value)
})

const deviceModel = reactive({
  name: '',
  serialNumber: '',
  pin: '',
})

const deviceModal = ref(null)

const createDeviceAction = async () => {
  const success = await createDevice(deviceModel.name, deviceModel.serialNumber, deviceModel.pin)
  if (success) {
    deviceModal.value.closeModal()
    await getAllDevices(page.value)
  }
}

const deleteDeviceAction = async (deviceId) => {
  const confirmation = confirm(`Are you sure you want to delete ${deviceId}?`)
  if (confirmation) {
    const success = await deleteDevice(deviceId)
    if (success) {
      await getAllDevices(page.value)
    }
  }
}
</script>

<template>
  <h1>Administrateur</h1>
  <Modal
    ref="deviceModal"
    buttonTitle="Créer une device"
    title="Créer une device"
    description="Ce formulaire permet de créer une device"
  >
    <template #errors>
      <AlertComponent alertType="error" v-if="hasError">{{ hasMessage }}</AlertComponent>
    </template>

    <template #article>
      <form @submit.prevent.stop>
        <label for="name" class="form-label">Nom</label>
        <input type="text" name="name" v-model="deviceModel.name" />
        <label for="serialNumber" class="form-label">Numéro de série</label>
        <input type="text" name="serialNumber" v-model="deviceModel.serialNumber" />
        <label for="pin" class="form-label">Code Pin</label>
        <input type="text" name="pin" v-model="deviceModel.pin" />
      </form>
    </template>

    <template #footer>
      <button @click="createDeviceAction">Valider</button>
    </template>
  </Modal>

  <div style="margin-top: 2rem" class="devices">
    <div class="grid">
      <p style="margin-top: 1rem">Page : {{ page }} / {{ maxPage - 1 }}</p>
      <input v-model="page" type="number" placeholder="Page" />
    </div>
    <table style="margin-top: 2rem; margin-bottom: 5rem">
      <thead>
        <tr>
          <th scope="col">Id</th>
          <th scope="col">Nom</th>
          <th scope="col">Numéro de série</th>
          <th scope="col">User ID</th>
          <th scope="col">Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="device in devices">
          <td>{{ device.id }}</td>
          <td>{{ device.name }}</td>
          <td>{{ device.serialNumber }}</td>
          <td>{{ device.userId }}</td>
          <td>
            <Modal
              buttonTitle="Voir une device"
              title="Information sur votre device"
              description="Ce formulaire permet de voir une device"
            >
              <template #article>
                <ul>
                  <li>ID {{ device.id }}</li>
                  <li>Name {{ device.name }}</li>
                  <li>Serial Number : {{ device.serialNumber }}</li>
                  <li>PIN : {{ device.pin }}</li>
                  <li>UserID : {{ device.userId }}</li>
                </ul>
              </template>

              <template #footer>
                <button @click="deleteDeviceAction(device.id)" class="red">Supprimer</button>
              </template>
            </Modal>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
