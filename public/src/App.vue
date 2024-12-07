<template>
  <ion-app>
    <ion-router-outlet :id="mainContentId" />
    <MenuComponent />
  </ion-app>
</template>

<script setup lang="ts">
import { IonApp, IonRouterOutlet } from '@ionic/vue';
import MenuComponent from './components/common/MenuComponent.vue';

const mainContentId = 'main-content';
</script>
