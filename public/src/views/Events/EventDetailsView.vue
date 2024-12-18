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
    <ion-content :fullscreen="true">
      <ion-img
        src="https://placehold.co/600x400"
        alt="Placeholder image"
      ></ion-img>
      <div class="ion-padding">
        <header>
          <p>{{ event.category }}</p>
          <h2>{{ event.title }}</h2>
          <p>
            Subscribers:
            {{ numberOfSubscribers <= 0 ? 0 : numberOfSubscribers }}
            {{ event.limit == 0 ? '' : '/' + event.limit }}
          </p>
        </header>

        <section>
          <h3>Description</h3>
          <p>{{ event.description }}</p>
        </section>

        <section v-if="event.promotedUntil">
          <p><strong>Promoted</strong></p>
        </section>

        <section>
          <h3>Date</h3>
          <p>
            {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
          </p>
        </section>

        <section v-if="creator">
          <h3>Creator</h3>
          <p>{{ creator.name }}</p>
        </section>

        <section>
          <h3>Location</h3>
          <p>{{ event.location }}</p>
        </section>
      </div>

      <section map>
        <map-event
          v-if="event.latitude"
          :latitude="event.latitude"
          :longitude="event.longitude"
        />
        <p v-else>Map unavailable – location not specified.</p>
      </section>

      <section qrCode class="ion-padding" v-if="hasEventStarted()">
        <canvas
          v-if="!hasAttended && !isOwnerOfTheEvent()"
          ref="canvas"
        ></canvas>
        <ion-button
          v-else-if="isOwnerOfTheEvent()"
          @click="handleScanQrCode"
          expand="block"
          color="warning"
        >
          <ion-icon :icon="qrCode"></ion-icon>
          <span class="ion-margin-start">Scan QR Codes</span>
        </ion-button>
      </section>

      <section actions class="ion-padding">
        <ion-button
          v-if="hasLimitReached() && !isOwner()"
          :disabled="true"
          expand="block"
          color="danger"
        >
          No more subscriptions are being accepted
        </ion-button>

        <ion-button
          v-else-if="!isSubscribed && !isOwner()"
          @click="handleSubscription"
          :disabled="hasAttended"
          expand="block"
        >
          <ion-icon :icon="notifications"></ion-icon>
          <span class="ion-margin-start">Subscribe</span>
        </ion-button>

        <ion-button
          v-else-if="!isOwner()"
          @click="handleUnsubscribe"
          :disabled="hasAttended"
          expand="block"
          color="danger"
        >
          <ion-icon :icon="notificationsOff"></ion-icon>
          <span class="ion-margin-start">Unsubscribe</span>
        </ion-button>

        <ion-button
          v-if="isLoggedIn"
          @click="handlePromoteEvent"
          expand="block"
          fill="outline"
        >
          <ion-icon :icon="megaphone"></ion-icon>
          <span class="ion-margin-start">Promote Event</span>
        </ion-button>
      </section>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import {
  IonPage,
  IonContent,
  IonIcon,
  IonButton,
  IonButtons,
  IonBackButton,
} from '@ionic/vue';
import {
  megaphone,
  notifications,
  qrCode,
  notificationsOff,
} from 'ionicons/icons';
import { Capacitor } from '@capacitor/core';
import { CapacitorBarcodeScanner } from '@capacitor/barcode-scanner';
import { formatDate } from '@/lib/dateFormatter';
import { SendRequest } from '@/lib/request';
import { onMounted, ref, computed, nextTick } from 'vue';
import { useRoute } from 'vue-router';
import { toastController } from '@ionic/vue';
import QRCode from 'qrcode';
import MapEvent from '@/views/maps/MapEvent.vue';

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

<style>
header {
  margin-bottom: 16px;
}
header h2 {
  margin: 0;
  font-size: 1.5rem;
}
header p {
  margin: 4px 0;
  color: #666;
}
section {
  margin-bottom: 12px;
}
section h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}
section p {
  margin: 4px 0;
  color: #444;
}
ion-button {
  margin-top: 12px;
}
</style>
