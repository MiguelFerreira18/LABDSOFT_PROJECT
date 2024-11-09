<template>

    <ion-content class="ion-padding">
        <div class="centered-square">
            <ion-input ref="email" type="email" fill="solid" label="Email" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-input ref="name" fill="solid" label="Name" label-placement="floating" error-text="Invalid name"
                @ionBlur="markTouched"></ion-input>

            <ion-input ref="password" type="password" fill="solid" label="password" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-input ref="confirmPassword" type="password" fill="solid" label="password" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-checkbox ref="isInstitution">Are you an
                institution?</ion-checkbox>

            <ion-button expand="block" @click="signUp">Submit</ion-button>
            <ion-toast :is-open="true" message="Sign up successful" :duration="5000" :color="toastColor"
                @didDismiss="setIsOpen(false)"></ion-toast>
        </div>
    </ion-content>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import router from '@/router';
import { ref } from 'vue';

const email = ref<HTMLInputElement | null>(null);
const name = ref<HTMLInputElement | null>(null);
const password = ref<HTMLInputElement | null>(null);
const confirmPassword = ref<HTMLInputElement | null>(null);
const isInstitution = ref<HTMLInputElement | null>(null);
const isOpen = ref(false);
const toastColor = ref('primary');

async function signUp() {
    if (!confirmPasswordMatch()) {
        toastColor.value = 'danger';
        isOpen.value = true;
        console.log('Passwords do not match');
        return;
    }

    const payload = {
        email: email.value?.value.trim() || '',
        name: name.value?.value.trim() || '',
        password: password.value?.value.trim() || '',
        repeatPassword: confirmPassword.value?.value.trim() || '',
        type: isInstitution.value?.checked ? 'INSTITUTION' : 'USER'
    }
    console.log(payload);

    try {
        const response = await SendRequest('/auth/public/signup', 'POST', payload, ["email", "name", "password", "repeatPassword"]);
        const data = await response.json();
        console.log(data);
        if (response.ok) {
            router.push('/login');
        }
    } catch (error) {
        console.log(error);
    }
}

function confirmPasswordMatch() {
    return password.value?.value === confirmPassword.value?.value;
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