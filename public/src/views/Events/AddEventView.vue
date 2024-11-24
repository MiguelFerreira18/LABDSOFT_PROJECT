<template>
  <div>
    <h1>Create New Event</h1>
    <form @submit.prevent="addEvent">
      <!-- Event Title -->
      <div>
        <label for="title">Title</label>
        <input type="text" id="title" v-model="event.title" required />
      </div>

      <!-- Event Location -->
      <div>
        <label for="location">Location</label>
        <input type="text" id="location" v-model="event.location" required />
      </div>

      <!-- Start Date -->
      <div>
        <label for="startDate">Start Date</label>
        <input type="date" id="startDate" v-model="event.startDate" required />
      </div>

      <!-- End Date -->
      <div>
        <label for="endDate">End Date</label>
        <input type="date" id="endDate" v-model="event.endDate" required />
      </div>

      <!-- Description -->
      <div>
        <label for="description">Description</label>
        <textarea id="description" v-model="event.description" required></textarea>
      </div>

      <!-- Categories -->
      <div>
        <label for="category">Category</label>
        <select id="category" v-model="event.category" required>
          <option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </option>
        </select>
      </div>

      <!-- Submit Button -->
      <button type="submit">Create Event</button>
    </form>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { createEvent } from "@/lib/eventRequests";
import { useRouter } from "vue-router";

// Reactive Variables
const event = ref({
  title: "",
  location: "",
  startDate: "",
  endDate: "",
  description: "",
  category: "",
});
const categories = ["Social", "Sports", "Education", "Entertainment", "Other"]; // Predefined categories
const errorMessage = ref("");
const router = useRouter();

// Get user ID
const userId = localStorage.getItem("uuid");
if (!userId) {
  errorMessage.value = "User not logged in. Please log in to create an event.";
  // Optionally, redirect to login
  router.push("/login");
}

// Add Event Function
async function addEvent() {
  try {
    const payload = {
      ...event.value,
      creatorID: userId,
    };

    // Call the createEvent function
    const createdEvent = await createEvent(payload);

    console.log("Event created successfully:", createdEvent);
    router.push(`/event/${createdEvent.id}`); // Navigate to the event detail page
  } catch (error) {
    console.error("Error creating event:", error);
    errorMessage.value = "Failed to create event. Please check the input and try again.";
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