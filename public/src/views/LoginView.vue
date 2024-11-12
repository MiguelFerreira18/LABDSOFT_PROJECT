<template>
    <ion-content class="ion-padding">
        <div class="centered-square">
            <ion-input ref="email" type="email" fill="solid" label="Email" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-input ref="password" type="password" fill="solid" label="password" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-button expand="block" @click="login">Submit</ion-button>
            <ion-text class="signup-link" @click="pushToSignUp">
                <p>Sign up</p>
            </ion-text>
        </div>
    </ion-content>
</template>


<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { IsJWTExpired } from '@/lib/jwt';
import { ref } from 'vue';
import router from '@/router';

const password = ref<HTMLInputElement | null>(null);
const email = ref<HTMLInputElement | null>(null);

async function login() {
    const payload = {
        email: email.value?.value || '',
        password: password.value?.value || ''
    }
    try {
        const response = await SendRequest('/auth/public/login', 'POST', payload);
        const data = await response.json();
        if (response.ok || !IsJWTExpired(data.token)) { //NOTE: temprorary or condition needs to be changed to AND condition
            localStorage.setItem('token', data.token);

            router.push('/tabs/tab1');
        }
    } catch (error) {
        console.log(error);
    }
}

function pushToSignUp() {
    router.push('/signup');
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

.signup-link {
    margin-top: 10px;
    font-size: 0.9em;
    color: var(--ion-color-primary);
    cursor: pointer;
}
</style>