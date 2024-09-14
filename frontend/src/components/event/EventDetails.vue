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
            <!-- Conditionally display buttons -->
            <button v-if="!isLoggedIn" @click="navigateToLogin"
                class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
                Login to Book Ticket
            </button>
            <button v-if="isLoggedIn && canBookTicket" @click="handleBookTicket"
                class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
                Book Ticket
            </button>
            <button v-if="isLoggedIn && canManageTicketSettings" @click="handleOpenTicketSettings"
                class="bg-green-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500">
                Manage Ticket Settings
            </button>
            <button v-if="isLoggedIn && canEditEvent" @click="handleOpenEditEvent"
                class="bg-yellow-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-yellow-500">
                Edit Event
            </button>
            <button v-if="isLoggedIn && canEditEvent" @click="handleDeleteEvent"
                class="bg-red-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500">
                Delete Event
            </button>
        </div>

        <!-- Modal for TicketForm -->
        <Modal v-if="showBookingForm" :isOpen="showBookingForm" @close="closeModal">
            <TicketForm :eventId="Number(eventId)" :eventTitle="event.title" />
        </Modal>

        <!-- Modal for TicketSettings -->
        <Modal v-if="showTicketSettings" :isOpen="showTicketSettings" @close="closeModal">
            <TicketSettings :eventId="Number(eventId)" />
        </Modal>

        <!-- Modal for EditEventForm -->
        <Modal v-if="showEditEventForm" :isOpen="showEditEventForm" @close="closeModal">
            <EditEventForm :event="event" />
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
import { ref, onMounted, computed } from "vue";
import { eventService } from "@/services/eventService";
import Modal from "@/components/Modal.vue";
import TicketForm from "@/components/ticket/TicketForm.vue";
import TicketSettings from "@/components/ticket/TicketSettings.vue";
import EditEventForm from "@/components/event/EditEventForm.vue";
import { userService } from "@/services/userService";
import { useRouter } from "vue-router";

interface Event {
    id: number;
    title: string;
    description: string;
    date: string;
    location: string;
    category: string;
    imageUrl?: string;
    organizerId: number;
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
        EditEventForm,
    },
    setup(props: { eventId: number }) {
        const event = ref<Event | null>(null);
        const error = ref<string>("");
        const showBookingForm = ref<boolean>(false);
        const showTicketSettings = ref<boolean>(false);
        const showEditEventForm = ref<boolean>(false);
        const currentUserId = ref<number | null>(null);
        const router = useRouter();

        onMounted(async () => {
            try {
                const userId = await userService.getUserId();
                currentUserId.value = userId ? Number(userId) : null;

                const response = await eventService.getEventById(props.eventId);
                event.value = response as Event;
            } catch (err) {
                console.error("Failed to load event details", err);
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

        const handleOpenEditEvent = (): void => {
            showEditEventForm.value = true;
        };

        const handleDeleteEvent = async (): Promise<void> => {
            if (confirm("Are you sure you want to delete this event?")) {
                try {
                    await eventService.deleteEvent(props.eventId);
                    router.push("/");
                } catch (err) {
                    console.error("Failed to delete event", err);
                    error.value = "Failed to delete event";
                }
            }
        };

        const closeModal = (): void => {
            showBookingForm.value = false;
            showTicketSettings.value = false;
            showEditEventForm.value = false;
        };

        const navigateToLogin = (): void => {
            router.push("/users/login");
        };

        const isLoggedIn = computed(() => currentUserId.value !== null);

        const canBookTicket = computed(() => event.value && isLoggedIn.value && currentUserId.value !== event.value.organizerId);

        const canManageTicketSettings = computed(() => event.value && isLoggedIn.value && currentUserId.value === event.value.organizerId);

        const canEditEvent = computed(() => event.value && isLoggedIn.value && currentUserId.value === event.value.organizerId);

        return {
            event,
            error,
            showBookingForm,
            showTicketSettings,
            showEditEventForm,
            handleBookTicket,
            handleOpenTicketSettings,
            handleOpenEditEvent,
            handleDeleteEvent,
            closeModal,
            navigateToLogin,
            isLoggedIn,
            canBookTicket,
            canManageTicketSettings,
            canEditEvent,
        };
    },
};
</script>

<style scoped>
/* Add any component-specific styles here */
</style>
