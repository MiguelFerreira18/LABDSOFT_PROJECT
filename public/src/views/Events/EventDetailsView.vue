<template>
  <ion-page>
    <ion-content :fullscreen="true" class="ion-padding">
      <ion-card v-if="event">
        <ion-card-header>
          <ion-card-subtitle>{{ event.category }}</ion-card-subtitle>
          <ion-card-title>{{ event.title }}</ion-card-title>
          <ion-card-subtitle
            >Subscribers: {{ numberOfSubscribers <= 0 ? 0 : numberOfSubscribers
            }}{{ event.limit == 0 ? '' : '/' + event.limit }}</ion-card-subtitle
          >
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
});

function hasEventStarted() {
  if (!subbedEvent.value?.event) return false;
  const event = subbedEvent.value.event;

  if (event.startDate && Array.isArray(event.startDate)) {
    const [year, month, day] = event.startDate;
    const startDate = new Date(year, month - 1, day);
    return startDate <= new Date();
  }
  return false;
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
</script>

<style></style>
