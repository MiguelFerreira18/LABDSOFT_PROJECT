<template>
  <ion-page>
    <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Home Page</ion-title>
        </ion-toolbar>
      </ion-header>
    <ion-content :fullscreen="true" id="main-content">
      <RewardsOverview />
      <EventsView />
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
} from '@ionic/vue';
import ExploreContainer from '@/components/ExploreContainer.vue';
import RewardsOverview from '@/components/RewardsOverview.vue';
import EventsView from './Events/EventsView.vue';
</script>
