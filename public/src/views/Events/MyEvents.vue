<template>
    <ion-content class="ion-padding">
      <h1 class="title">My Created Events</h1>
  
      <div v-if="createdEvents.length" class="event-cards-container">
        <div class="event-cards">
          <router-link
            v-for="event in createdEvents"
            :key="event.id"
            :to="`/event/EventDetail/${event.id}`"
            class="clickable-card"
          >
            <ion-card :style="{ backgroundColor: categoryColors[event.category] || '#ccc' }">
              <ion-card-header>
                <ion-card-title>{{ event.title }}</ion-card-title>
                <ion-card-subtitle>
                  {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
                </ion-card-subtitle>
              </ion-card-header>
              <ion-card-content>
                <p><strong>Location:</strong> {{ event.location }}</p>
                <p><strong>Category:</strong> {{ event.category }}</p>
                <p><strong>Attendees:</strong> {{ event.attendees.length }}</p>
              </ion-card-content>
            </ion-card>
          </router-link>
        </div>
      </div>
      <div v-else>
        <p class="no-events-message">You haven't created any events yet.</p>
      </div>
    </ion-content>
  </template>
  
  <script lang="ts">
  import { ref, onMounted } from 'vue';
  import { fetchCreatedEvents } from '@/lib/eventRequests';
  import { formatDate } from '@/lib/dateFormatter';
  import { categoryColors } from '@/lib/categories';
  
  // Define an interface for the Event type
  interface Event {
    id: string;
    title: string;
    category: string;
    startDate: string;
    endDate: string;
    location: string;
    attendees: string[]; // Adjust based on actual data structure
  }
  
  export default {
    setup() {
      // Initialize the array with the correct type
      const createdEvents = ref<Event[]>([]);
      const userId = localStorage.getItem('uuid'); // Retrieve the user ID from localStorage
  
      const loadCreatedEvents = async () => {
        try {
          if (!userId) {
            console.error('User ID not found.');
            return;
          }
          const events = await fetchCreatedEvents(userId);
          createdEvents.value = events; // Ensure events match the Event type
        } catch (error) {
          console.error('Error fetching created events:', error);
        }
      };
  
      onMounted(() => {
        loadCreatedEvents();
      });
  
      return {
        createdEvents,
        formatDate,
        categoryColors,
      };
    },
  };
  </script>
  
  <style scoped>
  .title {
    text-align: center;
    font-size: 2rem;
    font-weight: bold;
    color: aliceblue;
    margin-bottom: 40px;
  }
  
  .event-cards-container {
    padding: 16px;
  }
  
  .event-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }
  
  .clickable-card {
    text-decoration: none;
  }
  
  ion-card {
    border-radius: 8px;
    color: aliceblue;
  }
  
  ion-card-title {
    font-size: 1.2rem;
    font-weight: bold;
  }
  
  ion-card-subtitle {
    font-size: 0.9rem;
  }
  
  .no-events-message {
    text-align: center;
    font-size: 1.2rem;
    color: lightgray;
  }
  </style>