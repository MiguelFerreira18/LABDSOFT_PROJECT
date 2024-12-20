<template>
  <ion-page>
    <ion-content class="ion-padding">
      <div class="centered-square">
        <ion-input
          v-model="email"
          type="email"
          fill="solid"
          label="Email"
          label-placement="floating"
          error-text="Invalid email"
          @ionBlur="markTouched"
        ></ion-input>

        <ion-input
          v-model="name"
          fill="solid"
          label="Name"
          label-placement="floating"
          error-text="Invalid name"
          @ionBlur="markTouched"
        ></ion-input>

        <ion-input
          v-model="password"
          type="password"
          fill="solid"
          label="Password"
          label-placement="floating"
          error-text="Invalid email"
          @ionBlur="markTouched"
        ></ion-input>

        <ion-input
          v-model="confirmPassword"
          type="password"
          fill="solid"
          label="Repeat Password"
          label-placement="floating"
          error-text="Invalid email"
          @ionBlur="markTouched"
        ></ion-input>

        <ion-item lines="none">
          <ion-label class="ion-text-wrap">Favorite Categories</ion-label>
          <ion-select
            v-model="selectedCategories"
            :multiple="true"
            placeholder="Select categories"
            @ionChange="onCategoryChange"
          >
            <ion-select-option
              v-for="category in categories"
              :key="category"
              :value="category"
            >
              {{ category }}
            </ion-select-option>
          </ion-select>
        </ion-item>

        <ion-checkbox v-model="isInstitution"
          >Are you an institution?</ion-checkbox
        >

        <ion-button expand="block" @click="signUp">Submit</ion-button>

        <ion-toast
          :is-open="isOpen"
          :message="message"
          :duration="5000"
          :color="toastColor"
          @didDismiss="setIsOpen(false)"
        ></ion-toast>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { ConfirmPasswordMatch, IsAGoodPassword } from '@/lib/signUpUtil';
import router from '@/router';
import { ref } from 'vue';
import { categories } from '@/lib/categories';
import {
  IonPage,
  IonContent,
  IonInput,
  IonButton,
  IonCheckbox,
  IonSelect,
  IonSelectOption,
  IonToast,
  IonItem,
  IonLabel,
} from '@ionic/vue';

console.log('Categorias disponíveis:', categories);

const email = ref<string>('');
const name = ref<string>('');
const password = ref<string>('');
const confirmPassword = ref<string>('');
const isInstitution = ref<boolean>(false);
const message = ref('');
const isOpen = ref(false);
const toastColor = ref('primary');
const selectedCategories = ref<string[]>([]);

function onCategoryChange(event: any) {
  console.log('Categorias selecionadas:', event.detail.value);
  selectedCategories.value = event.detail.value;
}

async function signUp() {
  console.log('Signing up');
  console.log('Email:', email.value);
  if (areFieldsEmpty()) {
    callToast('danger', 'Please fill all fields');
    return;
  }
  if (
    !ConfirmPasswordMatch(
      password.value.trim() || '0',
      confirmPassword.value.trim() || '1',
    )
  ) {
    callToast('danger', 'Passwords do not match');
    return;
  }
  if (!IsAGoodPassword(password.value.trim() || '')) {
    callToast('danger', 'Password is not strong enough');
    return;
  }

  console.log('Categorias selecionadas:', selectedCategories.value);

  const payload = {
    email: email.value.trim() || '',
    name: name.value.trim() || '',
    password: password.value.trim() || '',
    pushToken: localStorage.getItem('pushToken') || '',
    repeatPassword: confirmPassword.value.trim() || '',
    type: isInstitution.value ? 'INSTITUTION' : 'USER',
    preferredCategories: selectedCategories.value,
  };

  try {
    const response = await SendRequest('/auth/public/signup', 'POST', payload);
    if (response.ok) {
      //TODO Acceptance criteria requires for a pop up to be shown
      router.push('/login');
    }
  } catch (error) {
    console.log(error);
  }
}

function areFieldsEmpty() {
  return (
    email.value.trim() === '' ||
    name.value.trim() === '' ||
    password.value.trim() === '' ||
    confirmPassword.value.trim() === ''
  );
}

function callToast(color: string, passMessage: string) {
  toastColor.value = color;
  isOpen.value = true;
  message.value = passMessage;

  console.log(passMessage);
}

function setIsOpen(value: boolean) {
  isOpen.value = value;
}

function markTouched() {
  const input = document.querySelector('ion-input[ref="input"]');
  if (input) {
    input.classList.add('ion-touched');
  }
}
</script>
<style>
.centered-square {
  width: 80%;
  max-width: 320px;
  padding: 20px;
  border-radius: 10px;
  background-color: #f0f0f0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
