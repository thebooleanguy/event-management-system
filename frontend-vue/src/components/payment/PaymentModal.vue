<template>
    <div v-if="isVisible" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-80">
            <h3 class="text-xl font-semibold mb-4">Payment Details</h3>
            <form @submit.prevent="handlePayment">
                <div class="mb-4">
                    <label for="paymentMethod" class="block text-gray-700">Payment Method</label>
                    <select v-model="paymentMethod" id="paymentMethod"
                        class="w-full border border-gray-300 rounded px-3 py-2">
                        <option value="Credit Card">Credit Card</option>
                        <option value="PayPal">PayPal</option>
                    </select>
                </div>
                <div v-if="paymentMethod === 'Credit Card'">
                    <div class="mb-4">
                        <label for="cardNumber" class="block text-gray-700">Card Number</label>
                        <input type="text" id="cardNumber" placeholder="1234 5678 9012 3456"
                            class="w-full border border-gray-300 rounded px-3 py-2" disabled />
                    </div>
                    <div class="mb-4 flex justify-between">
                        <div class="w-1/2 mr-2">
                            <label for="expiryDate" class="block text-gray-700">Expiry Date</label>
                            <input type="text" id="expiryDate" placeholder="MM/YY"
                                class="w-full border border-gray-300 rounded px-3 py-2" disabled />
                        </div>
                        <div class="w-1/2">
                            <label for="cvv" class="block text-gray-700">CVV</label>
                            <input type="text" id="cvv" placeholder="123"
                                class="w-full border border-gray-300 rounded px-3 py-2" disabled />
                        </div>
                    </div>
                    <div class="mb-4">
                        <label for="cardHolderName" class="block text-gray-700">Card Holder Name</label>
                        <input type="text" id="cardHolderName" placeholder="John Doe"
                            class="w-full border border-gray-300 rounded px-3 py-2" disabled />
                    </div>
                </div>
                <div class="mb-4">
                    <label for="amount" class="block text-gray-700">Amount to Pay</label>
                    <input type="number" :value="amount" id="amount"
                        class="w-full border border-gray-300 rounded px-3 py-2" disabled />
                </div>
                <div class="mb-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Pay Now
                    </button>
                    <button type="button" @click="closeModal"
                        class="ml-2 bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400">
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts">
import { defineComponent, ref, PropType } from 'vue';
import PaymentService from '@/services/paymentService';

export default defineComponent({
    props: {
        isVisible: {
            type: Boolean as PropType<boolean>,
            required: true
        },
        bookingId: {
            type: Number as PropType<number>,
            required: true
        },
        userId: {
            type: Number as PropType<number>,
            required: true
        },
        amount: {
            type: Number as PropType<number>,
            required: true
        }
    },
    setup(props, { emit }) {
        const paymentMethod = ref<string>('Credit Card');

        const handlePayment = async () => {
            try {
                await PaymentService.processPayment({
                    userId: props.userId,
                    bookingId: props.bookingId,
                    amount: props.amount,
                    paymentMethod: paymentMethod.value
                });
                alert('Payment processed successfully!');
                emit('close');
            } catch (err) {
                alert('Failed to process payment');
                console.error('Payment error:', err);
            }
        };

        const closeModal = () => {
            emit('close');
        };

        return {
            paymentMethod,
            handlePayment,
            closeModal
        };
    }
});
</script>

<style scoped>
/* Add any specific styling for the modal here */
</style>
