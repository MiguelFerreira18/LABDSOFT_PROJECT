<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Search Events</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <ion-fab horizontal="end">
        <ion-fab-button @click="toggleDropdown">
          <ion-icon :icon="filterCircleOutline"></ion-icon>
        </ion-fab-button>
      </ion-fab>

      <div v-if="showDropdown" class="dropdown-menu">
        <label for="search-input">Search by Location (km radius):</label>
        <input
          style="background-color: var(--ion-color-light)"
          id="search-input"
          type="number"
          v-model="searchQuery"
          placeholder="Enter radius in km"
          @input="updateZoom(searchQuery)"
        />
        <ion-button color="danger" @click="clearSearch" class="clear-button">
          Clear
        </ion-button>
      </div>

      <div v-if="currentPosition">
        <MapComponent
          :latitude="currentPosition.latitude"
          :longitude="currentPosition.longitude"
          :events="events"
          :zoom="mapZoom"
        />
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
import { ref, onMounted } from 'vue';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButton,
} from '@ionic/vue';
import { filterCircleOutline } from 'ionicons/icons';
import { fetchNonPromotedEvents } from '@/lib/eventRequests';
import { Geolocation } from '@capacitor/geolocation';
import MapComponent from '@/components/maps/MapComponent.vue';
import { Event } from '@/domain/Event';

const currentPosition = ref<{ latitude: number; longitude: number } | null>(
  null,
);
const errorMessage = ref<string | null>(null);
const events = ref<Event[]>([]);
const showDropdown = ref(false);
const searchQuery = ref('');

const mapZoom = ref<number>(12);

const getCurrentLocation = async () => {
  try {
    const coordinates = await Geolocation.getCurrentPosition();
    currentPosition.value = {
      latitude: coordinates.coords.latitude,
      longitude: coordinates.coords.longitude,
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

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const clearSearch = () => {
  searchQuery.value = '';
  mapZoom.value = 12;
};

const updateZoom = (distance: string) => {
  const radiusInKm = parseFloat(distance);

  let zoomLevel = 20 - Math.log(radiusInKm + 1) * 3;

  mapZoom.value = zoomLevel;
};

onMounted(() => {
  getCurrentLocation();
  fetchEvents();
});
</script>

<style scoped>
ion-fab-button {
  position: fixed;
  top: 300px;
  right: 20px;
  z-index: 10;
}

.dropdown-menu {
  position: fixed;
  top: 130px;
  right: 16px;
  background: var(--ion-color-light);
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 200px;
  z-index: 100;
}

#search-input {
  padding: 8px;
  border: 1px solid var(--ion-color-medium);
  border-radius: 4px;
  font-size: 16px;
}

.clear-button {
  margin-top: 10px;
  align-self: flex-end;
  font-size: 14px;
}
</style>
