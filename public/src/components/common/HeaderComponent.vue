<template>
    <ion-header>
        <ion-toolbar>
        <ion-title>{{ title }}</ion-title>
        </ion-toolbar>
    </ion-header>
</template>

<script setup lang="ts">
import { IonHeader, IonToolbar, IonTitle } from '@ionic/vue';

// Define props
defineProps({
    title: {
        type: String,
        required: true,
    }
});
</script>
  