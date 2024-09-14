<template>
    <div class="p-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">All Bookings</h2>

        <p v-if="isLoading" class="text-gray-600">Loading...</p>
        <p v-else-if="error" class="text-red-500">{{ error }}</p>
        <p v-else-if="allBookings.length === 0" class="text-gray-600">No bookings found.</p>
        <table v-else class="min-w-full bg-white rounded-lg shadow-md overflow-hidden">
            <thead class="bg-gray-100">
                <tr>
                    <th class="py-3 px-6 text-left">Event Title</th>
                    <th class="py-3 px-6 text-left">User ID</th>
                    <th class="py-3 px-6 text-left">Total Tickets</th>
                    <th class="py-3 px-6 text-left">Total Price</th>
                    <th class="py-3 px-6 text-center">Action</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="booking in allBookings" :key="booking.id" class="border-b">
                    <td class="py-4 px-6">{{ booking.eventTitle }}</td>
                    <td class="py-4 px-6">{{ booking.userId }}</td>
                    <td class="py-4 px-6">{{ booking.totalTickets }}</td>
                    <td class="py-4 px-6">${{ booking.totalPrice.toFixed(2) }}</td>
                    <td class="py-4 px-6 text-center">
                        <button class="text-red-600 hover:text-red-800 focus:outline-none"
                            @click="cancelBooking(booking.id)" aria-label="Cancel booking">
                            <font-awesome-icon :icon="['fas', 'trash-alt']" />
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { bookingService } from '@/services/bookingService';
import { eventService } from '@/services/eventService';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

export default defineComponent({
    components: {
        FontAwesomeIcon
    },
    setup() {
        const allBookings = ref<any[]>([]);
        const error = ref('');
        const isLoading = ref(true);

        onMounted(async () => {
            try {
                const bookings = await bookingService.getAllBookings();

                // Fetch event titles for the bookings
                allBookings.value = await Promise.all(
                    bookings.map(async (booking: any) => {
                        try {
                            const event = await eventService.getEventById(booking.eventId);
                            return { ...booking, eventTitle: event.title }; // Change 'eventName' to 'eventTitle'
                        } catch (err) {
                            console.error('Error fetching event:', err);
                            return { ...booking, eventTitle: 'Unknown Event' }; // Default to 'Unknown Event' if there's an error
                        }
                    })
                );
            } catch (err) {
                error.value = 'Failed to fetch all bookings. Please try again later.';
                console.error('Error fetching bookings:', err);
            } finally {
                isLoading.value = false;
            }
        });

        async function cancelBooking(bookingId: number) {
            if (!confirm('Are you sure you want to cancel this booking?')) return;
            try {
                await bookingService.cancelBooking(bookingId);
                allBookings.value = allBookings.value.filter((booking: any) => booking.id !== bookingId);
                alert('Booking canceled successfully!');
            } catch (err) {
                error.value = 'Failed to cancel booking. Please try again later.';
                console.error('Error canceling booking:', err);
            }
        }

        return {
            allBookings,
            error,
            isLoading,
            cancelBooking
        };
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>