<template>
  <ion-page>
    <HeaderComponent title="Institution" />
    <ion-content class="content-center">
      <ion-grid class="profileInfo">
        <ion-row class="profile1">
          <ion-col size="12" class="profile1">
            <ion-row>
              <ion-icon class="img_profile" :icon="globeOutline"></ion-icon>
              <ion-col class="text-center">
                <ion-row>
                  <ion-card-title><b>{{ userInfo.name }}</b></ion-card-title>
                </ion-row>
                <ion-row>
                  <ion-card-subtitle>{{ userInfo.email }}</ion-card-subtitle>
                </ion-row>
              </ion-col>
            </ion-row>
          </ion-col>
        </ion-row>
        <ion-row>
          <ion-col size="12">
            <div class="title-with-icon">
              <ion-icon class="icon4" aria-hidden="true" :icon="flame"></ion-icon>
              <h1><b>Institution Rating</b></h1>
            </div>
            <div class="rating-icons">
              <ion-icon 
                v-for="(icon, index) in generateStars(institutionRating)" 
                :key="index" 
                :icon="icon" 
                class="rating-icon">
              </ion-icon>
            </div>
          </ion-col>
        </ion-row>
        <ion-row>
          <ion-col size="12" class="full-height">
            <div class="title-with-icon">
              <ion-icon class="icon3" aria-hidden="true" :icon="personCircleOutline"></ion-icon>
              <h1><b>Institution Information</b></h1>
            </div>
            <ion-list>
              <ion-item>
                <ion-label>Founding Date</ion-label>
                <ion-text>{{ userInfo.birthDate }}</ion-text>
              </ion-item>
            </ion-list>
          </ion-col>
        </ion-row>
        <ion-row>
          <ion-col size="12" class="full-height">
            <div class="title-with-icon">
              <ion-icon class="icon2" aria-hidden="true" :icon="trailSignOutline"></ion-icon>
              <h1><b>Location</b></h1>
            </div>
            <ion-list>
              <ion-item>
                <ion-label>Address</ion-label>
                <ion-text>{{ userInfo.address }}</ion-text>
              </ion-item>
              <ion-item>
                <ion-label>City</ion-label>
                <ion-text>{{ userInfo.city }}</ion-text>
              </ion-item>
              <ion-item>
                <ion-label>Country</ion-label>
                <ion-text>{{ userInfo.country }}</ion-text>
              </ion-item>
            </ion-list>
          </ion-col>
        </ion-row>
      </ion-grid>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { SendRequest } from '@/lib/request';
import { globeOutline } from 'ionicons/icons';
import { star, starHalf, starOutline, flame } from 'ionicons/icons';
import {
  IonPage,
  IonContent,
  IonGrid,
  IonRow,
  IonCol,
  IonCardTitle,
  IonCardSubtitle,
  IonButton,
  IonIcon,
} from '@ionic/vue';
import {
  checkmarkDoneCircle,
  checkmarkDoneCircleOutline,
  personCircleOutline,
  trailSignOutline,
} from 'ionicons/icons';
import HeaderComponent from '@/components/common/HeaderComponent.vue';

// Reactive state to store user information
const userInfo = ref({
  name: '',
  email: '',
  birthDate: '',
  gender: '',
  address: '',
  city: '',
  country: '',
});
const userPoints = ref(0);

// Fetch user information
const fetchUserInfo = async () => {
  const userId = localStorage.getItem('userId');
  const email = localStorage.getItem('email');
  try {
    const response = await SendRequest('/api/users/getuser?id=' + userId, 'GET');
    if (response.ok) {
      const data = await response.json();
      userInfo.value = data;
      userInfo.value.email = email || '';
      institutionRating.value = data.rating || 0; // Buscamos o rating direto da API
    } else {
      console.error('Error fetching user info:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching user info:', error);
  }
};
const fetchUserPoints = async () => {
  const userId = localStorage.getItem('uuid');
  try {
    const response = await SendRequest(`/api/rewards/points/${userId}`, 'GET');
    if (response.ok) {
      const { data } = await response.json();
      userPoints.value = data;
    } else {
      console.error('Error fetching user points:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching user points:', error);
  }
};

const institutionRating = ref(0); // Inicializamos com 0
const institutionStars = ref<string[]>([]);

const generateStars = (rating: number) => {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 >= 0.5;
  const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

  return [
    ...Array(fullStars).fill(star),      // Ícone de estrela cheia
    ...(hasHalfStar ? [starHalf] : []),  // Ícone de meia estrela
    ...Array(emptyStars).fill(starOutline), // Ícone de estrela vazia
  ];
};

// Fetch data on component mount
onMounted(() => {
  fetchUserInfo();
  fetchUserPoints();
  institutionStars.value = generateStars(institutionRating.value);
});
</script>

<style scoped>
.content-center {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 25%;
}

.img_profile {
  font-size: 80px;
  border-radius: 50%;
  background-color: #f0f0f0;
  color: #4f6554;
  padding: 10px;
}

.text-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 2%;
}

.profileInfo {
  padding: 5%;
}

.icon1 {
  border-radius: 50%;
  background-color: #a9c3af;
  color: #4f6554;
  font-size: 24px;
}

.icon2 {
  border-radius: 50%;
  background-color: #ffd1a9;
  color: #805938;
  font-size: 24px;
}

.icon3 {
  border-radius: 50%;
  background-color: #d4ebf8;
  color: #6a767c;
  font-size: 24px;
}

.icon4 {
  border-radius: 50%;
  background-color: #d4ebf8;
  color: #ff6200;
  font-size: 24px;
}


.title-with-icon {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

h1 {
  margin: 0;
}

ion-col {
  margin-bottom: 5%;
}

.rating-icons {
  display: flex;
  gap: 4px; /* Espaço entre as estrelas */
}

.rating-icon {
  font-size: 20px;
  color: #fbb034; /* Cor das estrelas (amarelo dourado) */
}
</style>