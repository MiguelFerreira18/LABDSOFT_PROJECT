<template>
    <ion-page>
        <ion-content :fullscreen="true" class="ion-padding">
            <ion-card v-if="event">
                <ion-card-header>
                    <ion-card-subtitle>{{ event.category }}</ion-card-subtitle>
                    <ion-card-title>{{ event.title }}</ion-card-title>
                    <ion-card-subtitle>Subscribers: {{ numberOfSubscribers }}</ion-card-subtitle>
                </ion-card-header>
                <ion-list>
                    <ion-item>
                        <ion-label>Description</ion-label>
                        {{ event.description }}
                    </ion-item>
                    <ion-item v-if="event.promotedUntil">
                        <ion-label color="tertiary">Promoted</ion-label>
                    </ion-item>
                    <ion-item>
                        <ion-label>Date</ion-label>
                        {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
                    </ion-item>
                    <ion-item>
                        <ion-label>Location</ion-label>
                        {{ event.location }}
                    </ion-item>
                    <ion-item v-if="creator">
                        <ion-label>Creator</ion-label>
                        {{ creator.name }}
                    </ion-item>
                    <ion-item>
                        <ion-label>Rate this event</ion-label>
                        <ion-range v-model="rating" min="0" max="5" step="1" snaps="true" ticks="true" pin="true" @ionChange="handleRatingChange">
                            <ion-icon slot="start" name="star-outline"></ion-icon>
                            <ion-icon slot="end" name="star"></ion-icon>
                        </ion-range>
                    </ion-item>
                </ion-list>
                <ion-button v-if="hasAttendedAndEventAsPassed()" @click="handleClaimReward" :disabled="hasAttended"
                    expand="block" fill="clear" shape="round" color="success">
                    Claim Reward
                </ion-button>
                <ion-button v-else-if="!isSubscribed" @click="handleSubscription" :disabled="hasAttended" expand="block"
                    fill="clear" shape="round" color="success">
                    Subscribe
                </ion-button>
                <ion-button v-else @click="handleUnsubscribe" expand="block" fill="clear" :disabled="hasAttended"
                    shape="round" color="danger">
                    Unsubscribe
                </ion-button>
                <ion-button v-if="isLoggedIn" @click="handlePromoteEvent" expand="block" fill="clear" shape="round"
                    color="primary">
                    Promote Event
                </ion-button>
            </ion-card>
            <ion-card v-else>
                <ion-card-header>
                    <ion-card-subtitle>Loading...</ion-card-subtitle>
                    <ion-card-title>Loading...</ion-card-title>
                </ion-card-header>
            </ion-card>
        </ion-content>
    </ion-page>
</template>


<script setup lang="ts">
import { IonPage, IonContent, IonCard, IonCardHeader, IonCardSubtitle, IonCardTitle, IonButton, IonList, IonItem, IonLabel } from '@ionic/vue';
import { formatDate } from '@/lib/dateFormatter';
import { SendRequest } from '@/lib/request';
import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import { toastController } from '@ionic/vue';

const event = ref<any>({});
const creator = ref<any>({});
const numberOfSubscribers = ref(0);
const isSubscribed = ref(false);
const hasAttended = ref(false);
const subbedEvent = ref<any>({})
const route = useRoute();
const isLoggedIn = computed(() => !!localStorage.getItem('token'));
const rating = ref<number | null>(null);

onMounted(async () => {
    await getCurrentEvent();
    await getNumberOfSubscribers();
    const { data, response } = await getIsSubscribed();
    if (response.ok && data && data.status === 'ATTENDED') {
        hasAttended.value = true;
        isSubscribed.value = true;
        subbedEvent.value = data;

    }
    else if (response.ok && data && data.status === 'SUBSCRIBED') {
        subbedEvent.value = data;
        isSubscribed.value = true;
    }
});

async function handleSubscription() {
    if (isSubscribed.value) return;
    const payload: Record<string, string> = {
        uuid: localStorage.getItem('uuid') || '',
        eventId: Array.isArray(route.params.id) ? route.params.id[0] : route.params.id || ''
    }
    const response = await SendRequest('/subscription/subscribe', 'POST', payload);
    if (response.ok) {
        const { data } = await response.json();
        isSubscribed.value = true;
        subbedEvent.value = data;
    }
}
async function handleClaimReward() {
    if (hasAttended.value) return;
    const response = await SendRequest(`/api/rewards/claim/${localStorage.getItem('uuid')}/${route.params.id}`, 'POST');
    if (response.ok) {
        hasAttended.value = true;
    }
}
function hasAttendedAndEventAsPassed() {
    if (!subbedEvent.value?.event) return false;
    const event = subbedEvent.value.event;

    if (event.endDate && Array.isArray(event.endDate)) {
        const [year, month, day] = event.endDate;
        const endDate = new Date(year, month - 1, day);
        const hasPassedEndDate = new Date() > endDate;

        return hasPassedEndDate;
    }

    return false;
}
async function handleUnsubscribe() {
    if (!isSubscribed.value && hasAttended.value) return;
    if (subbedEvent.value === undefined) {
        const { data, response } = await getIsSubscribed();
        if (response.ok && data.status === 'SUBSCRIBED') {
            subbedEvent.value = data;
        }
    }
    const response = await SendRequest(`/subscription/unsubscribe/${subbedEvent.value.id}`, 'POST');
    if (response.ok) {
        isSubscribed.value = false;
    }
}

async function getIsSubscribed() {
    const eventId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    const response = await SendRequest(`/subscription/isSubscribed/${localStorage.getItem('uuid')}/${eventId}`, 'GET');
    const { data } = await response.json();
    return { data, response };
}

async function handlePromoteEvent() {
    const userId = localStorage.getItem('uuid');
    const eventId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;

    if (!userId || !eventId) {
        console.error('Missing userId or eventId.');
        return;
    }

    const response = await SendRequest(
        `/api/events/${eventId}/promote?userId=${userId}`,
        'POST'
    );

    if (response.ok) {

        showToast('Event promoted successfully!', 'success');
    } else {

        showToast('You have already promoted an event.', 'danger');
    }
}

async function showToast(message: string, color: 'success' | 'danger') {
    const toast = await toastController.create({
        message: message,
        duration: 2500,
        position: 'top',
        color: color,
        icon: 'trophy-outline',
    });

    await toast.present();
}

async function getCurrentEvent() {
    const eventId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    const response = await SendRequest(`/api/events/${eventId}`, 'GET');
    const data = await response.json();
    event.value = data;
    creator.value = data.creator;
    console.log(data);

}
async function getNumberOfSubscribers() {
    const eventId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    const response = await SendRequest(`/subscription/event/count/${eventId}`, 'GET');
    const { data } = await response.json();
    numberOfSubscribers.value = data;
}

async function handleRatingChange(event: CustomEvent) {
  const newRating = event.detail.value;
  const payload: Record<string, string> = {
        uuid: localStorage.getItem('uuid') || '',
        eventId: Array.isArray(route.params.id) ? route.params.id[0] : route.params.id || '',
        rating: newRating.toString()
    }

  if(newRating != 0 && isSubscribed.value) {
    const response = await SendRequest(
        `/api/events/rate`,
        'POST',
        payload
    );
    if (response.ok) {
        showToast('Event rated successfully!', 'success');
    } else {
        showToast('You have already rated this event.', 'danger');
    }
  }
}

</script>

<style></style>