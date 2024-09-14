<template>
    <!-- Ticket Booking Form -->
    <form @submit.prevent="handleSubmit" class="bg-white rounded-lg shadow-md p-8 max-w-lg mx-auto">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Book Ticket</h2>

        <!-- Display Event ID and Event Title (Non-editable) -->
        <div class="space-y-4">
            <div>
                <label for="eventId" class="block text-gray-700 font-semibold mb-1">Event ID</label>
                <input type="text" id="eventId" :value="eventId" readonly
                    class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600" />
            </div>

            <div>
                <label for="eventTitle" class="block text-gray-700 font-semibold mb-1">Event Title</label>
                <input type="text" id="eventTitle" :value="eventTitle" readonly
                    class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600" />
            </div>

            <!-- Display Available Tickets (Read-Only) -->
            <div>
                <label for="availableTickets" class="block text-gray-700 font-semibold mb-1">Available Tickets</label>
                <input type="text" id="availableTickets" :value="availableTickets" readonly
                    class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600" />
            </div>

            <!-- Editable Fields for Booking -->
            <div>
                <label for="totalTickets" class="block text-gray-700 font-semibold mb-1">Total Tickets</label>
                <input type="number" id="totalTickets" v-model.number="totalTickets" min="1" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- Payment Method Dropdown -->
            <div>
                <label for="paymentMethod" class="block text-gray-700 font-semibold mb-1">Payment Method</label>
                <select id="paymentMethod" v-model="paymentMethod"
                    class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value=null selected>Pay at Entrance</option>
                    <option value="Credit Card">Credit Card</option>
                    <option value="PayPal">PayPal</option>
                </select>
            </div>
        </div>

        <!-- Display Error Message if any -->
        <p v-if="error" class="text-red-500 mt-4 text-center">{{ error }}</p>

        <!-- Submit Button -->
        <div class="mt-6 flex justify-center">
            <button type="submit"
                class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="isLoading">
                {{ isLoading ? 'Booking...' : 'Book Ticket' }}
            </button>
        </div>
    </form>
</template>


<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { bookingService } from '@/services/bookingService';
import { userService } from '@/services/userService';
import { eventService } from '@/services/eventService';

export default defineComponent({
    props: {
        eventId: {
            type: Number,
            required: true
        },
        eventTitle: {
            type: String,
            required: true
        }
    },
    setup(props) {
        const userId = ref<string>('');
        const totalTickets = ref<number>(1);
        const availableTickets = ref<number>(0); // Kept for future use
        const paymentMethod = ref<string | null>(null); // Added payment method
        const error = ref<string>('');
        const isLoading = ref<boolean>(false);

        onMounted(async () => {
            try {
                userId.value = await userService.getUserId() ?? '';
                console.log('Fetched user ID:', userId.value);

                availableTickets.value = await eventService.getAvailableTickets(props.eventId);
                console.log('Available tickets:', availableTickets.value);
            } catch (err) {
                error.value = 'Failed to fetch user information or available tickets';
                console.error('Error fetching data:', err);
            }
        });

        async function handleSubmit() {
            isLoading.value = true;
            try {
                const bookingData = {
                    eventId: props.eventId,
                    userId: userId.value,
                    totalTickets: totalTickets.value,
                    paymentMethod: paymentMethod.value || undefined // Set to undefined if not selected
                };

                console.log('Submitting booking data:', bookingData);

                await bookingService.bookTicket(bookingData);
                alert('Ticket booked successfully!');
            } catch (err) {
                error.value = err instanceof Error
                    ? `Booking failed: ${err.message}`
                    : 'Booking failed: Unknown error occurred';
                console.error('Booking error:', error.value);
            } finally {
                isLoading.value = false;
            }
        }

        return {
            userId,
            totalTickets,
            availableTickets,
            paymentMethod,
            error,
            isLoading,
            handleSubmit
        };
    }
});
</script>


<style scoped>
/* Add any scoped styles if necessary */
</style>