<template>
  <ion-page id="menu-footer">
    <ion-tabs>
      <ion-router-outlet></ion-router-outlet>
      <ion-tab-bar slot="bottom">
        <ion-tab-button tab="home" href="/home">
          <ion-icon aria-hidden="true" :icon="homeOutline" />
          <ion-label>Home</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="search" href="/search">
          <ion-icon aria-hidden="true" :icon="searchOutline" />
          <ion-label>Search</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="add-event" href="/events/create">
          <ion-icon aria-hidden="true" :icon="addOutline" />
          <ion-label>Add Event</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="menu" href="/rewards">
          <ion-icon aria-hidden="true" :icon="trophyOutline" />
          <ion-label>Rewards</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="profile" href="/profile">
          <ion-icon aria-hidden="true" :icon="personCircleOutline" />
          <ion-label>Profile</ion-label>
        </ion-tab-button>
      </ion-tab-bar>
    </ion-tabs>
  </ion-page>
</template>

<script setup lang="ts">
import {
  IonTabBar,
  IonTabButton,
  IonTabs,
  IonLabel,
  IonIcon,
  IonPage,
  IonRouterOutlet,
} from '@ionic/vue';
import {
  addOutline,
  personCircleOutline,
  homeOutline,
  searchOutline,
  trophyOutline,
} from 'ionicons/icons';
</script>
