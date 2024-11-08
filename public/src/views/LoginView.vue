<template>
    <div>
        <div>
            <h2>Login</h2>
            <div>
                <div>
                    <input name="email" v-model="email" type="text" placeholder="" aria-label="Your email">

                    <label for="email" class="">Email</label>
                </div>
                <div>
                    <input name="password" v-model="password" type="password" placeholder="" aria-label="Your password">

                    <label for="password">Password</label>
                </div>
                <button @click="login" type="button">Login</button>
            </div>
        </div>
    </div>
</template>


<script setup lang="ts">
import { SendRequest } from '@/lib/request';
import { isJWTExpired } from '@/lib/jwt';
import { ref } from 'vue';
import router from '@/router';

const email = ref('');
const password = ref('');
async function login() {
    const payload = {
        email: email.value,
        password: password.value
    }

    try {
        const response = await SendRequest('/auth/public/login', 'POST', payload, ["email", "password"]);
        const data = await response.json();
        console.log(data);
        if (response.ok || !isJWTExpired(data.token)) { //NOTE: temprorary or condition needs to be changed to AND condition
            localStorage.setItem('token', data.token);

            router.push('/tabs/tab1');
        }
    } catch (error) {
        console.log(error);
    }
}
</script>
