<template>
    <div class="bg-white shadow-md p-4 mb-6 rounded-md flex items-center justify-between">
        <div class="flex items-center flex-grow">
            <h2 class="text-2xl font-semibold">
                <span v-if="loading">Loading...</span>
                <span v-else-if="userProfile">Welcome {{ userProfile.name }}</span>
                <span v-else>Welcome</span>
            </h2>
            <div class="flex-grow flex justify-center mx-4">
                <SearchBar @search="handleSearch" />
            </div>
        </div>

        <div class="flex space-x-4 items-center">
            <router-link to="/events/myEvents"
                class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
                My Events
            </router-link>
            <router-link to="/events/create"
                class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
                Create Event
            </router-link>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import SearchBar from '@/components/SearchBar.vue'; // Adjust path as necessary
import { userService } from '@/services/userService'; // Adjust path as necessary

interface UserProfile {
    name: string;
    email: string;
    role: string;
}

export default {
    components: {
        SearchBar,
    },
    setup() {
        const userProfile = ref<UserProfile | null>(null);
        const error = ref<string>('');
        const loading = ref<boolean>(true);

        const fetchUserProfile = async () => {
            try {
                const profile = await userService.getUserProfile();
                userProfile.value = {
                    name: profile.name,
                    email: profile.email,
                    role: profile.role,
                };
            } catch (err) {
                console.error('Error fetching user profile:', err);
                error.value = 'Failed to load user profile.';
            } finally {
                loading.value = false;
            }
        };

        onMounted(fetchUserProfile);

        const handleSearch = (event: CustomEvent) => {
            // Dispatch search event logic
            const searchEvent = new CustomEvent('search', { detail: event.detail });
            window.dispatchEvent(searchEvent);
        };

        return {
            userProfile,
            error,
            loading,
            handleSearch,
        };
    },
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>