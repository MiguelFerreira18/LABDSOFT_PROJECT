<template>
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
                <ion-icon name="calendar" size="large" color="success"></ion-icon>
                <p><strong>Daily Streak</strong></p>
                <p>{{ rewards.dailyStreakDays }} days</p>
            </ion-col>
            </ion-row>
        </ion-grid>
        </ion-card-content>
    </ion-card>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { IonCard, IonCardHeader, IonCardContent, IonCardTitle, IonGrid, IonRow, IonCol, IonIcon } from '@ionic/vue';
import { ref, onMounted } from 'vue';

interface RewardsResponse {
    points: number;
    dailyStreakDays: number;
    pointsEarned: number;
}

const rewards = ref<RewardsResponse>({
  points: 0,
  dailyStreakDays: 0,
  pointsEarned: 0,
});

onMounted(async () => {
    try {
        const userId = localStorage.getItem('userId') || '';
        const fetchedRewards = await dailyRewards({ id: userId });
        rewards.value = fetchedRewards;
    } catch (error) {
        console.error('Error fetching daily rewards:', error);
    }
});

async function dailyRewards(response: { id: string }): Promise<RewardsResponse> {
    
    try {
        const res = await SendRequest(`/api/rewards/${response.id}/daily`, 'POST', {});

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