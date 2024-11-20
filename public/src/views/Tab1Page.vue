<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Tab 1</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Tab 1</ion-title>
        </ion-toolbar>
      </ion-header>

      <ExploreContainer name="Tab 1 page" />
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonButton, toastController } from '@ionic/vue';
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/vue';
import ExploreContainer from '@/components/ExploreContainer.vue';
import { ref, onMounted } from 'vue';
import { SendRequest } from '@/lib/request';

const userId = ref<string | null>(null);

interface RewardsResponse {
    points: number;
    dailyStreakDays: number;
    pointsEarned: number;
}

onMounted(async () => {
  userId.value = localStorage.getItem('userId');
  console.log(userId.value);

  if (userId.value) {
    try {
      const rewards = await dailyRewards({ id: userId.value });

      if(rewards.pointsEarned > 0) {
        await presentToast('top',`You earned ${rewards.pointsEarned} points!`);
      }else{
        await presentToast('bottom',`You have already claimed your daily rewards!`);
      }
      
    } catch (error) {
      console.error('Error fetching daily rewards:', error);
    }
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

async function presentToast(position: 'top' | 'middle' | 'bottom', message: string) {
  const toast = await toastController.create({
    message: message,
    duration: 1500,
    position: position,
  });

  await toast.present();
}

</script>
