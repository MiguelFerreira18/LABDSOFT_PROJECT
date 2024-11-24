<template>
  <ion-page>
    <ion-header>
        <ion-toolbar>
            <ion-title>Create New Event</ion-title>
        </ion-toolbar>
    </ion-header>
  <ion-content :fullscreen="true" class="ion-padding">
    <form @submit.prevent="addEvent">

      <ion-input class="ion-margin-vertical" label="Title" fill="outline" label-placement="floating" placeholder="Run Club" id="title" v-model="event.title" required></ion-input>
      <ion-input class="ion-margin-vertical" label="Location" fill="outline" label-placement="floating" placeholder="Central Park" id="location" v-model="event.location" required></ion-input>
      <ion-button @click="navigateToMap" size="small" fill="outline">S<ion-icon slot="start" :icon="navigateOutline"></ion-icon>elect Location On Map</ion-button>
      <ion-input class="ion-margin-vertical" label="Start Date" fill="outline" label-placement="floating" type="date" id="startDate" v-model="event.startDate" required></ion-input>
      <ion-input class="ion-margin-vertical" label="End Date" fill="outline" label-placement="floating" type="date" id="endDate" v-model="event.endDate" required></ion-input>
      <ion-textarea class="ion-margin-vertical" label="Description" fill="outline" label-placement="floating" placeholder="Join us for a run around Central Park!" id="description" v-model="event.description" required></ion-textarea>
      <ion-select class="ion-margin-vertical" :aria-label="'fruit'" :placeholder="'Select Category'" @ionChange="handleCategoryChange"
        :key="'category-select'">
        <ion-select-option v-for="category in categories" :value="category" :key="category">
          {{ category }}
        </ion-select-option>
      </ion-select>
      <br />
      <ion-button type="submit">Create Event</ion-button>
  </form>
  <div v-if="errorMessage" class="error-message">

      <p>{{ errorMessage }}</p>
    </div>
  </ion-content>
</ion-page>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from 'vue-router';
import { SendRequest } from "@/lib/request";
import { categories } from "@/lib/categories";
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent, IonInput, IonSelect, IonButton, IonSelectOption, IonTextarea, IonIcon } from '@ionic/vue';
import { navigateOutline } from 'ionicons/icons';
import { locationState } from "@/stateManagement/locationState";

const router = useRouter();
const event = ref<any>({});
const fetchedEvent = ref<any>({});
const errorMessage = ref("")

function handleCategoryChange(domEvent: any) {
  event.value.category = domEvent.target.value;

}

// Add Event Function
async function addEvent() {
  try {
    const payload = {

      title: event.value.title,
      location: event.value.location,
      startDate: event.value.startDate,
      endDate: event.value.endDate,
      description: event.value.description,
      category: event.value.category,
      creatorID: localStorage.getItem('uuid') || '',
      latitude: event.value.latitude,
      longitude: event.value.longitude,
    }
    console.log(payload);
    const response = await SendRequest('/api/events', 'POST', payload);
    const data = await response.json();
    fetchedEvent.value = data;
    if (response.ok) {
      router.push(`/event/EventDetail/${fetchedEvent.value.id}`);
    } else {
      errorMessage.value = "Failed to create event. Please try again.";
    }

  } catch (error) {
    console.error("Error creating event:", error);
    errorMessage.value = "Failed to create event. Please check the input and try again.";
  }
}

function navigateToMap() {
  locationState.onLocationSelected = handleLocationSelected;
  router.push({ name: 'map'});
}

// Handle location selected from map
function handleLocationSelected({ latitude, longitude, address }: { latitude: number; longitude: number; address: string }) {
  console.log(`Lat: ${latitude}, Lng: ${longitude}`);
  event.value.latitude = latitude;
  event.value.longitude = longitude;
  event.value.location = address;
}
</script>

<style scoped>
form {
  max-width: 600px;
  margin: 0 auto;
}

input,
textarea,
select,
button {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 8px;
  font-size: 16px;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
  font-size: 14px;
}
</style>