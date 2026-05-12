<template>
  <l-map v-model:zoom="zoom" :center="startPosition" style="height: 100%; width: 100%">
    <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

    <l-polyline :lat-lngs="latLngs" color="green" />

    <l-marker v-if="latLngs.length" :lat-lng="latLngs[0]" :icon="homeIcon">
      <l-popup>Départ</l-popup>
    </l-marker>

    <l-marker v-for="(pos, index) in middlePoints" :key="index" :lat-lng="pos">
      <l-popup> Point {{ index + 1 }} </l-popup>
    </l-marker>

    <l-marker v-if="latLngs.length" :lat-lng="latLngs[latLngs.length - 1]" :icon="dogIcon">
      <l-popup>Animal</l-popup>
    </l-marker>
  </l-map>
</template>

<script setup>
import { computed, ref } from 'vue'
import L from 'leaflet'

import { LMap, LTileLayer, LMarker, LPolyline, LPopup } from '@vue-leaflet/vue-leaflet'

import 'leaflet/dist/leaflet.css'

const props = defineProps({
  positions: {
    type: Array,
    required: true,
  },
})

const zoom = ref(13)

const latLngs = computed(() =>
  props.positions.map((pos) => [parseFloat(pos.latitude), parseFloat(pos.longitude)]),
)

const startPosition = computed(() => latLngs.value[0] || [0, 0])

const middlePoints = computed(() => {
  if (latLngs.value.length <= 2) return []
  return latLngs.value.slice(1, -1)
})

const homeIcon = L.divIcon({
  html: '🏠',
  className: 'custom-div-icon',
  iconSize: [30, 30],
})

const dogIcon = L.divIcon({
  html: '🐶',
  className: 'custom-div-icon',
  iconSize: [30, 30],
})
</script>

<style>
@import 'leaflet/dist/leaflet.css';

.custom-div-icon {
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
