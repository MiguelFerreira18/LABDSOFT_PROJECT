<template>
  <ion-page>
    <ion-header>
        <ion-toolbar>
            <ion-title>Create New Event</ion-title>
        </ion-toolbar>
    </ion-header>
  <ion-content :fullscreen="true" class="ion-padding">
    <form @submit.prevent="addEvent">
      <ion-input label="Title" fill="outline" label-placement="floating" placeholder="Run Club" id="title" v-model="event.title" required></ion-input>
      <br />
      <ion-input label="Location" fill="outline" label-placement="floating" placeholder="Central Park" id="location" v-model="event.location" required></ion-input>
      <br />
      <ion-input label="Start Date" fill="outline" label-placement="floating" type="date" id="startDate" v-model="event.startDate" required></ion-input>
      <br />
      <ion-input label="End Date" fill="outline" label-placement="floating" type="date" id="endDate" v-model="event.endDate" required></ion-input>
      <br />
      <ion-textarea label="Description" fill="outline" label-placement="floating" placeholder="Join us for a run around Central Park!" id="description" v-model="event.description" required></ion-textarea>
      <br />
      <ion-select :aria-label="'fruit'" :placeholder="'Select Category'" @ionChange="handleCategoryChange"
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
import router from '@/router';
import { SendRequest } from "@/lib/request";
import { categories } from "@/lib/categories";
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent, IonInput, IonSelect, IonButton, IonSelectOption, IonTextarea } from '@ionic/vue';
const event = ref<any>({});
const fetchedEvent = ref<any>({});
const errorMessage = ref("")

function handleCategoryChange(domEvent: any) {
  event.value.category = domEvent.target.value;
}

async function addEvent() {
  try {
    const payload = {
      title: event.value.title,
      location: event.value.location,
      startDate: event.value.startDate,
      endDate: event.value.endDate,
      description: event.value.description,
      category: event.value.category,
      creatorID: localStorage.getItem('uuid') || ''
    }
    const response = await SendRequest('/api/events', 'POST', payload);
    const data = await response.json();
    fetchedEvent.value = data;
    if (response.ok) {
      router.push(`/event/EventDetail/${fetchedEvent.value.id}`);
    } else {
      errorMessage.value = "Failed to create event. Please try again.";
    }
  } catch (error) {
    errorMessage.value = "Failed to create event. Please try again.";
  }
}
</script>

<style scoped>
form {
  max-width: 600px;
  margin: 0 auto;
}

input,
textarea,
button {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 8px;
  font-size: 16px;
}

.error-message {
  color: red;
  font-size: 14px;
}
</style>
