<template>
    <div class="p-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">My Payments</h2>

        <!-- Loading Message -->
        <p v-if="isLoading" class="text-gray-500">Loading...</p>

        <!-- Error Message -->
        <p v-if="error" class="text-red-500">{{ error }}</p>

        <!-- No Payments Message -->
        <p v-if="!isLoading && !userPayments.length" class="text-gray-600">No payments found.</p>

        <!-- User Payments List -->
        <div v-if="!isLoading && userPayments.length" class="space-y-4">
            <div v-for="payment in userPayments" :key="payment.id"
                class="relative payment-card p-4 flex items-center justify-between">
                <div class="flex-1">
                    <div class="text-lg font-semibold text-gray-800">Transaction ID: {{ payment.transactionId }}</div>
                    <div class="text-gray-600">Amount: ${{ payment.amount }}</div>
                    <div class="text-gray-600">Payment Method: {{ payment.paymentMethod }}</div>
                    <div class="text-gray-600">Booking ID: {{ payment.bookingId }}</div>
                </div>
                <button v-if="!payment.canceled" @click="cancelPayment(payment.id)" class="payment-btn text-gray-700">
                    <FontAwesomeIcon :icon="faTrashAlt" class="mr-2" /> Cancel
                </button>
                <span v-else class="text-red-500">Canceled</span>
            </div>
        </div>

    </div>
</template>

<script lang="ts">
import { userService } from '@/services/userService';
import { defineComponent, ref, onMounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import paymentService from '@/services/paymentService';

export default defineComponent({
    components: {
        FontAwesomeIcon
    },
    setup() {
        const userPayments = ref<Array<any>>([]);
        const error = ref<string>('');
        const isLoading = ref<boolean>(true);

        const userId = ref<string | null>(null);

        async function fetchUserPayments() {
            try {
                userId.value = await userService.getUserId();
                console.log('User ID:', Number(userId.value));
                const payments = await paymentService.getPaymentsByUser(Number(userId.value));
                console.log('Fetched Payments:', payments); // Log payments to check if they are returned
                userPayments.value = payments;
            } catch (err) {
                error.value = 'Failed to fetch payments';
                console.error('Error fetching payments:', err);
            } finally {
                isLoading.value = false;
            }
        }


        async function cancelPayment(paymentId: number) {
            try {
                await paymentService.cancelPayment(paymentId);
                userPayments.value = userPayments.value.map(payment =>
                    payment.id === paymentId ? { ...payment, canceled: true } : payment
                );
                alert('Payment canceled successfully!');
            } catch (err) {
                error.value = 'Failed to cancel payment';
                console.error('Error canceling payment:', err);
            }
        }

        onMounted(() => {
            fetchUserPayments();
        });

        return {
            userPayments,
            error,
            isLoading,
            cancelPayment,
            faTrashAlt
        };
    }
});
</script>

<style scoped>
.payment-card {
    background: #f9fafb;
    border: 2px solid #e5e7eb;
    border-radius: 8px;
    padding: 16px;
    position: relative;
}

.payment-btn {
    border: 1px solid #d1d5db;
    background: #f9fafb;
    padding: 8px 12px;
    border-radius: 8px;
    display: inline-flex;
    align-items: center;
    font-size: 0.875rem;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
}

.payment-btn:hover {
    background-color: #e5e7eb;
    color: #1f2937;
}
</style>
