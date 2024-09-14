<template>
    <div class="flex items-center justify-center min-h-screen bg-[#E5E5E5]">
        <form @submit.prevent="handleSubmit" class="w-full max-w-lg bg-[#FFFFFF] p-10 rounded-3xl shadow-xl space-y-8">
            <h2 class="text-3xl font-bold text-[#1A1A1A] text-center">Login to Your Account</h2>
            <p v-if="error" class="text-red-500 text-center">{{ error }}</p>
            <div class="space-y-6">
                <div class="relative">
                    <label for="email" class="block text-sm font-medium text-[#1A1A1A]">Email</label>
                    <div
                        class="flex items-center border border-[#D1D5DB] rounded-md shadow-sm focus-within:ring-2 focus-within:ring-[#7DD3FC]">
                        <font-awesome-icon :icon="faEnvelope" class="text-[#1E40AF] ml-3" />
                        <input type="email" id="email" v-model="email" required placeholder="Enter your email"
                            class="w-full px-3 py-2 text-[#1A1A1A] focus:outline-none rounded-r-md" />
                    </div>
                </div>
                <div class="relative">
                    <label for="password" class="block text-sm font-medium text-[#1A1A1A]">Password</label>
                    <div
                        class="flex items-center border border-[#D1D5DB] rounded-md shadow-sm focus-within:ring-2 focus-within:ring-[#7DD3FC]">
                        <font-awesome-icon :icon="faLock" class="text-[#1E40AF] ml-3" />
                        <input type="password" id="password" v-model="password" required
                            placeholder="Enter your password"
                            class="w-full px-3 py-2 text-[#1A1A1A] focus:outline-none rounded-r-md" />
                    </div>
                </div>
            </div>
            <button type="submit"
                class="w-full py-3 px-4 bg-[#F7D44C] text-[#1A1A1A] rounded-md shadow-lg hover:bg-[#E5B840] transition duration-300 focus:outline-none focus:ring-2 focus:ring-[#F7D44C] focus:ring-opacity-50">
                Login
            </button>
            <p class="text-sm text-center text-[#1A1A1A] mt-6">
                Don't have an account? <router-link to="/users/register" class="text-[#7DD3FC] hover:underline">Register
                    here</router-link>
            </p>
        </form>
    </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { userService } from '@/services/userService'; // Adjust path as necessary
import { useStore } from 'vuex'; // Import Vuex store
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faEnvelope, faLock } from '@fortawesome/free-solid-svg-icons';

export default {
    name: 'Login',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const email = ref<string>('');
        const password = ref<string>('');
        const error = ref<string>('');
        const router = useRouter();
        const store = useStore(); // Use Vuex store

        const handleSubmit = async () => {
            try {
                const response = await userService.login(email.value, password.value);
                if (response && response.token) {
                    store.dispatch('login', response.user); // Dispatch the login action
                    localStorage.setItem('token', response.token);
                    router.push('/');
                } else {
                    error.value = 'Invalid email or password';
                }
            } catch {
                error.value = 'Invalid email or password';
            }
        };

        return {
            email,
            password,
            error,
            handleSubmit,
            faEnvelope,
            faLock
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
