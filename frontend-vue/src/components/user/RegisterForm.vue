<template>
    <div class="flex items-center justify-center min-h-screen bg-background-gray">
        <form @submit.prevent="handleSubmit" class="w-full max-w-lg bg-card-white p-10 rounded-3xl shadow-xl space-y-8">
            <h2 class="text-3xl font-bold text-dark-gray text-center">Create Your Account</h2>
            <p v-if="error" class="text-red-500 text-center">{{ error }}</p>
            <div class="space-y-6">
                <div class="relative">
                    <label for="name" class="block text-sm font-medium text-dark-gray">Name</label>
                    <div
                        class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue">
                        <font-awesome-icon :icon="faUser" class="text-dark-gray ml-3" />
                        <input type="text" id="name" v-model="name" required placeholder="Enter your name"
                            class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md" />
                    </div>
                </div>
                <div class="relative">
                    <label for="email" class="block text-sm font-medium text-dark-gray">Email</label>
                    <div
                        class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue">
                        <font-awesome-icon :icon="faEnvelope" class="text-dark-gray ml-3" />
                        <input type="email" id="email" v-model="email" required placeholder="Enter your email"
                            class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md" />
                    </div>
                </div>
                <div class="relative">
                    <label for="password" class="block text-sm font-medium text-dark-gray">Password</label>
                    <div
                        class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue">
                        <font-awesome-icon :icon="faLock" class="text-dark-gray ml-3" />
                        <input type="password" id="password" v-model="password" required
                            placeholder="Enter your password"
                            class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md" />
                    </div>
                </div>
            </div>
            <button type="submit"
                class="w-full py-3 px-4 bg-button-yellow text-black rounded-md shadow-lg hover:bg-yellow-500 transition duration-300 focus:outline-none focus:ring-2 focus:ring-button-yellow focus:ring-opacity-50">
                Register
            </button>
            <p class="text-sm text-center text-dark-gray mt-6">
                Already have an account? <router-link to="/users/login" class="text-accent-blue hover:underline">Login
                    here</router-link>
            </p>
        </form>
    </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { userService } from '@/services/userService'; // Adjust path as necessary
import { userStore } from '@/stores/userStore'; // Adjust path as necessary
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUser, faEnvelope, faLock } from '@fortawesome/free-solid-svg-icons';

export default {
    name: 'Register',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const name = ref < string > ('');
        const email = ref < string > ('');
        const password = ref < string > ('');
        const error = ref < string > ('');
        const router = useRouter();

        const handleSubmit = async () => {
            try {
                const response = await userService.register({ name: name.value, email: email.value, password: password.value });
                userStore.login(response.user);
                localStorage.setItem('token', response.token);
                router.push('/');
            } catch {
                error.value = 'Registration failed. Please try again.';
            }
        };

        return {
            name,
            email,
            password,
            error,
            handleSubmit,
            faUser,
            faEnvelope,
            faLock
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>