<template>
  <ion-content class="ion-padding">
    <h1 class="title">Dashboard</h1>

    <!-- Filters Section -->
    <div class="filters-section">
      <ion-item>
        <ion-label>Filter by Category</ion-label>
        <ion-select multiple placeholder="Select Categories" v-model="selectedCategories">
          <ion-select-option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </ion-select-option>
        </ion-select>
      </ion-item>
      <ion-item>
        <ion-label>Filter by Date</ion-label>
        <ion-datetime display-format="MMM DD, YYYY" v-model="dateLimit"></ion-datetime>
      </ion-item>
    </div>

    <!-- All Events Section -->
    <div v-if="filteredEvents.length" class="all-events-section">
      <h2 class="subtitle">All Events</h2>
      <div class="event-cards">
        <router-link v-for="event in filteredEvents" :key="event.eventId" :to="`/event/EventDetail/${event.eventId}`"
          class="clickable-card">
          <ion-card :style="{ backgroundColor: categoryColors[event.category] || '#ccc' }">
            <ion-card-header>
              <ion-card-title>{{ event.title }}</ion-card-title>
              <ion-card-subtitle>
                {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
              </ion-card-subtitle>
            </ion-card-header>
            <ion-card-content>
              <p><strong>Creator:</strong> {{ event.creator }}</p>
              <p><strong>Location:</strong> {{ event.location }}</p>
              <p><strong>Category:</strong> {{ event.category }}</p>
              <p v-if="event.totalAttendees > 0"><strong>Attendees:</strong> {{ event.totalAttendees }}</p>
              <p v-else>No attendees yet</p>
            </ion-card-content>
          </ion-card>
        </router-link>
      </div>
    </div>

    <div v-else class="no-events">
      <h3>No events found</h3>
    </div>
  </ion-content>
</template>

<script lang="ts">
import { ref, computed, onMounted } from 'vue';
import { fetchDashboardSummaries } from '@/lib/eventRequests'; // Import the correct API function
import { formatDate } from '@/lib/dateFormatter'; // Replace with your date formatter utility
import { categories, categoryColors } from '@/lib/categories'; // Replace with your category definitions

export default {
  setup() {
    const allEvents = ref<any>([]);
    const selectedCategories = ref<any>([]);
    const dateLimit = ref(null);

    const userId = ref(localStorage.getItem('uuid') || ''); // Fetch user ID from localStorage

    const loadEvents = async () => {
      try {
        allEvents.value = await fetchDashboardSummaries(userId.value); // Fetch all events
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };

    // Filtered events based on selected categories and date
    const filteredEvents = computed(() =>
      //@ts-expect-error - TS doesn't recognize the filter method on allEvents.value
      allEvents.value.filter(event => {
        const isCategoryMatch =
          selectedCategories.value.length === 0 ||
          selectedCategories.value.includes(event.category);
        const isDateMatch =
          !dateLimit.value || new Date(event.endDate) <= new Date(dateLimit.value);
        return isCategoryMatch && isDateMatch;
      })
    );

    onMounted(() => {
      loadEvents(); // Load events on component mount
    });

    return {
      allEvents,
      filteredEvents,
      selectedCategories,
      dateLimit,
      categories,
      categoryColors,
      formatDate,
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

.filters-section {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f8f8;
  border-radius: 8px;
}

.all-events-section {
  margin-top: 20px;
}

.event-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

.clickable-card {
  width: 100%;
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

.no-events {
  text-align: center;
  margin-top: 50px;
  color: #666;
}
</style>