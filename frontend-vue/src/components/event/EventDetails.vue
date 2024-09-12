<template>
    <div v-if="event" class="bg-white rounded-lg overflow-hidden shadow-lg p-6 max-w-4xl mx-auto mt-8">
        <img :src="event.imageUrl || 'placeholder-image-url'" :alt="event.title"
            class="w-full h-64 object-cover rounded-md mb-6" />
        <h2 class="text-4xl font-bold mb-4">{{ event.title }}</h2>
        <p class="text-xl text-gray-600 mb-4">
            {{ event.date }} - {{ event.location }}
        </p>
        <p class="text-gray-800 mb-6">{{ event.description }}</p>
        <p class="text-sm text-gray-500">Category: {{ event.category }}</p>

        <div class="mt-6 text-center space-x-4">
            <button @click="handleBookTicket"
                class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
                Book Ticket
            </button>
            <button @click="handleOpenTicketSettings"
                class="bg-green-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500">
                Manage Ticket Settings
            </button>
        </div>

        <!-- Modal for TicketForm -->
        <Modal v-if="showBookingForm" @close="closeModal">
            <TicketForm :eventId="eventId" :eventName="event.title" />
        </Modal>

        <!-- Modal for TicketSettings -->
        <Modal v-if="showTicketSettings" @close="closeModal">
            <TicketSettings :eventId="eventId" />
        </Modal>
    </div>

    <!-- Error handling -->
    <div v-else-if="error"
        class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative max-w-4xl mx-auto mt-8"
        role="alert">
        <strong class="font-bold">Error:</strong>
        <span class="block sm:inline">{{ error }}</span>
    </div>

    <!-- Loading state -->
    <div v-else class="max-w-4xl mx-auto mt-8 text-center">
        <p class="text-gray-600">Loading...</p>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from "vue";
import { getEventById } from "../services/eventService"; // Import your event service
import Modal from "../components/Modal.vue";
import TicketForm from "../components/ticket/TicketForm.vue";
import TicketSettings from "../components/ticket/TicketSettings.vue";

// Define the Event type for TypeScript
interface Event {
    id: number;
    title: string;
    description: string;
    date: string;
    location: string;
    category: string;
    imageUrl?: string; // imageUrl is optional
}

export default {
    props: {
        eventId: {
            type: [String, Number],
            required: true,
        },
    },
    components: {
        Modal,
        TicketForm,
        TicketSettings,
    },
    setup(props: { eventId: string | number }) {
        // State variables with proper typing
        const event = ref<Event | null>(null); // Event type, null if not loaded yet
        const error = ref<string>(""); // Error message
        const showBookingForm = ref<boolean>(false); // Modal state
        const showTicketSettings = ref<boolean>(false); // Modal state

        // Fetch event details on component mount
        onMounted(async () => {
            try {
                const response = await getEventById(props.eventId);
                event.value = response.data as Event; // Ensure correct type for event data
            } catch (err) {
                error.value = "Failed to load event details";
            }
        });

        // Handlers
        const handleBookTicket = (): void => {
            showBookingForm.value = true;
        };

        const handleOpenTicketSettings = (): void => {
            showTicketSettings.value = true;
        };

        const closeModal = (): void => {
            showBookingForm.value = false;
            showTicketSettings.value = false;
        };

        return {
            event,
            error,
            showBookingForm,
            showTicketSettings,
            handleBookTicket,
            handleOpenTicketSettings,
            closeModal,
        };
    },
};
</script>

<style scoped>
/* You can add any component-specific styles here */
</style>
