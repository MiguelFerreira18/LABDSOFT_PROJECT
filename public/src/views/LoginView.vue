<template>
    <ion-content class="ion-padding">
        <div class="centered-square">
            <ion-input ref="email" type="email" fill="solid" label="Email" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-input ref="password" type="password" fill="solid" label="Password" label-placement="floating"
                error-text="Invalid email" @ionBlur="markTouched"></ion-input>

            <ion-button expand="block" @click="login">Submit</ion-button>
            <ion-text class="signup-link" @click="pushToSignUp">
                <p>Sign up</p>
            </ion-text>
        </div>
    </ion-content>
</template>


<script setup lang="ts">

import { IonButton, IonContent, IonToast } from '@ionic/vue';
import { toastController } from '@ionic/vue';
import { SendRequest } from '@/lib/request';
import { IsJWTExpired, ParseJwt, SaveJwtFieldsToLocaStorate } from '@/lib/jwt';
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
        const user = await response.json();
        const authHeader = response.headers.get('authorization');
        if (response.ok && authHeader && !IsJWTExpired(authHeader)) {

            localStorage.setItem('userId', user.id);
            SaveJwtFieldsToLocaStorate(ParseJwt(authHeader));
            localStorage.setItem('token', authHeader);

            try {
                const rewards = await dailyRewards({ id: user.id });

                if (rewards.pointsEarned > 0) {
                    await presentToast('top', `You earned ${rewards.pointsEarned} points for logging in!`);
                } else {
                    // await presentToast('bottom',`You have already claimed your daily rewards!`);
                }

                // Ensure to only fetch rewards once per login
                localStorage.setItem('rewardsFetched', 'true');

            } catch (error) {
                console.error('Error fetching daily rewards:', error);
            }

            router.push('/tabs/tabHome');
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

interface RewardsResponse {
    points: number;
    dailyStreakDays: number;
    pointsEarned: number;
}

async function dailyRewards(response: { id: string }): Promise<RewardsResponse> {
    try {

        const res = await SendRequest(`/api/rewards/${response.id}/daily`, 'POST', {});

        const rewards: RewardsResponse = await res.json();

        return rewards as RewardsResponse;
    } catch (error) {
        console.error('Error in dailyRewards function:', error);
        throw error;
    }
}

async function presentToast(position: 'top' | 'middle' | 'bottom', message: string) {
    const toast = await toastController.create({
        message: message,
        duration: 2500,
        position: position,
        color: 'success',
        icon: 'trophy-outline',
        cssClass: 'reward-toast',
    });

    await toast.present();
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