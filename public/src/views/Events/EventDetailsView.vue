<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-buttons slot="start">
          <ion-back-button defaultHref="/events"></ion-back-button>
        </ion-buttons>
        <ion-title>Event Details</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true" class="ion-padding">
      <ion-card v-if="event">
        <ion-card-header>
          <ion-card-subtitle>{{ event.category }}</ion-card-subtitle>
          <ion-card-title>{{ event.title }}</ion-card-title>
          <ion-card-subtitle>
            Subscribers: {{ numberOfSubscribers <= 0 ? 0 : numberOfSubscribers
            }}{{ event.limit == 0 ? '' : '/' + event.limit }}
          </ion-card-subtitle>
        </ion-card-header>
        <ion-list>
          <ion-item v-if="creatorRating !== null">
            <ion-label>Creator Rating</ion-label>
            <div class="star-rating">
              <span v-for="star in 5" :key="star" class="star" :class="{ filled: star <= creatorRating }">&#9733;</span>
            </div>
          </ion-item>
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
            <ion-label>Event Rating</ion-label>
            <div class="star-rating">
              <span v-for="star in 5" :key="star" class="star" :class="{ filled: star <= event.rating }">&#9733;</span>
            </div>
          </ion-item>
          <ion-item v-if="isSubscribed && hasEventStarted()">
            <ion-label>Rate this event</ion-label>
            <div class="star-rating">
              <span v-for="star in 5" :key="star" class="star" @click="rateEvent(star)" :class="{ filled: star <= userRating }">&#9733;</span>
            </div>
          </ion-item>
        </ion-list>
        <ion-row class="ion-justify-content-center">
          <ion-col size="auto">
            <canvas
              v-if="hasEventStarted() && !hasAttended && !isOwnerOfTheEvent()"
              ref="canvas"
            ></canvas>
            <ion-button
              @click="handleScanQrCode"
              v-else-if="hasEventStarted() && isOwnerOfTheEvent()"
              expand="block"
              fill="clear"
              shape="round"
              color="warning"
            >
              Scan QR Codes
            </ion-button>
          </ion-col>
        </ion-row>
        <ion-button
          v-if="hasLimitReached() && !isOwner()"
          :disabled="true"
          expand="block"
          fill="clear"
          shape="round"
          color="danger"
        >
          No more subscriptions are being accepted
        </ion-button>
        <ion-button
          v-else-if="!isSubscribed && !isOwner()"
          @click="handleSubscription"
          :disabled="hasAttended"
          expand="block"
          fill="clear"
          shape="round"
          color="success"
        >
          Subscribe
        </ion-button>
        <ion-button
          v-else-if="!isOwner()"
          @click="handleUnsubscribe"
          expand="block"
          fill="clear"
          :disabled="hasAttended"
          shape="round"
          color="danger"
        >
          Unsubscribe
        </ion-button>
        <ion-button
          v-if="isLoggedIn"
          @click="handlePromoteEvent"
          expand="block"
          fill="clear"
          shape="round"
          color="primary"
        >
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
import {
  IonPage,
  IonContent,
  IonCard,
  IonCardHeader,
  IonCardSubtitle,
  IonCardTitle,
  IonButton,
  IonList,
  IonItem,
  IonLabel,
  IonButtons,
  IonBackButton,
} from '@ionic/vue';
import { Capacitor } from '@capacitor/core';
import { CapacitorBarcodeScanner } from '@capacitor/barcode-scanner';
import { formatDate } from '@/lib/dateFormatter';
import { SendRequest } from '@/lib/request';
import { onMounted, ref, computed, nextTick } from 'vue';
import { useRoute } from 'vue-router';
import { toastController } from '@ionic/vue';
import QRCode from 'qrcode';

const canvas = ref<HTMLCanvasElement | null>(null);
const event = ref<any>({});
const creator = ref<any>({});
const numberOfSubscribers = ref(0);
const isSubscribed = ref(false);
const hasAttended = ref(false);
const subbedEvent = ref<any>({});
const route = useRoute();
const isLoggedIn = computed(() => !!localStorage.getItem('token'));
const scanResult = ref<string | null>(null);
const userRating = ref(0);
const hasRated = ref(false);
const creatorRating = ref<number | null>(null);

onMounted(async () => {
  await getCurrentEvent();
  await getNumberOfSubscribers();
  const { data, response } = await getIsSubscribed();
  if (response.ok && data && data.status === 'ATTENDED') {
    hasAttended.value = true;
    isSubscribed.value = true;
    subbedEvent.value = data;
  } else if (response.ok && data && data.status === 'SUBSCRIBED') {
    subbedEvent.value = data;
    isSubscribed.value = true;

    getQrCodeIfEventHasStarted(subbedEvent.value.qrdata);
  }
  if (data.rate) {
    userRating.value = data.rate;
    hasRated.value = true;
  }
  if (data.event.rating) {
    event.value.rating = data.event.rating;
  }
  await calculateCreatorRating(data.event.creator.id);
});

async function calculateCreatorRating(creatorId: string) {
  try {
    const response = await SendRequest(`/api/events?creatorId=${creatorId}`, 'GET');
    const events: Event[] = await response.json();
    console.log('Events:', events);
    const pastEvents = events.filter(event => {
      const endDate = new Date(event.endDate[0], event.endDate[1] - 1, event.endDate[2]);
      return endDate < new Date();
    });
    console.log('Past events:', pastEvents);
    const totalRating = pastEvents.reduce((sum, event) => sum + event.rating, 0);
    const averageRating = pastEvents.length > 0 ? totalRating / pastEvents.length : 0;
    creatorRating.value = averageRating;
  } catch (error) {
    console.error('Erro ao calcular o rating do criador:', error);
    creatorRating.value = null;
  }
}

function hasEventStarted() {
  if (!event.value || !event.value.startDate || !Array.isArray(event.value.startDate)) return false;

  const startDate = new Date(event.value.startDate[0], event.value.startDate[1] - 1, event.value.startDate[2]);
  return startDate <= new Date();
}

function isOwnerOfTheEvent() {
  return creator.value.id === localStorage.getItem('uuid');
}

async function handleSubscription() {
  if (isSubscribed.value) return;
  const payload: Record<string, string> = {
    uuid: localStorage.getItem('uuid') || '',
    eventId: Array.isArray(route.params.id)
      ? route.params.id[0]
      : route.params.id || '',
  };
  const response = await SendRequest(
    '/subscription/subscribe',
    'POST',
    payload,
  );
  if (response.ok) {
    const { data } = await response.json();
    isSubscribed.value = true;
    subbedEvent.value = data;
  }
}

async function handleClaimReward() {
  if (hasAttended.value) return;
  const response = await SendRequest(
    `/api/rewards/claim/${localStorage.getItem('uuid')}/${route.params.id}`,
    'POST',
  );
  if (response.ok) {
    hasAttended.value = true;
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
  const response = await SendRequest(
    `/subscription/unsubscribe/${subbedEvent.value.id}`,
    'POST',
  );
  if (response.ok) {
    isSubscribed.value = false;
  }
}

async function getIsSubscribed() {
  const eventId = Array.isArray(route.params.id)
    ? route.params.id[0]
    : route.params.id;
  const response = await SendRequest(
    `/subscription/isSubscribed/${localStorage.getItem('uuid')}/${eventId}`,
    'GET',
  );
  const { data } = await response.json();
  return { data, response };
}

async function handlePromoteEvent() {
  const userId = localStorage.getItem('uuid');
  const eventId = Array.isArray(route.params.id)
    ? route.params.id[0]
    : route.params.id;

  if (!userId || !eventId) {
    console.error('Missing userId or eventId.');
    return;
  }

  const response = await SendRequest(
    `/api/events/${eventId}/promote?userId=${userId}`,
    'POST',
  );

  if (response.ok) {
    showToast('Event promoted successfully!', 'success');
  } else {
    showToast('You have already promoted an event.', 'danger');
  }
}

async function handleScanQrCode() {
  try {
    const isAvailable = Capacitor.isPluginAvailable('Camera');
    if (!isAvailable) {
      alert('Camera plugin not available');
      return;
    }
    const result = await CapacitorBarcodeScanner.scanBarcode({
      hint: 0 || 17,
      cameraDirection: 1,
    });

    if (result && result.ScanResult) {
      scanResult.value = result.ScanResult;
      const response = await SendRequest(`${scanResult.value}`, 'POST');
      if (response.ok) {
        showToast('QR Code scanned successfully!', 'success');
      } else {
        showToast('Failed to scan QR Code', 'danger');
      }
    } else {
      alert('No barcode detected');
    }
  } catch (error) {
    console.error('Barcode scan error:', error);
    alert('Failed to scan barcode');
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
  const eventId = Array.isArray(route.params.id)
    ? route.params.id[0]
    : route.params.id;
  const response = await SendRequest(`/api/events/${eventId}`, 'GET');
  const data = await response.json();
  event.value = data;
  creator.value = data.creator;
  if (data.rating) {
    event.value.rating = data.rating;
  }
}

async function getNumberOfSubscribers() {
  const eventId = Array.isArray(route.params.id)
    ? route.params.id[0]
    : route.params.id;
  const response = await SendRequest(
    `/subscription/event/count/${eventId}`,
    'GET',
  );
  const { data } = await response.json();
  numberOfSubscribers.value = data;
}

async function getQrCodeIfEventHasStarted(url: string) {
  if (!hasEventStarted() || !url) return;

  await nextTick();

  if (!canvas.value) {
    console.warn('Canvas still not available');
    return;
  }

  const fullURL = `${url}`;

  QRCode.toCanvas(canvas.value, fullURL, function (error: any) {
    if (error) console.error(error);
  });
}

function hasLimitReached() {
  if (numberOfSubscribers.value >= event.value.limit && event.value.limit > 0) {
    return true;
  } else {
    return false;
  }
}

function isOwner() {
  return creator.value.id === localStorage.getItem('uuid');
}

async function rateEvent(star: number) {
  if (!isSubscribed.value) {
    showToast('You must be subscribed to rate the event!', 'danger');
    return;
  }
  if (!hasEventStarted()) {
    showToast('You can only rate events that have started!', 'danger');
    return;
  }
  if (hasRated.value) {
    showToast('You have already rated this event!', 'danger');
    return;
  }

  const payload: Record<string, string> = {
    uuid: localStorage.getItem('uuid') || '',
    eventId: Array.isArray(route.params.id) ? route.params.id[0] : route.params.id || '',
    rating: star.toString(),
  };

  const response = await SendRequest(`/api/events/rate`, 'POST', payload);
  if (response.ok) {
    showToast('Event rated successfully!', 'success');
    userRating.value = star;
    hasRated.value = true;
  } else {
    showToast('Failed to rate the event', 'danger');
  }
}
</script>

<style scoped>
.star-rating {
  display: flex;
  align-items: center;
}

.star {
  font-size: 24px;
  cursor: pointer;
  color: #ccc;
}

.star.filled {
  color: #f39c12;
}
</style>