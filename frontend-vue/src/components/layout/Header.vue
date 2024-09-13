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

                    <!-- My Tickets Button -->
                    <router-link to="/payments/user-payments"
                        class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center">
                        <font-awesome-icon :icon="faMoneyBillAlt" class="text-[#FFFFFF] text-lg" />
                    </router-link>

                    <!-- Admin Button -->
                    <template v-if="isAdmin">
                        <router-link to="/admin/users"
                            class="bg-green-600 text-white p-2 rounded-full flex items-center justify-center hover:bg-green-700">
                            <font-awesome-icon :icon="faCog" class="text-lg" />
                        </router-link>
                    </template>

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
import { computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'; // Use Vuex's useStore
import { userService } from '@/services/userService'; // Adjust path as necessary
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUser, faSignOutAlt, faSignInAlt, faBell, faTicketAlt, faCog, faMoneyBillAlt } from '@fortawesome/free-solid-svg-icons';

export default {
    name: 'Header',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const store = useStore();
        const router = useRouter();

        // Computed property to access the `isLoggedIn` and `isAdmin` states from Vuex store
        const isLoggedIn = computed(() => !!store.getters.getUser);
        const isAdmin = computed(() => store.getters.isAdmin);

        const checkLoginStatus = async () => {
            try {
                const profile = await userService.getUserProfile();
                if (profile) {
                    store.dispatch('login', profile); // Dispatch login action with user profile
                } else {
                    store.dispatch('logout'); // Dispatch logout action
                }
            } catch (err) {
                console.error('Error fetching user profile:', err);
                store.dispatch('logout'); // Dispatch logout action on error
            }
        };

        const handleLogout = async () => {
            try {
                await userService.logout();
                store.dispatch('logout'); // Ensure the store reflects logout
                window.location.reload(); // Refresh the page after logout
            } catch (err) {
                console.error('Logout failed:', err);
            }
        };

        onMounted(checkLoginStatus);

        return {
            isLoggedIn,
            isAdmin,
            handleLogout,
            faUser,
            faMoneyBillAlt,
            faSignOutAlt,
            faSignInAlt,
            faBell,
            faTicketAlt,
            faCog
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
