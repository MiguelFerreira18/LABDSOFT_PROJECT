<template>
    <MenuComponent />
    <ion-header>
        <ion-toolbar>
        <ion-title>{{ title }}</ion-title>
        <ion-buttons slot="end">
            <ion-menu-button></ion-menu-button>
        </ion-buttons>
        </ion-toolbar>
    </ion-header>
</template>

<script setup lang="ts">
import { IonHeader, IonToolbar, IonTitle, IonButtons, IonMenuButton } from '@ionic/vue';
import MenuComponent from '@/components/MenuComponent.vue';

// Define props
defineProps({
    title: {
        type: String,
        required: true,
    },
});
</script>
  