<template>
  <ion-page>
    <HeaderComponent title="Rewards Dashboard" />
    <ion-content :fullscreen="true" class="ion-padding">
      <!-- Rewards Summary Section -->
      <ion-card>
        <ion-card-header>
          <ion-card-title>Rewards Summary</ion-card-title>
        </ion-card-header>
        <ion-card-content>
          <ion-grid>
            <ion-row>
              <ion-col size="6" class="ion-text-center">
                <ion-icon name="star" size="large" color="primary"></ion-icon>
                <p><strong>Points</strong></p>
                <p>{{ rewards.points }}</p>
              </ion-col>
              <ion-col size="6" class="ion-text-center">
                <ion-icon
                  name="calendar"
                  size="large"
                  color="success"
                ></ion-icon>
                <p><strong>Daily Streak</strong></p>
                <p>{{ rewards.dailyStreakDays }} days</p>
              </ion-col>
            </ion-row>
          </ion-grid>
        </ion-card-content>
      </ion-card>

      <!-- Milestones Section -->
      <ion-card>
        <ion-card-header>
          <ion-card-title>Milestones</ion-card-title>
        </ion-card-header>
        <ion-card-content>
          <ion-list>
            <ion-item v-for="milestone in milestones" :key="milestone.id">
              <ion-label>
                <h2>{{ milestone.name }}</h2>
                <p>{{ milestone.description }}</p>
              </ion-label>
            </ion-item>
          </ion-list>
        </ion-card-content>
      </ion-card>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { ref, onMounted } from 'vue';
import HeaderComponent from '@/components/common/HeaderComponent.vue';
import { getAllMilestones } from '@/lib/badgesService';
import {
  IonPage,
  IonContent,
  IonGrid,
  IonRow,
  IonCol,
  IonCardTitle,
  IonIcon,
  IonCard,
  IonCardHeader,
  IonCardContent,
  IonList,
  IonItem,
  IonLabel,
} from '@ionic/vue';

// Rewards data interface
interface RewardsResponse {
  points: number;
  dailyStreakDays: number;
  pointsEarned: number;
}

// Milestone data interface
interface Milestone {
  id: string;
  name: string;
  description: string;
}

interface MilestonesResponse {
  milestones: Milestone[];
}

// Reactive state
const rewards = ref<RewardsResponse>({
  points: 0,
  dailyStreakDays: 0,
  pointsEarned: 0,
});

const milestones = ref<Milestone[]>([]);

// Fetch rewards data
onMounted(async () => {
  try {
    const userId = localStorage.getItem('userId') || '';
    const fetchedRewards = await dailyRewards({ id: userId });
    rewards.value = fetchedRewards;

    const fetchedMilestones = await getAllMilestones(); // Fetch milestones from milestoneService
    milestones.value = fetchedMilestones.milestones;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});

// Function to fetch daily rewards
async function dailyRewards(response: {
  id: string;
}): Promise<RewardsResponse> {
  try {
    const res = await SendRequest(
      `/api/rewards/${response.id}/daily`,
      'POST',
      {},
    );
    const rewards: RewardsResponse = await res.json();
    return rewards as RewardsResponse;
  } catch (error) {
    console.error('Error in dailyRewards function:', error);
    throw error;
  }
}
</script>

<style scoped>
ion-card-title {
  text-align: center;
  font-size: 1.2rem;
}

ion-col p {
  margin: 0;
}
</style>
