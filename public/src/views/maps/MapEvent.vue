<template>
  <div>
    <capacitor-google-map
      ref="mapRef"
      style="display: inline-block; width: 100%; height: 30vh"
    ></capacitor-google-map>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, onUpdated, onBeforeMount } from 'vue';
import { GoogleMap } from '@capacitor/google-maps';

// Props for latitude and longitude
const props = defineProps<{
  latitude: number;
  longitude: number;
}>();

const mapRef = ref<HTMLElement>();
let mapInstance: GoogleMap;

onMounted(async () => {
  console.log('Start mouting map');
  await nextTick(); // Wait for DOM to render
  await initializeMap();
});

async function initializeMap() {
  if (!mapRef.value) return;

  mapInstance = await GoogleMap.create({
    id: 'event-map',
    element: mapRef.value,
    apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY as string,
    config: {
      center: {
        lat: props.latitude,
        lng: props.longitude,
      },
      zoom: 16,
    },
  });

  const marker = await mapInstance.addMarker({
    coordinate: {
      lat: props.latitude,
      lng: props.longitude,
    },
    title: 'Event Location',
    snippet: 'Event Location',
  });

  await mapInstance.disableTouch();
}
</script>
