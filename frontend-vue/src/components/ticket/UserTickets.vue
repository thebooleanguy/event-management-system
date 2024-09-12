<template>
    <div class="p-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">My Bookings</h2>

        <!-- Loading Message -->
        <p v-if="isLoading">Loading...</p>

        <!-- Error Message -->
        <p v-if="error" class="text-red-500">{{ error }}</p>

        <!-- No Bookings Message -->
        <p v-if="!isLoading && !userBookings.length" class="text-gray-600">No bookings found.</p>

        <!-- User Bookings List -->
        <div v-if="!isLoading && userBookings.length" class="space-y-4">
            <div v-for="booking in userBookings" :key="booking.id" class="relative booking-card">
                <div class="booking-info">
                    <div class="booking-title">{{ booking.eventTitle }}</div>
                    <div class="booking-details">Total Tickets: {{ booking.totalTickets }}</div>
                    <div class="booking-details">Total Price: ${{ booking.totalPrice }}</div>
                </div>
                <div class="booking-action">
                    <button @click="cancelBooking(booking.id)">
                        <FontAwesomeIcon :icon="faTrashAlt" class="text-lg mr-2" /> Cancel
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { bookingService } from '@/services/bookingService';
import { eventService } from '@/services/eventService';
import { userService } from '@/services/userService';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

export default defineComponent({
    components: {
        FontAwesomeIcon
    },
    setup() {
        const userId = ref<string>('');
        const userBookings = ref<Array<any>>([]);
        const error = ref<string>('');
        const isLoading = ref<boolean>(true);

        async function fetchUserBookings() {
            try {
                userId.value = await userService.getUserId();
                userBookings.value = await bookingService.getUserBookings(userId.value);

                // Fetch event titles for the bookings
                userBookings.value = await Promise.all(
                    userBookings.value.map(async (booking: any) => {
                        try {
                            const event = await eventService.getEventById(booking.eventId);
                            return { ...booking, eventTitle: event.title };
                        } catch (err) {
                            console.error('Error fetching event:', err);
                            return booking;
                        }
                    })
                );
            } catch (err) {
                error.value = 'Failed to fetch user bookings';
                console.error('Error fetching user bookings:', err);
            } finally {
                isLoading.value = false;
            }
        }

        async function cancelBooking(bookingId: string) {
            try {
                await bookingService.cancelBooking(bookingId);
                userBookings.value = userBookings.value.filter((booking: any) => booking.id !== bookingId);
                alert('Booking canceled successfully!');
            } catch (err) {
                error.value = 'Failed to cancel booking';
                console.error('Error canceling booking:', err);
            }
        }

        onMounted(() => {
            fetchUserBookings();
        });

        return {
            userBookings,
            error,
            isLoading,
            cancelBooking,
            faTrashAlt
        };
    }
});
</script>

<style scoped>
.booking-card {
    background: linear-gradient(135deg, #f0f0f0, #ffffff);
    border: 2px dashed #d1d5db;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 16px;
    margin-bottom: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
}

.booking-info {
    display: flex;
    flex-direction: column;
}

.booking-title {
    font-size: 1.25rem;
    font-weight: 600;
    color: #1f2937;
}

.booking-details {
    color: #6b7280;
    font-size: 0.875rem;
}

.booking-action {
    text-align: center;
}

.booking-action button {
    background-color: #f87171;
    color: white;
    padding: 8px 16px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.booking-action button:hover {
    background-color: #ef4444;
}

.booking-card::before,
.booking-card::after {
    content: '';
    width: 20px;
    height: 20px;
    background: white;
    border-radius: 50%;
    border: 2px dashed #d1d5db;
    position: absolute;
}

.booking-card::before {
    top: -10px;
    left: -10px;
}

.booking-card::after {
    bottom: -10px;
    right: -10px;
}
</style>