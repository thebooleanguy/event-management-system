<template>
    <div class="p-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">My Bookings</h2>

        <!-- Loading Message -->
        <p v-if="isLoading" class="text-gray-500">Loading...</p>

        <!-- Error Message -->
        <p v-if="error" class="text-red-500">{{ error }}</p>

        <!-- No Bookings Message -->
        <p v-if="!isLoading && !userBookings.length" class="text-gray-600">No bookings found.</p>

        <!-- User Bookings List -->
        <div v-if="!isLoading && userBookings.length" class="space-y-4">
            <div v-for="booking in userBookings" :key="booking.id"
                class="relative ticket-card p-4 flex items-center justify-between">
                <div class="flex-1">
                    <div class="text-lg font-semibold text-gray-800">{{ booking.eventTitle }}</div>
                    <div class="text-gray-600">Total Tickets: {{ booking.totalTickets }}</div>
                    <div class="text-gray-600">Total Price: ${{ booking.totalPrice }}</div>
                </div>
                <div class="ml-4 flex-shrink-0 flex items-center">
                    <button v-if="!booking.paymentId" @click="openPaymentModal(booking)"
                        class="ticket-btn text-gray-700">
                        <span class="mr-2">💳</span> Pay Online
                    </button>
                    <span v-else class="paid-stamp text-gray-700 inline-flex items-center">
                        <FontAwesomeIcon :icon="faCashRegister" class="mr-2" /> Paid
                    </span>
                </div>
                <button v-if="!booking.paymentId" @click="cancelBooking(booking.id)" class="ticket-btn text-gray-700">
                    <FontAwesomeIcon :icon="faTrashAlt" class="mr-2" /> Cancel
                </button>
            </div>
        </div>

        <!-- Payment Modal -->
        <PaymentModal :isVisible="showPaymentModal" :bookingId="selectedBookingId" :userId="userId"
            :amount="selectedAmount" @close="closePaymentModal" />
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import PaymentModal from '@/components/payment/PaymentModal.vue';
import { bookingService } from '@/services/bookingService';
import { eventService } from '@/services/eventService';
import { userService } from '@/services/userService';
import { faTrashAlt, faCashRegister } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

export default defineComponent({
    components: {
        FontAwesomeIcon,
        PaymentModal
    },
    setup() {
        const userId = ref<number>(0);
        const userBookings = ref<Array<any>>([]);
        const error = ref<string>('');
        const isLoading = ref<boolean>(true);
        const showPaymentModal = ref<boolean>(false);
        const selectedBookingId = ref<number>(0);
        const selectedAmount = ref<number>(0);

        async function fetchUserBookings() {
            try {
                const userIdValue = await userService.getUserId();
                if (userIdValue !== null) {
                    userId.value = Number(userIdValue);
                    userBookings.value = await bookingService.getBookingsByUser(userId.value);

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
                } else {
                    // Handle the case when userIdValue is null
                }
            } catch (err) {
                error.value = 'Failed to fetch user bookings';
                console.error('Error fetching user bookings:', err);
            } finally {
                isLoading.value = false;
            }
        }

        async function cancelBooking(bookingId: number) {
            try {
                await bookingService.cancelBooking(bookingId);
                userBookings.value = userBookings.value.filter((booking: any) => booking.id !== bookingId);
                alert('Booking canceled successfully!');
            } catch (err) {
                error.value = 'Failed to cancel booking';
                console.error('Error canceling booking:', err);
            }
        }

        function openPaymentModal(booking: any) {
            selectedBookingId.value = booking.id;
            selectedAmount.value = booking.totalPrice;
            showPaymentModal.value = true;
        }

        function closePaymentModal() {
            showPaymentModal.value = false;
        }

        onMounted(() => {
            fetchUserBookings();
        });

        return {
            userBookings,
            error,
            isLoading,
            cancelBooking,
            openPaymentModal,
            closePaymentModal,
            showPaymentModal,
            selectedBookingId,
            selectedAmount,
            userId,
            faTrashAlt,
            faCashRegister
        };
    }
});
</script>



<style scoped>
.ticket-card {
    background: #f9fafb;
    /* light gray for a faded look */
    border: 2px dashed #d1d5db;
    /* dashed border for the ticket edge */
    border-radius: 12px;
    position: relative;
    overflow: hidden;
    padding: 16px;
}

.ticket-card::before,
.ticket-card::after {
    content: '';
    position: absolute;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: white;
    border: 2px dashed #d1d5db;
}

.ticket-card::before {
    top: -10px;
    left: -10px;
}

.ticket-card::after {
    bottom: -10px;
    right: -10px;
}

/* Custom button styling to match ticket theme */
.ticket-btn {
    border: 1px solid #d1d5db;
    background: #f9fafb;
    /* Matching the ticket background color */
    color: #4b5563;
    /* Darker gray for text */
    padding: 8px 12px;
    margin-left: 10px;
    margin-right: 10px;
    border-radius: 8px;
    display: inline-flex;
    align-items: center;
    font-size: 0.875rem;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
}

.ticket-btn:hover {
    background-color: #e5e7eb;
    /* Slightly darker gray */
    color: #1f2937;
    /* Darker text color */
}

/* Styling the paid stamp */
.paid-stamp {
    background-color: #d1d5db;
    /* Light gray background */
    color: #4b5563;
    /* Darker text color */
    padding: 4px 8px;
    border-radius: 12px;
    display: inline-flex;
    align-items: center;
    font-weight: 600;
    font-size: 0.875rem;
}
</style>
