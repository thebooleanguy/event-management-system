<template>
    <div class="max-w-3xl mx-auto p-4 bg-background-gray">
        <div v-if="isLoading" class="text-center p-4">
            <p class="text-header-black">Loading notifications...</p>
        </div>

        <div v-else-if="notifications.length === 0" class="text-center p-4">
            <p class="text-header-black">No notifications found.</p>
        </div>

        <div v-else class="space-y-8 p-4">
            <h2 class="text-lg font-semibold text-header-black mb-4">Notifications</h2>

            <!-- Unread notifications -->
            <div v-if="unreadNotifications.length > 0">
                <h3 class="text-md font-semibold text-header-black mb-2">Unread</h3>
                <div v-for="notification in unreadNotifications" :key="notification.id"
                    class="bg-card-white rounded-lg shadow-md p-6 mb-4">
                    <div class="flex justify-between items-center mb-4">
                        <h3 class="text-lg font-semibold text-header-black">{{ notification.content }}</h3>
                        <button @click="markAsRead(notification.id)"
                            class="bg-accent-blue text-white p-2 rounded hover:bg-dark-blue focus:outline-none focus:ring-2 focus:ring-accent-blue">
                            <font-awesome-icon icon="check" size="lg" />
                        </button>
                    </div>
                    <p class="text-header-black text-sm">Received: {{ new Date(notification.date).toLocaleString() }}
                    </p>
                </div>
            </div>

            <!-- Read notifications -->
            <div v-if="readNotifications.length > 0">
                <h3 class="text-md font-semibold text-header-black mb-2">Read</h3>
                <div v-for="notification in readNotifications" :key="notification.id"
                    class="bg-card-white rounded-lg shadow-md p-6 mb-4">
                    <div class="flex justify-between items-center mb-4">
                        <h3 class="text-lg font-semibold text-header-black">{{ notification.content }}</h3>
                        <button @click="markAsUnread(notification.id)"
                            class="bg-button-yellow text-header-black p-2 rounded hover:bg-button-yellow focus:outline-none focus:ring-2 focus:ring-button-yellow">
                            <font-awesome-icon icon="envelope-open" size="lg" />
                        </button>
                    </div>
                    <p class="text-header-black text-sm">Received: {{ new Date(notification.date).toLocaleString() }}
                    </p>
                </div>
            </div>
        </div>

        <div v-if="error" class="text-center p-4">
            <p class="text-header-black">{{ error }}</p>
        </div>
    </div>
</template>

<script lang="ts">

import { defineComponent, ref, onMounted, computed } from 'vue';
import { notificationService } from '@/services/notificationService';
import { userService } from '@/services/userService';

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCheck, faEnvelopeOpen } from '@fortawesome/free-solid-svg-icons';

library.add(faCheck, faEnvelopeOpen);

interface Notification {
    id: string;
    content: string;
    date: string;
    readStatus: boolean;
}

export default defineComponent({
    name: 'Notifications',
    components: {
        FontAwesomeIcon,
    },
    setup() {
        const notifications = ref<Notification[]>([]);
        const error = ref<string>('');
        const isLoading = ref<boolean>(true);

        const getCurrentUserId = async (): Promise<string | null> => {
            try {
                return await userService.getUserId();
            } catch (err) {
                console.error('Failed to fetch user ID:', err);
                return null;
            }
        };

        const loadNotifications = async () => {
            try {
                const userId = await getCurrentUserId();
                if (userId) {
                    notifications.value = await notificationService.getNotificationsForUser(userId);
                } else {
                    error.value = 'User not logged in';
                }
            } catch (err) {
                error.value = 'Failed to load notifications';
            } finally {
                isLoading.value = false;
            }
        };

        const markAsRead = async (notificationId: string) => {
            try {
                await notificationService.markNotificationAsRead(notificationId);
                notifications.value = notifications.value.map(notification =>
                    notification.id === notificationId
                        ? { ...notification, readStatus: true }
                        : notification
                );
            } catch (err) {
                error.value = 'Failed to mark notification as read';
            }
        };

        const markAsUnread = async (notificationId: string) => {
            try {
                await notificationService.markNotificationAsUnread(notificationId);
                notifications.value = notifications.value.map(notification =>
                    notification.id === notificationId
                        ? { ...notification, readStatus: false }
                        : notification
                );
            } catch (err) {
                error.value = 'Failed to mark notification as unread';
            }
        };

        const unreadNotifications = computed(() => notifications.value.filter(notification => !notification.readStatus));
        const readNotifications = computed(() => notifications.value.filter(notification => notification.readStatus));

        onMounted(loadNotifications);

        return {
            notifications,
            error,
            isLoading,
            markAsRead,
            markAsUnread,
            unreadNotifications,
            readNotifications
        };
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
