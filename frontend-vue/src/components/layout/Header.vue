<template>
    <header class="bg-[#1A1A1A] text-[#FFFFFF] p-4">
        <div class="container mx-auto flex justify-between items-center">
            <router-link to="/" class="text-2xl font-bold">ANYEVENT.LK</router-link>
            <div class="flex items-center space-x-4">
                <template v-if="isLoggedIn">
                    <!-- User Profile Button -->
                    <router-link to="/users/profile"
                        class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center">
                        <font-awesome-icon :icon="faUser" class="text-[#FFFFFF] text-lg" />
                    </router-link>

                    <!-- Notifications Button -->
                    <router-link to="/notifications"
                        class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center">
                        <font-awesome-icon :icon="faBell" class="text-[#FFFFFF] text-lg" />
                    </router-link>

                    <!-- My Tickets Button -->
                    <router-link to="/bookings/my-bookings"
                        class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center">
                        <font-awesome-icon :icon="faTicketAlt" class="text-[#FFFFFF] text-lg" />
                    </router-link>

                    <!-- Logout Button -->
                    <button @click="handleLogout"
                        class="bg-red-600 text-white p-2 rounded-full flex items-center justify-center hover:bg-red-700">
                        <font-awesome-icon :icon="faSignOutAlt" class="text-lg" />
                    </button>
                </template>
                <template v-else>
                    <router-link to="/users/login"
                        class="bg-blue-600 text-white p-2 rounded-full flex items-center justify-center hover:bg-blue-700">
                        <font-awesome-icon :icon="faSignInAlt" class="text-lg" />
                    </router-link>
                </template>
            </div>
        </div>
    </header>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { userStore } from '@/stores/userStore'; // Adjust path as necessary
import { userService } from '@/services/userService'; // Adjust path as necessary
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUser, faSignOutAlt, faSignInAlt, faBell, faTicketAlt } from '@fortawesome/free-solid-svg-icons';

export default {
    name: 'Header',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const isLoggedIn = ref<boolean>(false);
        const router = useRouter();

        const checkLoginStatus = async () => {
            try {
                const profile = await userService.getUserProfile();
                if (profile) {
                    userStore.login(profile); // Update store with user profile
                    isLoggedIn.value = true;
                } else {
                    userStore.logout(); // Ensure the store reflects logout
                    isLoggedIn.value = false;
                }
            } catch (err) {
                console.error('Error fetching user profile:', err);
                userStore.logout(); // Ensure store is updated on error
                isLoggedIn.value = false;
            }
        };

        const handleLogout = async () => {
            try {
                await userService.logout();
                router.push('/'); // Redirect to login page after logout
            } catch (err) {
                console.error('Logout failed:', err);
            }
        };

        onMounted(checkLoginStatus);

        return {
            isLoggedIn,
            handleLogout,
            faUser,
            faSignOutAlt,
            faSignInAlt,
            faBell,
            faTicketAlt
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>