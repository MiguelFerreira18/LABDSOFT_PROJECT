<template>
    <ion-page>
        <ion-header>
            <ion-toolbar>
                <ion-title>Search Events</ion-title>
            </ion-toolbar>
        </ion-header>
        <ion-content>
            <div v-if="currentPosition">
                <MapComponent :latitude="currentPosition.latitude" :longitude="currentPosition.longitude" :events="events"/>
            </div>
            <div v-else-if="errorMessage">
                <p>Error: {{ errorMessage }}</p>
                </div>
            <div v-else>
                <p>Fetching location...</p>
            </div>
        </ion-content>
    </ion-page>
</template>

<script setup lang="ts">
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
} from '@ionic/vue'
import { ref, onMounted } from 'vue';
import { fetchNonPromotedEvents } from '@/lib/eventRequests';
import { Geolocation } from '@capacitor/geolocation';
import MapComponent from '@/components/maps/MapComponent.vue';
import { Event } from '@/domain/Event';

const currentPosition = ref<{ latitude: number; longitude: number } | null>(null);
const errorMessage = ref<string | null>(null);
const events = ref<Event[]>([]);

const getCurrentLocation = async () => {
  try {
    const coordinates = await Geolocation.getCurrentPosition();
    currentPosition.value = {
      latitude: coordinates.coords.latitude,
      longitude: coordinates.coords.longitude
    };
  } catch (error: any) {
    errorMessage.value = error.message || 'Unable to fetch location';
  }
};

async function fetchEvents() {

    try {
        events.value = await fetchNonPromotedEvents();
        console.log('Events:', events.value);
    } catch (error) {
        console.log(error); 
    }
}

onMounted(() => {
    getCurrentLocation();
    fetchEvents();
});
</script>

<style>
button.gm-control-active.gm-fullscreen-control {
    display: none;
}

.gmnoprint.gm-style-mtc-bbw {
    display: none;
}

button.gm-svpc {
    display: none;
}
</style>