<template>
    <ion-page>
        <ion-header>
            <ion-toolbar>
                <ion-title>Google Map test</ion-title>
            </ion-toolbar>
        </ion-header>
        <ion-content>
            <my-map
                :markerData="markerData"
                @onMapClicked="mapClicked"
                @onMarkerClicked="markerClicked"
            ></my-map>
            <ion-popover
                :is-open="markerIsOpen"
                size="cover"
                @did-dismiss="markerIsOpen = false"
            >
                <ion-content class="ion-padding">
                <div>{{ markerInfo?.title }}</div>
                </ion-content>
            </ion-popover>
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

// sample data for the map
const markerData = [
  {
    coordinate: { lat: 41.528919, lng: -8.622538 },
    title: "title one",
    snippet: "title one snippet content will be presented here",
  },
  {
    coordinate: { lat: 41.529687, lng: -8.615438 },
    title: "title two",
    snippet: "title two snippet content will be presented here",
  },
  {
    coordinate: { lat: 41.525909, lng: -8.622015 },
    title: "title three",
    snippet: "title three snippet content will be presented here",
  },
];
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

const mapClicked = (data: { latitude: number; longitude: number }) => {
  console.log("Map clicked at:", data.latitude, data.longitude);
  latitude.value = data.latitude;
  longitude.value = data.longitude;
};

const getMarkerInfo = (marker: { latitude: number; longitude: number }) => {
  return markerData.filter(
    (m) =>
      m.coordinate.lat === marker.latitude &&
      m.coordinate.lng === marker.longitude
  )[0];
};
const markerClicked = (event: any) => {
  console.log(event);
  // only use dialog in web since we doesnt show info window
  if (!Capacitor.isNativePlatform()) {
    openModal(getMarkerInfo(event));
  }
};

//Submit the selected location
const selectLocation = () => {
  console.log('Location selected:', latitude.value, longitude.value);
  if (latitude.value && longitude.value) {
    const callback = locationState.onLocationSelected;

    if(callback) {
      callback({ latitude: latitude.value, longitude: longitude.value });
    } else {
      console.error('No callback function found.');
    }
    // Return to the previous page (AddEventView)
    router.back(); 
  } else {
    console.error('No location selected.');
  }
};
</script>

<style>
#map-container {
    width: 100%;
    height: 100%;
    position: absolute;
}
</style>