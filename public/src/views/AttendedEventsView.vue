<template>
    <ion-content>
        <ion-list v-for="event in events" :key="event.id">
            <ion-card @click="handlePagePush(event.id)">
                <ion-card-header>
                    <ion-card-subtitle>
                        <ion-list v-for="category in event.categories" :key="category">
                            <ion-item>
                                {{ category }}
                            </ion-item>
                        </ion-list>
                    </ion-card-subtitle>
                    <ion-card-title> {{ event.title }}</ion-card-title>
                </ion-card-header>
                <ion-card-content>
                    {{ event.description }}
                    <ion-item>
                        <ion-label>Location</ion-label>
                        <ion-note slot="end">{{ event.location }}</ion-note>
                    </ion-item>
                    <ion-item>
                        <ion-label>Start Date</ion-label>
                        <ion-note slot="end">{{ event.startDate }}</ion-note>
                    </ion-item>
                    <ion-item>
                        <ion-label>End Date</ion-label>
                        <ion-note slot="end">{{ event.endDate }}</ion-note>
                    </ion-item>
                    <ion-item>
                        <ion-label>Creator</ion-label>
                        <ion-note slot="end">{{ event.creator.name }}</ion-note>
                    </ion-item>
                    <ion-item>
                        <ion-label>Creator Email</ion-label>
                        <ion-note slot="end">{{ event.creator.email }}</ion-note>
                    </ion-item>
                </ion-card-content>
            </ion-card>
        </ion-list>
    </ion-content>

</template>



<script setup lang="ts">

import { SendRequest } from '@/lib/request';
import router from '@/router';
import { onMounted, ref } from 'vue';
interface Event {
    id: number;
    title: string;
    name: string;
    endDate: string;
    startDate: string;
    description: string;
    location: string;
    creator: any;
    categories: string[];
}
const events = ref<Event[]>([]);

onMounted(async () => {
    console.log('Component is mounted');
    const userId = localStorage.getItem('uuid') || '';
    const response = await SendRequest(`/subscription/subscriptions/attended/event/${userId}`, 'GET');
    const data = await response.json();
    if (response.ok && data) {
        console.log(data);
        events.value = data;
        console.log(data[0].categories);

        events.value = events.value.sort((a, b) => new Date(b.endDate).getTime() - new Date(a.endDate).getTime());
    }
});


async function handlePagePush(eventId: number) {
    console.log('Pushing to event detail page');
    router.push({ path: `/event/EventDetail/${eventId}` });
}

</script>
