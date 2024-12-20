<template>
  <ion-page>
    <header-component title="Home Page" />
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
import RewardsOverview from '@/components/RewardsOverview.vue';
import EventsView from './Events/EventsView.vue';
import HeaderComponent from '@/components/common/HeaderComponent.vue';
</script>
