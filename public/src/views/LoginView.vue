<template>
    <div class="h-screen flex justify-center items-center">
        <div class="bg-gray-50 border border-gray-200 py-3 px-5 rounded-lg flex flex-col ">
            <h2 class="text-xl pb-4 text-yellow-400 font-bold self-center">Login</h2>
            <div class="p-5 flex flex-col gap-4">
                <div class="relative">
                    <input
                        class="block px-2.5 pb-2.5 pt-4 w-full text-sm bg-transparent rounded-lg border-1 bg-white  appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                        name="email" v-model="email" type="text" placeholder="" aria-label="Your email">

                    <label for="email"
                        class="absolute text-sm duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] bg-white px-2 peer-focus:px-2 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-1/2 peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto start-1">Email</label>
                </div>
                <div class="relative">
                    <input
                        class="block px-2.5 pb-2.5 pt-4 w-full text-sm bg-transparent rounded-lg border-1 bg-white  appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                        name="password" v-model="password" type="password" placeholder="" aria-label="Your password">

                    <label
                        class="absolute text-sm duration-300 transform -translate-y-4 scale-75 top-2 z-10 origin-[0] bg-white px-2 peer-focus:px-2 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:top-1/2 peer-focus:top-2 peer-focus:scale-75 peer-focus:-translate-y-4 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto start-1"
                        for="password">Password</label>
                </div>
                <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 rounded login" @click="login"
                    type="button">Login</button>
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
