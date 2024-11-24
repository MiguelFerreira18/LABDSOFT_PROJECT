<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Create New Event</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true" class="ion-padding">
      <form @submit.prevent="addEvent">

        <ion-input class="ion-margin-vertical" label="Title" fill="outline" label-placement="floating"
          placeholder="Run Club" id="title" v-model="event.title" required></ion-input>
        <ion-input class="ion-margin-vertical" label="Location" fill="outline" label-placement="floating"
          placeholder="Central Park" id="location" v-model="event.location" required></ion-input>
        <ion-button @click="navigateToMap" size="small" fill="outline">Select Location On Map</ion-button>
        <ion-input class="ion-margin-vertical" label="Start Date" fill="outline" label-placement="floating" type="date"
          id="startDate" v-model="event.startDate" required></ion-input>
        <ion-input class="ion-margin-vertical" label="End Date" fill="outline" label-placement="floating" type="date"
          id="endDate" v-model="event.endDate" required></ion-input>
        <ion-textarea class="ion-margin-vertical" label="Description" fill="outline" label-placement="floating"
          placeholder="Join us for a run around Central Park!" id="description" v-model="event.description"
          required></ion-textarea>
        <ion-select class="ion-margin-vertical" :aria-label="'fruit'" :placeholder="'Select Category'"
          @ionChange="handleCategoryChange" :key="'category-select'">
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
import { createEvent } from "@/lib/eventRequests"; // Import the updated createEvent function
import { categories } from "@/lib/categories";
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent, IonInput, IonSelect, IonButton, IonSelectOption, IonTextarea, IonIcon } from '@ionic/vue';
import { navigateOutline } from 'ionicons/icons';
import { locationState } from "@/stateManagement/locationState";

// Router instance for navigation
const router = useRouter();

// Event data
const event = ref<any>({
  title: '',
  location: '',
  startDate: '',
  endDate: '',
  description: '',
  category: '',
  latitude: null,
  longitude: null
});

const errorMessage = ref("");

// Handle category selection change
function handleCategoryChange(domEvent: any) {
  event.value.category = domEvent.target.value;
}

// Add Event Function
async function addEvent() {
  try {
    // Check if creatorID is available in localStorage
    const creatorID = localStorage.getItem('uuid');
    if (!creatorID) {
      throw new Error("No creator ID found. Please log in first.");
    }
    console.log("Creator ID:", creatorID); // Log the creatorID to check it

    // Prepare the event payload
    const payload = {
      title: event.value.title,
      location: event.value.location,
      startDate: event.value.startDate,
      endDate: event.value.endDate,
      description: event.value.description,
      category: event.value.category,
      creatorID: creatorID, // Pass the creatorID
      latitude: event.value.latitude ?? null,
      longitude: event.value.longitude ?? null,
    };
    
    console.log("Payload to create event:", payload);

    // Call the createEvent function from lib to create the event
    const createdEvent = await createEvent(payload);
    console.log("Event created successfully:", createdEvent);
    router.push(`/event/EventDetail/${createdEvent.id}`);
  } catch (error: any) {
    console.error("Error creating event:", error);
    errorMessage.value = error.message || "Failed to create event. Please try again.";
  }
}

// Navigate to map to select location
function navigateToMap() {
  locationState.onLocationSelected = handleLocationSelected;
  router.push({ name: 'map' });
}

// Handle location selected from the map
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
