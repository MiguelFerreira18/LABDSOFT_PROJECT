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
        <label for="categories">Categories (comma separated)</label>
        <input type="text" id="categories" v-model="event.categories" />
      </div>
      <button type="submit">Create Event</button>
    </form>

    <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
import { createEvent } from "@/lib/eventRequests"; // Import the createEvent function

export default {
  data() {
    return {
      event: {
        title: "",
        location: "",
        startDate: "",
        endDate: "",
        description: "",
        categories: "",
      },
      errorMessage: "",
    };
  },
  methods: {
    async addEvent() {
      try {
        // Prepare event data
        const eventData = {
          title: this.event.title,
          location: this.event.location,
          startDate: this.event.startDate,
          endDate: this.event.endDate,
          description: this.event.description,
          categories: this.event.categories.split(",").map((cat) => cat.trim()), // Convert string to array
        };

        // Call the createEvent function
        const createdEvent = await createEvent(eventData);
        this.$router.push(`/event/${createdEvent.id}`); // Redirect to the event details page after successful creation
      } catch (error) {
        this.errorMessage = "Failed to create event. Please try again.";
      }
    },
  },
};
</script>

<style scoped>
/* Optional: Add some basic styling */
form {
  max-width: 600px;
  margin: 0 auto;
}

input, textarea, button {
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

