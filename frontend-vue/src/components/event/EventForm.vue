<template>
    <form @submit.prevent="handleSubmit" class="bg-card-white rounded-lg shadow-md p-8 max-w-lg mx-auto">
        <h2 class="text-2xl font-bold text-header-black mb-6 text-center">
            {{ event.id ? 'Update Event' : 'Create New Event' }}
        </h2>

        <!-- Form Fields -->
        <div class="space-y-4">
            <!-- Title -->
            <div>
                <label for="title" class="block text-gray-700 font-semibold mb-1">Title</label>
                <input v-model="event.title" type="text" id="title" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
                    placeholder="Enter event title" />
            </div>

            <!-- Description -->
            <div>
                <label for="description" class="block text-gray-700 font-semibold mb-1">Description</label>
                <textarea v-model="event.description" id="description" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
                    placeholder="Enter event description"></textarea>
            </div>

            <!-- Date -->
            <div>
                <label for="date" class="block text-gray-700 font-semibold mb-1">Date</label>
                <input v-model="event.date" type="date" id="date" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue" />
            </div>

            <!-- Location -->
            <div>
                <label for="location" class="block text-gray-700 font-semibold mb-1">Location</label>
                <input v-model="event.location" type="text" id="location" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
                    placeholder="Enter event location" />
            </div>

            <!-- Category -->
            <div>
                <label for="category" class="block text-gray-700 font-semibold mb-1">Category</label>
                <select v-model="event.category" id="category"
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue">
                    <option v-for="category in categories" :key="category" :value="category">
                        {{ toTitleCase(category) }}
                    </option>
                </select>
            </div>

            <!-- Image URL -->
            <div>
                <label for="imageUrl" class="block text-gray-700 font-semibold mb-1">Event Image URL</label>
                <input v-model="event.imageUrl" type="text" id="imageUrl"
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
                    placeholder="Enter image URL" />
                <img v-if="event.imageUrl" :src="event.imageUrl" alt="Event Image" class="w-full mt-4" />
            </div>
        </div>

        <p v-if="error" class="text-red-500 mt-4 text-center">{{ error }}</p>

        <div class="mt-6 flex justify-center">
            <button type="submit" :disabled="isLoading"
                class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-accent-blue">
                {{ isLoading ? 'Saving...' : event.id ? 'Update' : 'Create' }} Event
            </button>
        </div>
    </form>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService';
import { userService } from '@/services/userService';
import { useRouter } from 'vue-router';

// Define types for the event and category
interface Event {
    id?: number;
    title: string;
    description: string;
    date: string;
    location: string;
    category: string;
    imageUrl?: string;
}

const defaultEvent: Event = {
    title: '',
    description: '',
    date: '',
    location: '',
    category: 'OTHER',
    imageUrl: '/images/default.jpg'
};

export default defineComponent({
    props: {
        initialEvent: {
            type: Object as () => Event,
            required: true
        }
    },
    setup(props) {
        const event = ref<Event>({
            ...defaultEvent,
            ...props.initialEvent
        });
        const error = ref<string>('');
        const isLoading = ref<boolean>(false);
        const categories = ref<string[]>([]);

        const router = useRouter();

        // Utility function to convert category names to Title Case
        const toTitleCase = (str: string): string =>
            str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();

        // Fetch current user ID
        const getCurrentUserId = async (): Promise<string | null> => {
            try {
                return await userService.getUserId();
            } catch (err) {
                console.error('Failed to fetch user ID:', err);
                return null;
            }
        };

        // Form submission handler
        const handleSubmit = async (): Promise<void> => {
            isLoading.value = true;
            try {
                const organizerId = await getCurrentUserId();
                if (!organizerId) {
                    error.value = 'User not logged in';
                    return;
                }

                const eventData = { ...event.value };
                let newEventId: number;

                if (event.value.id) {
                    await eventService.updateEvent(event.value.id, eventData);
                    newEventId = event.value.id;
                } else {
                    const createdEvent = await eventService.createEvent(eventData, Number(organizerId));
                    newEventId = createdEvent.id;
                }

                // router.push(`/bookings/ticket-settings/${newEventId}`);
                router.push(`/`);
            } catch (err) {
                error.value = 'Failed to save event';
                console.error(err);
            } finally {
                isLoading.value = false;
            }
        };

        // Fetch categories on component mount
        onMounted(async () => {
            try {
                categories.value = await eventService.getCategories();
            } catch (err) {
                error.value = 'Failed to load categories';
                console.error(err);
            }
        });

        return {
            event,
            error,
            isLoading,
            categories,
            toTitleCase,
            handleSubmit
        };
    }
});
</script>

<style scoped>
/* Component-specific styles (optional) */
</style>