<template>
  <ion-menu menu-id="main-menu" side="end" content-id="main-content">
    <!-- <ion-menu side="end" content-id="event-history-content"> -->
    <ion-content>
      <ion-list>
        <ion-item href="/events/attended">
          <ion-label>Attended Events</ion-label>
        </ion-item>
        <ion-item href="/events/dashboard">
          <ion-label>Dashboard Events</ion-label>
        </ion-item>
      </ion-list>
    </ion-content>
  </ion-menu>
</template>

<script setup lang="ts">
import { IonMenu, IonContent } from '@ionic/vue';
</script>
