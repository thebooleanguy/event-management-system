import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

// Import Components
import AdminTickets from "@/components/ticket/AdminTickets.vue";
import TicketForm from "@/components/ticket/TicketForm.vue";
import UserTickets from "@/components/ticket/UserTickets.vue";
import TicketSettings from "@/components/ticket/TicketSettings.vue";

const routes: Array<RouteRecordRaw> = [
    // Define Routes here
    {
        path: "/bookings/admin-tickets",
        name: "AdminTickets",
        component: AdminTickets,
    },

    { path: "/bookings/book", component: TicketForm },

    { path: "/bookings/my-bookings", component: UserTickets },

    {
        path: "/bookings/ticket-settings/:eventId",
        component: TicketSettings,
        name: "ticket-settings", // Optional: Name for this route
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
