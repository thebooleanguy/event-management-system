<template>
    <div v-if="isAdmin">
        <div class="p-6 bg-white rounded-lg shadow-md">
            <h2 class="text-2xl font-bold mb-4">Manage Notifications</h2>
            <div class="mb-4">
                <input v-model="newNotification.content" type="text" placeholder="Notification Content"
                    class="w-full p-2 border rounded">
                <button @click="sendNotification"
                    class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                    <i class="fas fa-paper-plane"></i> Send Notification
                </button>
            </div>
            <div>
                <h3 class="text-xl font-semibold mb-2">Notifications</h3>
                <ul>
                    <li v-for="notification in notifications" :key="notification.id" class="mb-2 p-2 border rounded">
                        <span>{{ notification.content }}</span>
                        <button @click="deleteNotification(notification.id)"
                            class="ml-2 text-red-500 hover:text-red-700">
                            <i class="fas fa-trash"></i>
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import { computed, ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { notificationService } from '@/services/notificationService';

export default {
    setup() {
        const store = useStore();
        const user = computed(() => store.state.user);
        const isAdmin = computed(() => user.value?.role === 'ADMIN');
        const notifications = ref([]);
        const newNotification = ref({ content: '', userId: user.value?.id });

        const fetchNotifications = async () => {
            try {
                notifications.value = await notificationService.findAllNotifications();
            } catch (error) {
                console.error('Failed to fetch notifications:', error);
            }
        };

        const sendNotification = async () => {
            try {
                await notificationService.sendNotification(newNotification.value);
                Swal.fire('Success', 'Notification sent successfully!', 'success');
                fetchNotifications();
            } catch (error) {
                Swal.fire('Error', 'Failed to send notification', 'error');
            }
        };

        const deleteNotification = async (id) => {
            try {
                await notificationService.deleteNotification(id);
                Swal.fire('Deleted', 'Notification deleted successfully!', 'success');
                fetchNotifications();
            } catch (error) {
                Swal.fire('Error', 'Failed to delete notification', 'error');
            }
        };

        onMounted(fetchNotifications);

        return {
            isAdmin,
            notifications,
            newNotification,
            sendNotification,
            deleteNotification,
        };
    },
};
</script>

<style scoped>
/* Add any additional styling here */
</style>