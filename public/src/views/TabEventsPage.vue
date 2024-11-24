<template>
  <ion-page>
    <ion-header>
    </ion-header>
    <ion-content :fullscreen="true">
      <ion-header collapse="condense">
      </ion-header>
      <ion-list>
        <ion-item button @click="navigateTo('/events')">All Events</ion-item>
        <ion-item button @click="navigateTo('/events/create')">Create Event</ion-item>
        <ion-item button @click="navigateTo('/events/dashboard')">Dashboard Events</ion-item>
        <ion-item button @click="navigateTo('/events/attended')">Events History</ion-item>
      </ion-list>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonPage, IonHeader, IonContent, IonList, IonItem } from '@ionic/vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateTo = (path: string) => {
  router.push(path);
};
</script>