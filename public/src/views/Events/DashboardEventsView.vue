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

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { formatDate } from '@/lib/dateFormatter';
import { IonCard, IonCardHeader, IonCardTitle, IonCardSubtitle, IonCardContent, IonSpinner } from '@ionic/vue';
import { SendRequest } from '@/lib/request';

const eventSummaries = ref<any>([]);
const loading = ref(true);

onMounted(async () => {
  await loadDashboard();
})

async function loadDashboard() {
  console.log('Loading dashboard...');

  const uuid = localStorage.getItem('uuid') || '';
  try {
    const response = await SendRequest(`/api/events/dashboard/${uuid}`, 'GET');
    const data = await response.json();
    console.log('Dashboard data:', data);

    eventSummaries.value = data;
  } catch (error) {
    console.error('Failed to load dashboard:', error);
  } finally {
    loading.value = false;
  }

}

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
