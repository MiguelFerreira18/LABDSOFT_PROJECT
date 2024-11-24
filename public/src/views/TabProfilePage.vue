<template>
  <ion-page>
    <ion-content class="content-center">
        <ion-grid class="profileInfo">
          <ion-row class="profile1">
            <ion-col size="12" class="profile1">
                  <ion-row>
                    <img class="img_profile" src="https://media.tenor.com/wy2zHeWyf2gAAAAe/side-eye-dog-suspicious-look.png"></img>
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
            <ion-col size="12" class="full-height">
              <div class="title-with-icon">
                <ion-icon class="icon1" aria-hidden="true" :icon="checkmarkDoneCircleOutline"></ion-icon>
                <h1><b>Bio</b></h1>
              </div>
              <ion-text>
                User is a tech enthusiast and entrepreneur with a strong focus on smart city solutions, 
                gamification, and community-driven platforms. Passionate about creating innovative digital tools, 
                he combines technical expertise with a vision to enhance social connections and urban living.
              </ion-text>
            </ion-col>
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
          <ion-row>
            <ion-col size="12" class="full-height">
              <div class="title-with-icon">
                <ion-icon class="icon3" aria-hidden="true" :icon="personCircleOutline"></ion-icon>
                <h1><b>Personal Information</b></h1>
              </div>
              <ion-list>
                <ion-item>
                  <ion-label>Birth Date</ion-label>
                  <ion-text>{{ userInfo.birthDate }}</ion-text>
                </ion-item>
                <ion-item>
                  <ion-label>Gender</ion-label>
                  <ion-text>{{ userInfo.gender }}</ion-text>
                </ion-item>
              </ion-list>
            </ion-col>
          </ion-row>
          <!-- <ion-row>
            <ion-col size="12" class="full-height">
              <ion-card class="full-height" color="card">
                <ion-card-header>
                  <ion-card-title><b>Favourite Events</b></ion-card-title>
                </ion-card-header>
                <ion-card-content>
                </ion-card-content>
              </ion-card>
            </ion-col>
          </ion-row> -->
          <ion-button color="light" class="sign-out">Sign Out</ion-button>
        </ion-grid>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { SendRequest } from "@/lib/request";
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
} from "@ionic/vue";
import { checkmarkDoneCircle, checkmarkDoneCircleOutline, personCircleOutline, trailSignOutline } from "ionicons/icons";

// Reactive state to store user information
const userInfo = ref({
  name: "",
  email: "",
  birthDate: "",
  gender: "",
  address: "",
  city: "",
  country: "",
});

// Fetch user information
const fetchUserInfo = async () => {
  const userId = localStorage.getItem("userId");
  const email = localStorage.getItem("email");
  try {
    const response = await SendRequest("/api/users/getuser?id=" + userId, "GET");
    if (response.ok) {
      const data = await response.json();
      userInfo.value = data;
      userInfo.value.email = email || "";
    } else {
      console.error("Error fetching user info:", response.statusText);
    }
  } catch (error) {
    console.error("Error fetching user info:", error);
  }
};

// Fetch data on component mount
onMounted(() => {
  fetchUserInfo();
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
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px solid white;
  object-fit: cover;
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
  background-color: #D4EBF8;
  color: #6a767c;
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

</style>