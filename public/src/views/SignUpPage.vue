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
            <ion-toast :is-open="true" :message="message" :duration="5000" :color="toastColor"
                @didDismiss="setIsOpen(false)"></ion-toast>
        </div>
    </ion-content>
</template>

<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { ConfirmPasswordMatch, IsAGoodPassword } from '@/lib/signUpUtil';
import router from '@/router';
import { ref } from 'vue';

const email = ref<HTMLInputElement | null>(null);
const name = ref<HTMLInputElement | null>(null);
const password = ref<HTMLInputElement | null>(null);
const confirmPassword = ref<HTMLInputElement | null>(null);
const isInstitution = ref<HTMLInputElement | null>(null);
const message = ref('');
const isOpen = ref(false);
const toastColor = ref('primary');

async function signUp() {
    if (areFieldsEmpty()) {
        callToast('danger', 'Please fill all fields');
        return;
    }
    if (!ConfirmPasswordMatch(password.value?.value.trim() || '0', confirmPassword.value?.value.trim() || '1')) {
        callToast('danger', 'Passwords do not match');
        return;
    }
    if (!IsAGoodPassword(password.value?.value.trim() || '')) {
        callToast('danger', 'Password is not strong enough');
        return;
    }


    const payload = {
        email: email.value?.value.trim() || '',
        name: name.value?.value.trim() || '',
        password: password.value?.value.trim() || '',
        repeatPassword: confirmPassword.value?.value.trim() || '',
        type: isInstitution.value?.checked ? 'INSTITUTION' : 'USER'
    }

    try {
        const response = await SendRequest('/auth/public/signup', 'POST', payload, ["email", "name", "password", "repeatPassword"]);
        const data = await response.json();
        console.log(data);
        if (response.ok) {
            //TODO Acceptance criteria requires for a pop up to be shown
            router.push('/login');
        }
    } catch (error) {
        console.log(error);
    }
}


function areFieldsEmpty() {
    return email.value?.value.trim() === '' || name.value?.value.trim() === '' || password.value?.value.trim() === '' || confirmPassword.value?.value.trim() === '';
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