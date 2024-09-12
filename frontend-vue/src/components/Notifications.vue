<template>
    <div>
        <div v-if="isLoading" class="text-center p-4">
            <p class="text-gray-500">Loading notifications...</p>
        </div>

        <div v-else-if="notifications.length === 0" class="text-center p-4">
            <p class="text-gray-500">No notifications found.</p>
        </div>

        <div v-else class="space-y-4 p-4">
            <div v-for="notification in notifications" :key="notification.id" class="p-4 bg-white rounded-lg shadow-md">
                <h3 class="text-lg font-semibold">{{ notification.content }}</h3>
                <p class="text-gray-600">Received: {{ new Date(notification.date).toLocaleString() }}</p>
                <button v-if="!notification.readStatus" @click="markAsRead(notification.id)"
                    class="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    Mark as Read
                </button>
            </div>
        </div>

        <div v-if="error" class="text-center p-4">
            <p class="text-red-500">{{ error }}</p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { notificationService } from '@/lib/services/notificationService';
import { userService } from '@/lib/services/userService';

interface Notification {
    id: string;
    content: string;
    date: string;
    readStatus: boolean;
}

export default defineComponent({
    name: 'Notifications',
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

        onMounted(loadNotifications);

        return {
            notifications,
            error,
            isLoading,
            markAsRead
        };
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>