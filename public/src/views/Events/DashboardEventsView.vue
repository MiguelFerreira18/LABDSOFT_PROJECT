<template>
  <ion-content class="ion-padding">
    <h1 class="title">Dashboard</h1>

    <div v-if="loading" class="loading-spinner">
      <ion-spinner name="crescent"></ion-spinner>
    </div>

    <div v-else-if="eventSummaries.length === 0" class="no-events-message">
      <p>No events available to display.</p>
    </div>

    <div v-else class="dashboard-cards-container">
      <div class="dashboard-cards">
        <ion-card v-for="summary in eventSummaries" :key="summary.id" class="dashboard-card">
          <ion-card-header>
            <ion-card-title>{{ summary.title }}</ion-card-title>
            <ion-card-subtitle>{{ formatDate(summary.date) }}</ion-card-subtitle>
          </ion-card-header>

          <ion-card-content>
            <p><strong>Location:</strong> {{ summary.location }}</p>
            <p><strong>Total Attendees:</strong> {{ summary.totalAttendees }}</p>
          </ion-card-content>
        </ion-card>
      </div>
    </div>
  </ion-content>
</template>

<script>
import { ref, onMounted } from 'vue';
import { fetchDashboardSummaries } from '@/lib/dashboardRequests'; // A new helper to fetch dashboard summaries
import { formatDate } from '@/lib/dateFormatter';
import { IonCard, IonCardHeader, IonCardTitle, IonCardSubtitle, IonCardContent, IonSpinner } from '@ionic/vue';

export default {
  components: {
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardSubtitle,
    IonCardContent,
    IonSpinner,
  },
  setup() {
    const eventSummaries = ref([]);
    const loading = ref(true);

    // Fetch event summaries from the backend
    const loadDashboard = async () => {
      try {
        const data = await fetchDashboardSummaries();
        eventSummaries.value = data;
      } catch (error) {
        console.error('Failed to load dashboard:', error);
      } finally {
        loading.value = false;
      }
    };

    // Load the dashboard data on page mount
    onMounted(loadDashboard);

    return {
      eventSummaries,
      loading,
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
  margin-bottom: 20px;
}

.loading-spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.no-events-message {
  text-align: center;
  font-size: 1.2rem;
  color: var(--ion-color-medium);
}

.dashboard-cards-container {
  padding: 16px;
}

.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.dashboard-card {
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
