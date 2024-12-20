<template>
  <ion-content class="ion-padding">
    <ion-item>
      <ion-label>
        <p class="tag">{{ event.category }}</p>
        <h2>{{ event.title }}</h2>
        <p>
          Dates: {{ formatDate(event.startDate) }} -
          {{ formatDate(event.endDate) }}
        </p>
        <ion-button @click="goToEventDetail">View Event Details</ion-button>
      </ion-label>
    </ion-item>
  </ion-content>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { modalController } from '@ionic/vue';
import { Event } from '@/domain/Event';
import { formatDate } from '@/lib/dateFormatter';

const props = defineProps<{ event: Event }>();
const router = useRouter();

const goToEventDetail = async () => {
  await modalController.dismiss();
  router.push(`/event/EventDetail/${props.event.id}`);
};
</script>

<style scoped>
.tag {
  align-items: center;
  border-radius: 0.375rem;
  background-color: #f1f3f5;
  display: inline-flex;
  font-size: 0.6rem;
  height: 2em;
  justify-content: center;
  line-height: 1.5;
  padding-left: 0.75em;
  padding-right: 0.75em;
  white-space: nowrap;
  color: #343a40;
}
</style>
