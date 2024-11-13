<template>
    <ion-card>
        <ion-card-header>
            <ion-card-subtitle>AnyEvent</ion-card-subtitle>
            <ion-card-title>SomeAnyEvent</ion-card-title>
        </ion-card-header>
        <ion-list>
            <ion-item>
                <ion-label>AnyCOntent Here</ion-label>
                AnyItemCOntent
            </ion-item>
            <ion-item>
                <ion-label>AnyCOntent2 Here</ion-label>
                AnyItemCOntent2
            </ion-item>
            <ion-item>
                <ion-label>AnyCOntent3 Here</ion-label>
                AnyItemCOntent3
            </ion-item>
        </ion-list>
        <ion-button v-if="!isSubscribed" @click="handleSubscription" :disabled="hasAttended" expand="block" fill="clear"
            shape="round" color="success">
            Subscribe
        </ion-button>
        <ion-button v-else @click="handleUnsubscribe" expand="block" fill="clear" :disabled="hasAttended" shape="round"
            color="danger">
            Unsubscribe
        </ion-button>
    </ion-card>

</template>


<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const isSubscribed = ref(false);
const hasAttended = ref(false);
const subbedEvent = ref({})
const route = useRoute();

onMounted(async () => {
    const { data, response } = await getIsSubscribed();
    if (response.ok && data.status === 'ATTENDED') {
        hasAttended.value = true;
        isSubscribed.value = true;
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
    const response = await SendRequest('/subscription/subscribe', 'POST', payload, [], localStorage.getItem('jwt') || '');
    if (response.ok) {
        const { data } = await response.json();
        isSubscribed.value = true;
        subbedEvent.value = data;
    }
}
async function handleUnsubscribe() {
    if (!isSubscribed.value && hasAttended.value) return;
    if (subbedEvent.value === undefined) {
        const { data, response } = await getIsSubscribed();
        if (response.ok && data.status === 'SUBSCRIBED') {
            subbedEvent.value = data;
        }
    }
    const response = await SendRequest(`/subscription/unsubscribe/${subbedEvent.value.id}`, 'POST', {}, [], localStorage.getItem('jwt') || '');
    if (response.ok) {
        isSubscribed.value = false;
    }
}

async function getIsSubscribed() {
    const eventId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
    const response = await SendRequest(`/subscription/isSubscribed`, 'POST',
        {
            uuid: localStorage.getItem('uuid') || '',
            eventId: eventId || ''
        },
        [],
        localStorage.getItem('jwt') || '');
    const { data } = await response.json();
    return { data, response };
}
</script>

<style></style>