<template>
  <ion-content class="ion-padding">
    <h1 class="title">Dashboard</h1>

    <!-- Promoted Events Section -->
    <div v-if="filteredPromotedEvents.length" class="promoted-events-section">
      <h2 class="subtitle">Promoted Events</h2>
      <div class="promoted-events-bar">
        <router-link
          v-for="event in filteredPromotedEvents"
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
              <p><strong>Creator:</strong> {{ event.creator.name }}</p>
              <p><strong>Location:</strong> {{ event.location }}</p>
              <p><strong>Category:</strong> {{ event.category }}</p>
              <p v-if="event.attendees > 0"><strong>Attendees:</strong> {{ event.attendees }}</p>
              <p v-else>No attendees yet</p>
            </ion-card-content>
          </ion-card>
        </router-link>
      </div>
    </div>

    <!-- Non-Promoted Events Section -->
    <div class="event-cards-container">
      <div class="event-cards">
        <router-link
          v-for="event in filteredNonPromotedEvents"
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
              <p><strong>Creator:</strong> {{ event.creator.name }}</p>
              <p><strong>Location:</strong> {{ event.location }}</p>
              <p><strong>Category:</strong> {{ event.category }}</p>
              <p v-if="event.attendees > 0"><strong>Attendees:</strong> {{ event.attendees }}</p>
              <p v-else>No attendees yet</p>
            </ion-card-content>
          </ion-card>
        </router-link>
      </div>
    </div>
  </ion-content>
</template>

<script lang="ts">
import { ref, computed, onMounted } from 'vue';
import { fetchNonPromotedEvents, fetchPromotedEvents } from '@/lib/eventRequests';
import { formatDate } from '@/lib/dateFormatter';
import { categories, categoryColors } from '@/lib/categories';

export default {
  setup() {
    const promotedEvents = ref([]);
    const nonPromotedEvents = ref([]);
    const selectedCategories = ref([]);
    const dateLimit = ref(null);

    const userId = ref(localStorage.getItem('uuid') || ''); // Get user ID from localStorage

    const loadPromoted = async () => {
      try {
        promotedEvents.value = await fetchPromotedEvents(userId.value); // Pass userId if API requires it
      } catch (error) {
        console.error('Error fetching promoted events:', error);
      }
    };

    const loadNonPromoted = async () => {
      try {
        nonPromotedEvents.value = await fetchNonPromotedEvents(userId.value); // Pass userId if API requires it
      } catch (error) {
        console.error('Error fetching non-promoted events:', error);
      }
    };

    const filteredNonPromotedEvents = computed(() =>
      nonPromotedEvents.value.filter(event => {
        const isCategoryMatch =
          selectedCategories.value.length === 0 ||
          selectedCategories.value.includes(event.category);
        const isDateMatch =
          !dateLimit.value || new Date(event.endDate) <= new Date(dateLimit.value);
        return isCategoryMatch && isDateMatch;
      })
    );

    const filteredPromotedEvents = computed(() =>
      promotedEvents.value.filter(event => {
        const isCategoryMatch =
          selectedCategories.value.length === 0 ||
          selectedCategories.value.includes(event.category);
        const isDateMatch =
          !dateLimit.value || new Date(event.endDate) <= new Date(dateLimit.value);
        return isCategoryMatch && isDateMatch;
      })
    );

    onMounted(() => {
      loadPromoted();
      loadNonPromoted();
    });

    return {
      promotedEvents,
      nonPromotedEvents,
      filteredNonPromotedEvents,
      filteredPromotedEvents,
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

.promoted-events-section {
  margin-bottom: 40px;
}

.promoted-events-bar {
  display: flex;
  overflow-x: auto;
  gap: 16px;
}

.clickable-card {
  width: 250px;
  flex-shrink: 0;
  text-decoration: none;
}

.event-cards-container {
  padding: 16px;
}

.event-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
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
</style>
