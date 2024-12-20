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
      <div class="ion-padding">
        <header>
          <p class="tag">{{ event.category }}</p>
          <h2>{{ event.title }}</h2>
          <p>
            Subscribers:
            {{ numberOfSubscribers <= 0 ? 0 : numberOfSubscribers }}
            {{ event.limit == 0 ? '' : '/' + event.limit }}
          </p>
          <section v-if="creator">
            <ion-item lines="none" class="ion-no-padding">
              <ion-avatar aria-hidden="true" slot="start">
                <img
                  alt="create-avatar"
                  src="https://ionicframework.com/docs/img/demos/avatar.svg"
                />
              </ion-avatar>
              <ion-label>
                <h6>{{ creator.name }}</h6>
                <div class="star-rating-author" v-if="creatorRating !== null">
                  <span
                    v-for="star in 5"
                    :key="star"
                    class="star"
                    :class="{ filled: star <= creatorRating }"
                    >&#9733;</span
                  >
                </div>
              </ion-label>
            </ion-item>
          </section>
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
          <p>{{ formattedStartDate }} - {{ formattedEndDate }}</p>
        </section>

        <section>
          <h3>Location</h3>
          <p>{{ event.location }}</p>
        </section>

        <section Rating>
          <div class="star-rating">
            <span
              v-for="star in 5"
              :key="star"
              class="star"
              :class="{ filled: star <= event.rating }"
              >&#9733;</span
            >
          </div>

          <ion-item lines="none" v-if="isSubscribed && hasEventStarted()">
            <ion-label>Rate this event</ion-label>
            <div class="star-rating">
              <span
                v-for="star in 5"
                :key="star"
                class="star"
                @click="rateEvent(star)"
                :class="{ filled: star <= userRating }"
                >&#9733;</span
              >
            </div>
          </ion-item>
        </section>
        <section v-if="event.images && event.images.length > 0">
          <h3>Event related Images:</h3>
          <ion-row class="ion-padding">
            <ion-col v-for="image in event.images" :key="image.id" size="6">
              <ion-img
                :src="image.url"
                alt="Event Image"
                @click="openImageModal(image.url)"
                style="width: 100%; height: 200px; object-fit: cover"
              >
              </ion-img>
            </ion-col>
          </ion-row>

          <ion-modal
            :is-open="isModalOpen"
            @did-dismiss="isModalOpen = false"
            backdrop-dismiss="true"
          >
            <div class="modal-content">
              <div class="modal-background" @click="closeModal">
                <ion-img
                  :src="modalImageUrl"
                  alt="Expanded Image"
                  class="modal-image"
                />
              </div>
              <div class="modal-text" @click="closeModal">Back</div>
            </div>
          </ion-modal>
        </section>
      </div>

      <section map>
        <map-event
          v-if="event.latitude"
          :latitude="event.latitude"
          :longitude="event.longitude"
        />
        <p class="ion-padding" v-else>
          Map unavailable – location not specified.
        </p>
      </section>

      <input
        ref="fileInput"
        type="file"
        multiple
        @change="onFileSelected"
        style="display: none"
      />

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

        <ion-button
          v-if="isOwnerOfTheEvent()"
          @click="handleImageUpload"
          expand="block"
          fill="outline"
          color="primary"
        >
          <ion-icon :icon="images"></ion-icon>
          <span class="ion-margin-start">Add Images</span>
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
  images,
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
import { SendFormData } from '@/lib/sendFormData';

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
const isModalOpen = ref(false);
const modalImageUrl = ref('');
const fileInput = ref<HTMLInputElement | null>(null);

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
    const response = await SendRequest(
      `/api/events?creatorId=${creatorId}`,
      'GET',
    );
    const events: any = await response.json();
    const pastEvents = events.filter((event: any) => {
      const endDate = new Date(
        event.endDate[0],
        event.endDate[1] - 1,
        event.endDate[2],
      );
      return endDate < new Date();
    });
    const totalRating = pastEvents.reduce(
      (sum: any, event: any) => sum + event.rating,
      0,
    );
    const averageRating =
      pastEvents.length > 0 ? totalRating / pastEvents.length : 0;
    creatorRating.value = averageRating;
    console.log('Creator rating:', creatorRating.value);
  } catch (error) {
    creatorRating.value = null;
  }
}

function hasEventStarted() {
  if (
    !event.value ||
    !event.value.startDate ||
    !Array.isArray(event.value.startDate)
  )
    return false;

  const currentEvent = event.value;
  if (currentEvent.startDate && Array.isArray(currentEvent.startDate)) {
    const [year, month, day] = currentEvent.startDate;
    const startDate = new Date(year, month - 1, day);

    return startDate <= new Date();
  }
  return false;
}

async function handleImageUpload() {
  await nextTick();
  console.log(fileInput.value);
  if (fileInput.value) {
    // Verifique se a referência está definida
    fileInput.value.click(); // Dispara o clique no input de arquivo
  }
}

async function onFileSelected(event: Event) {
  const fileInput = event.target as HTMLInputElement;
  if (fileInput?.files) {
    const files = Array.from(fileInput.files);
    const formData = new FormData();

    files.forEach((file) => {
      formData.append('image', file); // Adiciona os arquivos ao FormData
    });

    const eventId = Array.isArray(route.params.id)
      ? route.params.id[0]
      : route.params.id;

    // Usa a função SendFormData que foi criada para enviar o FormData
    const response = await SendFormData(
      `/api/events/${eventId}/images`,
      'POST',
      formData,
    );

    if (response.ok) {
      showToast('Image added sucessfully!', 'success');
      await getCurrentEvent(); // Atualiza as imagens
    } else {
      showToast('Error adding image!', 'danger');
    }
  }
}

function isOwnerOfTheEvent() {
  return creator.value.id === localStorage.getItem('uuid');
}

function openImageModal(imageUrl: string) {
  modalImageUrl.value = imageUrl;
  isModalOpen.value = true;
}

function closeModal() {
  isModalOpen.value = false;
}

const formattedStartDate = computed(() => {
  if (event.value.startDate && Array.isArray(event.value.startDate)) {
    return formatDate(event.value.startDate); // Usando a função formatDate
  }
  return 'Invalid date'; // Retorna um valor padrão caso não exista data
});

const formattedEndDate = computed(() => {
  if (event.value.endDate && Array.isArray(event.value.endDate)) {
    return formatDate(event.value.endDate); // Usando a função formatDate
  }
  return 'Invalid date'; // Retorna um valor padrão caso não exista data
});

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
    eventId: Array.isArray(route.params.id)
      ? route.params.id[0]
      : route.params.id || '',
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

.tag {
  align-items: center;
  border-radius: 0.375rem;
  background-color: #212529;
  display: inline-flex;
  font-size: 0.75rem;
  height: 2em;
  justify-content: center;
  line-height: 1.5;
  padding-left: 0.75em;
  padding-right: 0.75em;
  white-space: nowrap;
  color: white;
}

.star-rating-author .star {
  font-size: 12px;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.modal-background {
  display: flex;
  justify-content: center;
  align-items: center;
  max-width: 80%;
  max-height: 80%;
  object-fit: contain;
}

.modal-image {
  max-width: 80%;
  max-height: 80%;
  object-fit: contain;
}

.modal-text {
  margin-top: 60px;
  font-size: 1.2rem;
  color: #5754e9;
  cursor: pointer;
  text-align: center;
  z-index: 1001;
}
</style>
