<template>
    <ion-card>
        <ion-card-header>
            <ion-card-subtitle>AnyEvent</ion-card-subtitle>
            <ion-card-title>SomeAnyEvent</ion-card-title>
        </ion-card-header>
        <ion-list>
            <ion-item>
                <ion-label>AnyContent Here</ion-label>
                AnyItemContent
            </ion-item>
            <ion-item>
                <ion-label>AnyContent2 Here</ion-label>
                AnyItemContent2
            </ion-item>
            <ion-item>
                <ion-label>AnyContent3 Here</ion-label>
                AnyItemContent3
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
        <ion-button v-else @click="handleUnsubscribe" expand="block" fill="clear" :disabled="hasAttended" shape="round"
            color="danger">
            Unsubscribe
        </ion-button>
        <ion-button v-if="isLoggedIn" @click="handlePromoteEvent" expand="block" fill="clear"
            shape="round" color="primary">
            Promote Event
        </ion-button>
    </ion-card>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import { toastController } from '@ionic/vue';

const isSubscribed = ref(false);
const hasAttended = ref(false);
const subbedEvent = ref<any>({})
const route = useRoute();
const isLoggedIn = computed(() => !!localStorage.getItem('token'));

onMounted(async () => {
    const { data, response } = await getIsSubscribed();
    if (response.ok && data.status === 'ATTENDED') {
        hasAttended.value = true;
        isSubscribed.value = true;
        subbedEvent.value = data;
    }
    else if (response.ok && data.status === 'SUBSCRIBED') {
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

        showToast('You have already promoted this event.', 'danger');
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
</script>

<style>

</style>
