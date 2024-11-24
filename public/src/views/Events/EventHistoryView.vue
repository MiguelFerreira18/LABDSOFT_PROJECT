<template>
    <ion-page>
    <ion-content class="ion-padding">
        <ion-button v-if="!isSeeingAttendedEvents" expand="block" fill="clear" shape="round" @click="toggleView">
            See Subscribed Events
        </ion-button>
        <ion-button v-else expand="block" fill="clear" shape="round" @click="toggleView">
            See Attended Events
        </ion-button>
        <ion-list v-if="events.length > 0">
            <!-- Events list -->
            <div class="event-grid">
                <ion-card v-for="event in events" :key="event.id" @click="handlePagePush(event.id)" class="event-card">
                    <!-- Category chips -->
                    <div class="ion-padding-horizontal ion-padding-top">
                        <ion-label>{{ event.category }}</ion-label>
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
</ion-page>
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
    category: string;
}
const events = ref<Event[]>([]);
const creator = ref<any>({});
const isSeeingAttendedEvents = ref(true);


async function fetchEvents(endpoint: any) {
    const userId = localStorage.getItem('uuid') || '';
    const response = await SendRequest(`${endpoint}${userId}`, 'GET');
    const data = await response.json();

    if (response.ok && data) {
        events.value = data;
        creator.value = data.creator;
        events.value = sortEventsByDate(events.value);
    }

}
function sortEventsByDate(eventsList: Event[]) {
    return eventsList.sort((a, b) =>
        new Date(b.endDate).getTime() - new Date(a.endDate).getTime()
    );
}

onMounted(async () => {
    const endpoint = isSeeingAttendedEvents.value
        ? '/subscription/attended/event/'
        : '/subscription/event/';
    await fetchEvents(endpoint);
});


async function handlePagePush(eventId: number) {
    router.push({ path: `/event/EventDetail/${eventId}` });
}
async function toggleView() {
    isSeeingAttendedEvents.value = !isSeeingAttendedEvents.value;
    const endpoint = isSeeingAttendedEvents.value
        ? '/subscription/attended/event/'
        : '/subscription/event/';
    await fetchEvents(endpoint);
}
function formatDateRange(startDate: string, endDate: string) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    return `${start.toDateString()} - ${end.toDateString()}`;
}

</script>
