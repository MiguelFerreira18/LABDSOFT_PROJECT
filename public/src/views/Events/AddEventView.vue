<template>
  <div>
    <h1>Create New Event</h1>
    <form @submit.prevent="addEvent">
      <div>
        <label for="title">Title</label>
        <input type="text" id="title" v-model="event.title" required />
      </div>
      <div>
        <label for="location">Location</label>
        <input type="text" id="location" v-model="event.location" required />
      </div>
      <div>
        <label for="startDate">Start Date</label>
        <input type="date" id="startDate" v-model="event.startDate" required />
      </div>
      <div>
        <label for="endDate">End Date</label>
        <input type="date" id="endDate" v-model="event.endDate" required />
      </div>
      <div>
        <label for="description">Description</label>
        <textarea id="description" v-model="event.description" required></textarea>
      </div>
      <div>
        <ion-select :aria-label="'fruit'" :placeholder="'Select Category'" v-model="event.category"
          :key="'category-select'">
          <ion-select-option v-for="category in categories" :value="category" :key="category">
            {{ category }}
          </ion-select-option>
        </ion-select>
      </div>
      <button type="submit">Create Event</button>
    </form>

    <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import router from '@/router';
import { SendRequest } from "@/lib/request";
import { categories } from "@/lib/categories";
const event = ref<any>({});
const fetchedEvent = ref<any>({});
const errorMessage = ref("")

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
