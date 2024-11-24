<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Google Map test</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <my-map :markerData="markerData" @onMapClicked="mapClicked" @onMarkerClicked="markerClicked"></my-map>
      <ion-popover :is-open="markerIsOpen" size="cover" @did-dismiss="markerIsOpen = false">
        <ion-content class="ion-padding">
          <div>{{ markerInfo?.title }}</div>
        </ion-content>
      </ion-popover>
      <!-- <span v-if="address"><strong>Address:</strong> {{ address }}</span> -->
      <ion-button @click="selectLocation">Select this Location</ion-button>
    </ion-content>
  </ion-page>
</template>
<script setup lang="ts">
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent, modalController, IonPopover, IonButton } from '@ionic/vue';
import { ref, defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import { Capacitor } from "@capacitor/core";
import MarkerInfoWindow from "@/components/maps/mapDetailsWindows.vue"
import MyMap from "@/views/maps/MapDetails.vue";
import { locationState } from '@/stateManagement/locationState';

const router = useRouter();

const emit = defineEmits<{
  (event: 'locationSelected', payload: { latitude: number; longitude: number }): void;
}>();

const markerInfo = ref<any>();
const markerIsOpen = ref<boolean>(false);
const latitude = ref<number | null>(null);
const longitude = ref<number | null>(null);
const address = ref<string | null>(null);
const markerData = ref<{ coordinate: any; title: string; snippet: string }[]>([]);

const openModal = async (marker: any) => {
  const modal = await modalController.create({
    component: MarkerInfoWindow,
    componentProps: {
      marker,
    },
    initialBreakpoint: 0.2,
    breakpoints: [0, 0.2],
    backdropBreakpoint: 0,
    showBackdrop: false,
    backdropDismiss: true,
  });
  modal.present();
  const { data, role } = await modal.onWillDismiss();
};

const mapClicked = async (data: { latitude: number; longitude: number }) => {
  console.log('Map clicked at:', data.latitude, data.longitude);
  latitude.value = data.latitude;
  longitude.value = data.longitude;

  // Fetch the address after updating latitude and longitude
  const fetchedAddress = await fetchAddress();
  if (fetchedAddress) {
    address.value = fetchedAddress;
  }

  // Update marker data with the selected location
  updateMarkerData(data.latitude, data.longitude);
};

const updateMarkerData = (latitude: number, longitude: number, title: string = "Selected Location") => {

  // Clear the existing markers and add the new one
  markerData.value = [
    {
      coordinate: { lat: latitude, lng: longitude },
      title: title,
      snippet: "This is your selected location",
    },
  ];
};

const getMarkerInfo = (marker: { latitude: number; longitude: number }) => {
  return markerData.value.find(
    (m) =>
      m.coordinate.lat === marker.latitude &&
      m.coordinate.lng === marker.longitude
  );
};

const markerClicked = (event: any) => {
  console.log(event);
  // only use dialog in web
  if (!Capacitor.isNativePlatform()) {
    openModal(getMarkerInfo(event));
  }
};

//Submit the selected location
const selectLocation = () => {
  console.log('Location selected:', latitude.value, longitude.value);
  if (latitude.value && longitude.value && address.value) {
    const callback = locationState.onLocationSelected;

    if (callback) {
      callback({ latitude: latitude.value, longitude: longitude.value, address: address.value });
    } else {
      console.error('No callback function found.');
    }
    // Return to the previous page (AddEventView)
    // router.back();
    router.push({ name: 'AddEventView' });
  } else {
    console.error('No location selected.');
  }
};

async function fetchAddress(): Promise<string | null> {

  if (latitude.value === null || longitude.value === null) {
    console.error('Latitude and Longitude are required to fetch the address.');
    return null;
  }

  const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude.value},${longitude.value}&result_type=administrative_area_level_2&key=${import.meta.env.VITE_GOOGLE_MAPS_API_KEY}`

  try {
    const response = await fetch(url);
    const data = await response.json();
    if (data.status === 'OK' && data.results.length > 0) {
      console.log('Address:', data.results[0].formatted_address);
      return data.results[0].formatted_address.split(",")[0].trim();
    } else {
      console.error('Geocoding API Error:', data);
      return 'Unable to fetch address';
    }
  } catch (error) {
    console.error('Error fetching Geocoding data:', error);
    return 'Error fetching address';
  }
}
</script>

<style>
#map-container {
  width: 100%;
  height: 100%;
  position: absolute;
}
</style>