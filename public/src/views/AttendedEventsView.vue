<template>
    <ion-content class="ion-padding">
        <ion-list v-if="events.length > 0">


            <!-- Events list -->
            <div class="event-grid">
                <ion-card v-for="event in events" :key="event.id" @click="handlePagePush(event.id)" class="event-card">
                    <!-- Category chips -->
                    <div class="ion-padding-horizontal ion-padding-top">
                        <ion-chip v-for="category in event.categories" :key="category" color="primary" outline>
                            <ion-label>{{ category }}</ion-label>
                        </ion-chip>
                    </div>

                    <ion-card-header>
                        <ion-card-title class="ion-padding-bottom">{{ event.title }}</ion-card-title>
                    </ion-card-header>

                    <ion-card-content>
                        <!-- Description with truncation -->
                        <p class="event-description">{{ event.description }}</p>

                        <!-- Event details with icons -->
                        <ion-list lines="none">
                            <ion-item>
                                <ion-icon :icon="locationOutline" slot="start"></ion-icon>
                                <ion-label>
                                    <p>Location</p>
                                    <h3>{{ event.location }}</h3>
                                </ion-label>
                            </ion-item>

                            <ion-item>
                                <ion-icon :icon="calendarOutline" slot="start"></ion-icon>
                                <ion-label>
                                    <p>Date</p>
                                    <h3>{{ formatDateRange(event.startDate, event.endDate) }}</h3>
                                </ion-label>
                            </ion-item>

                            <ion-item>
                                <ion-icon :icon="personOutline" slot="start"></ion-icon>
                                <ion-label>
                                    <p>Creator</p>
                                    <h3>{{ event.creator.name }}</h3>
                                    <p>{{ event.creator.email }}</p>
                                </ion-label>
                            </ion-item>
                        </ion-list>
                    </ion-card-content>
                </ion-card>
            </div>
        </ion-list>

        <!-- Empty state -->
        <div v-else class="empty-state ion-text-center ion-padding">
            <ion-icon :icon="calendarClearOutline" class="empty-state-icon"></ion-icon>
            <h2>No Events Yet</h2>
            <p>Your upcoming events will appear here</p>
        </div>
    </ion-content>
</template>



<script setup lang="ts">

import { SendRequest } from '@/lib/request';
import router from '@/router';
import { onMounted, ref } from 'vue';
import { calendarOutline, locationOutline, personOutline, calendarClearOutline } from 'ionicons/icons';
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
    const response = await SendRequest(`/subscription/attended/event/${userId}`, 'GET');
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
function formatDateRange(startDate: string, endDate: string) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    return `${start.toDateString()} - ${end.toDateString()}`;
}

</script>
