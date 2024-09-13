<template>
    <div class="p-6 bg-white rounded-lg shadow-lg max-w-2xl mx-auto">
        <h2 class="text-2xl font-semibold mb-6 text-center">Edit Event</h2>
        <form @submit.prevent="handleSubmit">
            <!-- Event Title -->
            <div class="mb-4">
                <label for="title" class="block text-sm font-medium text-gray-700 mb-2">Event Title</label>
                <input type="text" id="title" v-model="form.title" required
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Enter event title" />
            </div>

            <!-- Event Date -->
            <div class="mb-4">
                <label for="date" class="block text-sm font-medium text-gray-700 mb-2">Event Date</label>
                <input type="date" id="date" v-model="form.date" required
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
            </div>

            <!-- Event Location -->
            <div class="mb-4">
                <label for="location" class="block text-sm font-medium text-gray-700 mb-2">Event Location</label>
                <input type="text" id="location" v-model="form.location" required
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Enter event location" />
            </div>

            <!-- Event Category -->
            <div class="mb-4">
                <label for="category" class="block text-sm font-medium text-gray-700 mb-2">Category</label>
                <select id="category" v-model="form.category" required
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                    <option value="" disabled>Select a category</option>
                    <option v-for="category in categories" :key="category" :value="category">
                        {{ category }}
                    </option>
                </select>
            </div>

            <!-- Event Description -->
            <div class="mb-6">
                <label for="description" class="block text-sm font-medium text-gray-700 mb-2">Event Description</label>
                <textarea id="description" v-model="form.description" rows="4" required
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Enter event description"></textarea>
            </div>

            <!-- Submit Button -->
            <div class="text-center">
                <button type="submit"
                    class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    Save Changes
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, onMounted } from 'vue';
import { eventService } from '@/services/eventService';

export default defineComponent({
    props: {
        event: {
            type: Object,
            required: true,
        },
    },
    setup(props) {
        // Initialize form with event data, ensuring all fields are populated with existing values
        const form = ref({
            title: props.event.title || '',
            date: props.event.date || '',
            location: props.event.location || '',
            category: props.event.category || '',
            description: props.event.description || '',
            imageUrl: props.event.imageUrl || '',  // Ensure imageUrl isn't set to null
            availableTickets: props.event.availableTickets ?? null,  // Use nullish coalescing to prevent null assignments
            ticketPrice: props.event.ticketPrice ?? null,
        });

        const categories = ref<string[]>([]);

        // Load categories dynamically
        onMounted(async () => {
            try {
                categories.value = await eventService.getCategories();
            } catch (error) {
                console.error('Failed to load categories:', error);
            }
        });

        const handleSubmit = async () => {
            try {
                // Only send non-null or non-empty fields to prevent overwriting with null
                const updatedEvent = {
                    ...props.event, // Ensure other fields like organizerId remain intact
                    ...form.value,
                };
                await eventService.updateEvent(props.event.id, updatedEvent);
                alert('Event updated successfully');
            } catch (error) {
                console.error('Failed to update event:', error);
                alert('Failed to update event.');
            }
        };

        return {
            form,
            categories,
            handleSubmit,
        };
    },
});
</script>



<style scoped>
/* You can add any additional styling here */
</style>
