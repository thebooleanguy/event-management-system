<template>
    <div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <a v-for="event in events" :key="event.id" :href="`/events/${event.id}`"
                class="bg-white rounded-lg overflow-hidden shadow-md">
                <img :src="event.imageUrl || 'placeholder-image-url'" :alt="event.title"
                    class="w-full h-48 object-cover" />
                <div class="p-4">
                    <h3 class="text-xl font-semibold mb-2">{{ event.title }}</h3>
                    <p class="text-gray-600 mb-2">{{ event.date }} - {{ event.location }}</p>
                    <p class="text-sm text-gray-500">{{ event.description.slice(0, 100) }}...</p>
                </div>
            </a>
        </div>

        <p v-if="error" class="text-red-500 mt-4">{{ error }}</p>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService'; // Adjust path as necessary

interface Event {
    id: string;
    title: string;
    imageUrl?: string;
    date: string;
    location: string;
    description: string;
}

export default {
    name: 'EventList',
    setup() {
        const events = ref<Event[]>([]);
        const error = ref<string>('');

        const fetchEvents = async () => {
            try {
                events.value = await eventService.getAllEvents();
            } catch (err) {
                error.value = 'Failed to load events';
            }
        };

        onMounted(fetchEvents);

        return {
            events,
            error,
        };
    },
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>