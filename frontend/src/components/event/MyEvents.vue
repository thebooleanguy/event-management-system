<template>
    <div class="p-6 bg-gray-100 min-h-screen">
        <p v-if="isLoading" class="text-gray-600 text-center text-lg">Loading events...</p>

        <p v-if="!isLoading && filteredEvents.length === 0" class="text-gray-600 text-center text-lg mt-4">No events
            found for you.</p>

        <div v-if="!isLoading && filteredEvents.length > 0"
            class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mt-6">
            <a v-for="event in filteredEvents" :key="event.id" :href="`/events/${event.id}`"
                class="bg-white rounded-lg overflow-hidden shadow-lg transition-transform transform hover:scale-105">
                <img :src="event.imageUrl || 'placeholder-image-url'" :alt="event.title"
                    class="w-full h-48 object-cover" />
                <div class="p-4">
                    <h3 class="text-xl font-bold text-gray-800 mb-2">{{ event.title }}</h3>
                    <p class="text-gray-600 mb-2 text-sm">{{ event.date }} - {{ event.location }}</p>
                    <p class="text-gray-500 text-sm">{{ event.description.slice(0, 100) }}...</p>
                </div>
            </a>
        </div>

        <p v-if="error" class="text-red-600 text-center text-lg mt-4">{{ error }}</p>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService'; // Adjust path as necessary
import { userService } from '@/services/userService'; // Adjust path as necessary

interface Event {
    id: string;
    title: string;
    imageUrl?: string;
    date: string;
    location: string;
    description: string;
    organizerId: string;
}

export default {
    name: 'FilteredEventList',
    setup() {
        const filteredEvents = ref<Event[]>([]);
        const error = ref<string>('');
        const isLoading = ref<boolean>(true);

        const getCurrentUserId = async (): Promise<string | null> => {
            try {
                return await userService.getUserId();
            } catch (err) {
                console.error('Failed to fetch user ID:', err);
                return null;
            }
        };

        const fetchEvents = async () => {
            try {
                const userId = await getCurrentUserId();
                if (userId) {
                    const allEvents = await eventService.getAllEvents();
                    filteredEvents.value = allEvents.filter((event: Event) => event.organizerId === userId);
                } else {
                    error.value = 'User not logged in';
                }
            } catch (err) {
                error.value = 'Failed to load events';
            } finally {
                isLoading.value = false;
            }
        };

        onMounted(fetchEvents);

        return {
            filteredEvents,
            error,
            isLoading,
        };
    },
};
</script>

<style scoped>
/* Scoped styles if necessary */
</style>
