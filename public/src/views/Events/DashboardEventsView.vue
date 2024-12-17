<template>
  <HeaderComponent title="Dashboard" />
  <ion-content class="ion-padding">
    <div v-if="loading" class="loading-spinner">
      <ion-spinner name="crescent"></ion-spinner>
    </div>

      <div v-else-if="eventSummaries.length === 0" class="no-events-message">
        <p>No events available to display.</p>
      </div>

    <div v-else class="dashboard-cards-container">
      <div class="dashboard-cards">
        <ion-card
          v-for="summary in eventSummaries"
          :key="summary.id"
          class="dashboard-card"
        >
          <ion-card-header>
            <ion-card-title>{{ summary.title }}</ion-card-title>
            <ion-card-subtitle>{{
              formatDate(summary.date)
            }}</ion-card-subtitle>
          </ion-card-header>

          <ion-card-content>
            <p><strong>Location:</strong> {{ summary.location }}</p>
            <p>
              <strong>Total Attendees:</strong> {{ summary.totalAttendees }}
            </p>
          </ion-card-content>
        </ion-card>
      </div>
    </div>
  </ion-content>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { formatDate } from '@/lib/dateFormatter';
import {
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardSubtitle,
  IonCardContent,
  IonSpinner,
} from '@ionic/vue';
import { SendRequest } from '@/lib/request';
import HeaderComponent from '@/components/common/HeaderComponent.vue';

const eventSummaries = ref<any>([]);
const loading = ref(true);

onMounted(async () => {
  await loadDashboard();
});

async function loadDashboard() {
  console.log('Loading dashboard...');

  const uuid = localStorage.getItem('uuid') || '';
  try {
    const response = await SendRequest('/api/events/summaries', 'GET');
    if (response.ok) {
      const data = await response.json();
      eventSummaries.value = data;
    } else {
      console.error('Error fetching event summaries:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching event summaries:', error);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.title {
  text-align: center;
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
  margin-top: 20px;
}

.dashboard-cards-container {
  display: flex;
  justify-content: center;
}

.dashboard-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.dashboard-card {
  flex: 1 1 calc(33.333% - 20px);
  box-sizing: border-box;
}
</style>