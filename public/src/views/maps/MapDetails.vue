<script setup lang="ts">
import { onMounted, nextTick, ref, onUnmounted, watch } from "vue";
import { GoogleMap } from "@capacitor/google-maps";

// PROPS - Props allow us to pass data into the component
const props = defineProps<{
  markerData: { coordinate: any; title: string; snippet: string }[];
}>();

// EVENTS - Events allow us to emit data from the component
const emits = defineEmits<{
  (event: "onMarkerClicked", info: any): void;
  (event: "onMapClicked", data: { latitude: number; longitude: number; }): void;
}>();

const mapRef = ref<HTMLElement>();
const markerIds = ref<string[] | undefined>();
let newMap: GoogleMap;

// Watch for changes in `markerData` and update the markers on the map
watch(
  () => props.markerData,
  async () => {
    console.log("markerData changed", props.markerData);
    if (newMap) {
      await updateMarkers();
    }
  },
  { immediate: true, deep: true }
);

// Function to add/update markers
const updateMarkers = async () => {
  if (!newMap) return;

  // Remove existing markers
  if (markerIds.value) {
    await newMap.removeMarkers(markerIds.value);
  }

  // Add new markers
  const markers = props.markerData.map(({ coordinate, title, snippet }) => ({
    coordinate,
    title,
    snippet,
  }));
  markerIds.value = await newMap.addMarkers(markers);
};

// we need to wait for the element that the map will be associated
// with to be in the DOM
onMounted(async () => {
  console.log("mounted ", mapRef.value);
  await nextTick(); // this wait for the render to complete
  await createMap();
});

// remove markers on unmount
onUnmounted(() => {
  console.log("onunmounted");
  newMap.removeMarkers(markerIds?.value as string[]);
});

/**
 * add markers to map using prop passed in to component
 * @param newMap 
 */
const addSomeMarkers = async (newMap: GoogleMap) => {
  markerIds?.value && newMap.removeMarkers(markerIds?.value as string[]);
  // Plot each point on the map
  let markers = props.markerData.map(({ coordinate, title, snippet }) => {
    return {
      coordinate,
      title,
      snippet,
    };
  });
  markerIds.value = await newMap.addMarkers(markers);
};

/**
 * 
 */
async function createMap() {
  if (!mapRef.value) return;
  console.log(import.meta.env.VITE_GOOGLE_MAPS_API_KEY);
  // render map using capacitor plugin
  newMap = await GoogleMap.create({
    id: "my-cool-map",
    element: mapRef.value,
    apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY as string,
    config: {
      center: {
        lat: 41.17862567644741,
        lng: -8.607281259611911,
      },
      zoom: 14,
    },
  });
  // add markers to map
  addSomeMarkers(newMap);
  // Set Event Listeners...
  // Handle marker click, send event back to parent
  newMap.setOnMarkerClickListener((event) => {
    emits("onMarkerClicked", event);
  });
  // Handle map click, send event back to parent
  newMap.setOnMapClickListener((data: { latitude: number, longitude: number }) => {
    emits("onMapClicked", {
      latitude: data.latitude,
      longitude: data.longitude,
    });
  });
}
</script>
<template>
  <div>
    <capacitor-google-map ref="mapRef" style="display: inline-block; width: 100vw; height: 75vh">
    </capacitor-google-map>
  </div>
</template>