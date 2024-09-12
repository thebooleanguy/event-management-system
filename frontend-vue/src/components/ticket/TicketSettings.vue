<template>
    <div class="p-6">
        <h2 class="text-2xl font-bold mb-4">Ticket Settings</h2>

        <!-- Error Message -->
        <p v-if="error" class="text-red-500">{{ error }}</p>

        <!-- Loading Message -->
        <p v-if="isLoading" class="text-gray-500">Loading...</p>

        <!-- Available Tickets Input -->
        <div class="mb-4">
            <label for="availableTickets" class="block text-gray-700">Available Tickets</label>
            <input type="number" id="availableTickets" v-model.number="availableTickets"
                class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" min="0" :disabled="isLoading" />
        </div>

        <!-- Unit Price Input -->
        <div class="mb-4">
            <label for="unitPrice" class="block text-gray-700">Unit Price</label>
            <input type="number" id="unitPrice" v-model.number="unitPrice"
                class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" min="0" :disabled="isLoading" />
        </div>

        <!-- Save Button -->
        <div class="flex justify-end space-x-4">
            <button @click="saveChanges" class="bg-blue-500 text-white px-4 py-2 rounded" :disabled="isLoading">
                {{ isLoading ? 'Saving...' : 'Save Changes' }}
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService';

export default defineComponent({
    props: {
        eventId: {
            type: String,
            required: true
        }
    },
    setup(props) {
        const availableTickets = ref<number>(0);
        const unitPrice = ref<number>(0);
        const error = ref<string>('');
        const isLoading = ref<boolean>(false);

        async function fetchTicketData() {
            if (!props.eventId) {
                error.value = 'Event ID is not defined';
                return;
            }

            isLoading.value = true;
            try {
                const [tickets, price] = await Promise.all([
                    eventService.getAvailableTickets(props.eventId),
                    eventService.getUnitPrice(props.eventId)
                ]);
                availableTickets.value = tickets;
                unitPrice.value = price;
            } catch (err) {
                error.value = 'Failed to fetch ticket data';
                console.error('Error fetching ticket data:', err);
            } finally {
                isLoading.value = false;
            }
        }

        onMounted(() => {
            if (props.eventId) {
                fetchTicketData();
            }
        });

        async function saveChanges() {
            if (!props.eventId) {
                error.value = 'Event ID is not defined';
                return;
            }

            isLoading.value = true;
            try {
                await Promise.all([
                    eventService.setAvailableTickets(props.eventId, availableTickets.value),
                    eventService.setUnitPrice(props.eventId, unitPrice.value)
                ]);
                alert('Ticket settings updated successfully!');
            } catch (err) {
                error.value = 'Failed to update ticket settings';
                console.error('Error updating ticket settings:', err);
            } finally {
                isLoading.value = false;
            }
        }

        return {
            availableTickets,
            unitPrice,
            error,
            isLoading,
            saveChanges
        };
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>