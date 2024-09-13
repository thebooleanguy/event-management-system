import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { RouteLocationNormalized } from "vue-router";
import Layout from "@/components/layout/Layout.vue";
import Home from "@/components/Home.vue";
import Notifications from "@/components/Notifications.vue";

// Import Booking Components
import AdminTickets from "@/components/ticket/AdminTickets.vue";
import TicketForm from "@/components/ticket/TicketForm.vue";
import UserTickets from "@/components/ticket/UserTickets.vue";
import TicketSettings from "@/components/ticket/TicketSettings.vue";

// Import Event Components
import EventDetails from "@/components/event/EventDetails.vue";
import EventList from "@/components/event/EventList.vue";
import MyEvents from "@/components/event/MyEvents.vue";
import EventForm from "@/components/event/EventForm.vue"; // For event creation

// Import User Components
import LoginForm from "@/components/user/LoginForm.vue";
import RegisterForm from "@/components/user/RegisterForm.vue";
import UserProfile from "@/components/user/UserProfile.vue";

// Import Admin Components
import AdminUserManagement from "@/components/admin/AdminUserManagement.vue";
import Unauthorized from "@/components/admin/Unauthorized.vue";

import { userService } from "@/services/userService";

// Define a route guard to check if the user has an ADMIN role
const requireAdminRole = async (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: (arg?: { name: string }) => void
) => {
    try {
        const profile = await userService.getUserProfile();
        if (profile.role === "ADMIN") {
            next();
        } else {
            next({ name: "Unauthorized" }); // Redirect to an unauthorized page or login
        }
    } catch (error) {
        console.error("Failed to check user role:", error);
        next({ name: "Unauthorized" }); // Redirect to an unauthorized page or login
    }
};

// Define routes
const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        component: Layout,
        children: [
            {
                path: "",
                name: "Home",
                component: Home,
                meta: { title: "Home - ANYEVENT.LK" },
            },
            {
                path: "/bookings/admin-tickets",
                name: "AdminTickets",
                component: AdminTickets,
                meta: { title: "Admin Tickets - ANYEVENT.LK" },
            },
            {
                path: "/bookings/book",
                component: TicketForm,
                meta: { title: "Ticket Form - ANYEVENT.LK" },
            },
            {
                path: "/bookings/my-bookings",
                component: UserTickets,
                meta: { title: "User Tickets - ANYEVENT.LK" },
            },
            {
                path: "/bookings/ticket-settings/:eventId",
                component: TicketSettings,
                name: "ticket-settings",
                meta: { title: "Ticket Settings - ANYEVENT.LK" },
            },
            {
                path: "/events/:eventId",
                component: EventDetails,
                name: "event-details",
                props: true, // Pass `id` as a prop to EventDetails
                meta: { title: "Event Details - ANYEVENT.LK" }, // Set page title
            },
            {
                path: "/events/all",
                component: EventList,
                name: "event-list",
                meta: { title: "Event List - ANYEVENT.LK" },
            },
            {
                path: "/events/create",
                component: EventForm,
                name: "create-event",
                meta: { title: "Create Event - ANYEVENT.LK" },
            },
            {
                path: "/events/myEvents",
                component: MyEvents,
                name: "my-events",
                meta: { title: "My Events - ANYEVENT.LK" },
            },
            {
                path: "/notifications",
                component: Notifications,
                name: "notifications",
                meta: { title: "Notifications - ANYEVENT.LK" },
            },
            {
                path: "/users/login",
                component: LoginForm,
                meta: { title: "Login - ANYEVENT.LK" },
            },
            {
                path: "/users/register",
                component: RegisterForm,
                meta: { title: "Register - ANYEVENT.LK" },
            },
            {
                path: "/users/profile",
                component: UserProfile,
                meta: { title: "User Profile - ANYEVENT.LK" },
            },
            {
                path: "/admin/users",
                name: "AdminUserManagement",
                component: AdminUserManagement,
                beforeEnter: requireAdminRole, // Apply the route guard
                meta: { title: "Admin User Management - ANYEVENT.LK" },
            },
            {
                path: "/unauthorized",
                name: "Unauthorized",
                component: Unauthorized,
                meta: { title: "Unauthorized - ANYEVENT.LK" },
            },
        ],
    },
];

// Update the page title dynamically
const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const defaultTitle = "ANYEVENT.LK";
    document.title = (to.meta.title as string) || defaultTitle;
    next();
});

export default router;
