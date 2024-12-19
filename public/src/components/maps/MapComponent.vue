<template>
  <div>
    <capacitor-google-map
      ref="mapRef"
      style="display: inline-block; width: 100vw; height: 90vh"
    ></capacitor-google-map>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch } from 'vue';
import { GoogleMap } from '@capacitor/google-maps';
import { Event } from '@/domain/Event';

// Props for latitude, longitude, events, and zoom
const props = defineProps<{
  latitude: number;
  longitude: number;
  events: Event[];
  zoom?: number; // Make zoom optional
}>();

const mapRef = ref<HTMLElement>();
const imageSrc = ref('');
let mapInstance: GoogleMap;

const initialZoom = props.zoom ?? 12;

onMounted(async () => {
  console.log('Start mounting map');

  const basePath = window.location.origin;
  imageSrc.value = `${basePath}/assets/man.png`;

  await nextTick();
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
      zoom: initialZoom,
    },
  });

  const userLocationMarker = await mapInstance.addMarker({
    coordinate: {
      lat: props.latitude,
      lng: props.longitude,
    },
    title: 'Event Location',
    snippet: 'Event Location',
    iconUrl: imageSrc.value,
    iconSize: {
      width: 36,
      height: 36,
    },
  });

  await addMarkersToMap();
}

async function addMarkersToMap() {
  try {
    const markers = props.events.map((event) => ({
      coordinate: {
        lat: event.latitude,
        lng: event.longitude,
      },
      title: event.title,
    }));

    await mapInstance.addMarkers(markers);
  } catch (error) {
    console.log(error);
  }
}

watch(
  () => props.zoom,
  (newZoom) => {
    if (mapInstance) {
      mapInstance.setCamera({
        coordinate: {
          lat: props.latitude,
          lng: props.longitude,
        },
        zoom: newZoom,
      });
    }
  },
);
</script>
