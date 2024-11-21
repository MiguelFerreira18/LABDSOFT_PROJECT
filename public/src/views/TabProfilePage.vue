<template>
    <ion-page>
      <ion-content color="background" :fullscreen="true" class="content-center">
        <ion-card class="card_profile">
          <ion-grid class="profile1">
            <ion-row class="profile1">
              <ion-col size="12" class="profile1">
                <ion-card color="card">
                  <ion-card-content>
                    <ion-row>
                      <img class="img_profile" src="https://www.w3schools.com/howto/img_avatar.png"></img>
                      <ion-col class="text-center">
                        <ion-row>
                          <ion-card-title><b>Alberto Júnior</b></ion-card-title>
                        </ion-row>
                        <ion-row>
                          <ion-card-subtitle>user123@gmail.com</ion-card-subtitle>
                        </ion-row>
                      </ion-col>
                    </ion-row>
                  </ion-card-content>
                </ion-card>
              </ion-col>
            </ion-row>
            <ion-row>
              <ion-col size="6" class="full-height">
                <ion-card class="full-height" color="card">
                  <ion-card-header>
                    <ion-card-title><b>Bio</b></ion-card-title>
                  </ion-card-header>
                  <ion-card-content>
                    User is a tech enthusiast and entrepreneur with a strong focus on smart city solutions, 
                    gamification, and community-driven platforms. Passionate about creating innovative digital tools, 
                    he combines technical expertise with a vision to enhance social connections and urban living.
                  </ion-card-content>
                </ion-card>
              </ion-col>
              <ion-col size="6" class="full-height">
                <ion-card class="full-height" color="card">
                  <ion-card-header>
                    <ion-card-title><b>Location</b></ion-card-title>
                  </ion-card-header>
                  <ion-card-content>
                    <ul>
                      <li><b>Address:</b> {{ userInfo.address }}</li>
                      <li><b>City:</b> {{ userInfo.city }}</li>
                      <li><b>Country:</b> {{ userInfo.country }}</li>
                    </ul>
                  </ion-card-content>
                </ion-card>
              </ion-col>
            </ion-row>
            <ion-row>
              <ion-col size="6" class="full-height">
                <ion-card class="full-height" color="card">
                  <ion-card-header>
                    <ion-card-title><b>Personal Info</b></ion-card-title>
                  </ion-card-header>
                  <ion-card-content>
                    <ul>
                      <li><b>Birth Date:</b> {{ userInfo.birthDate }}</li>
                      <li><b>Gender:</b> {{ userInfo.gender }}</li>
                    </ul>
                  </ion-card-content>
                </ion-card>
              </ion-col>
              <ion-col size="6" class="full-height">
                <ion-card class="full-height" color="card">
                  <ion-card-header>
                    <ion-card-title><b>Favourite Events</b></ion-card-title>
                  </ion-card-header>
                  <ion-card-content>
                  </ion-card-content>
                </ion-card>
              </ion-col>
            </ion-row>
          </ion-grid>
        </ion-card>
      </ion-content>
    </ion-page>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from "vue";
  import axios from "axios";
  import {
    IonPage,
    IonContent,
    IonGrid,
    IonRow,
    IonCol,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardSubtitle,
    IonCardContent,
  } from "@ionic/vue";
  
  // Reactive state to store user information
  const userInfo = ref({
    birthDate: "",
    gender: "",
    address: "",
    city: "",
    country: "",
  });

  
  // Fetch user information
  const fetchUserInfo = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        console.error("No token found");
        return;
    } else {
        console.log(token.valueOf);
    }

    try {
      const response = await axios.get("http://localhost:9091/userInfo/info?email=admin@smartcity.com");
      userInfo.value = response.data;
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
    height: 100%;
  }
  
  .card_profile {
    padding: 2%;
    margin-top: 2%;
    margin-bottom: 2%;
    margin-left: 5%;
    margin-right: 5%;
    border-radius: 20px;
  }
  .full-height {
    height: 100%;
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
  
  </style>
  