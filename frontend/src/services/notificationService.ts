import axios, {
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
    AxiosHeaders,
} from "axios";

// Define API URL
const NOTIFICATION_API_URL = "http://localhost:8085/api/notifications/"; // Base URL for notification-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: NOTIFICATION_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Add an interceptor to include the Authorization header
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        const token = localStorage.getItem("token");
        if (token) {
            // Ensure headers is defined and then add or overwrite the Authorization header
            config.headers = new AxiosHeaders({
                ...config.headers,
                Authorization: `Bearer ${token}`,
            });
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Define TypeScript types for notification data
interface NotificationData {
    title: string;
    message: string;
    userId?: string;
    // Add other notification fields as needed
}

export const notificationService = {
    // Send a new notification
    sendNotification: async (notification: NotificationData): Promise<any> => {
        try {
            const response = await apiClient.post("/send", notification);
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to send notification! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to send notification! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Retrieve all notifications for a specific user
    getNotificationsForUser: async (userId: string): Promise<any> => {
        try {
            const response = await apiClient.get("/user", {
                params: { userId },
            });
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to get notifications for user! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to get notifications for user! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Retrieve a specific notification by ID
    getNotificationById: async (id: string): Promise<any> => {
        try {
            const response = await apiClient.get(`/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to get notification by ID! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to get notification by ID! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Retrieve all notifications
    findAllNotifications: async (): Promise<any> => {
        try {
            const response = await apiClient.get("/all");
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to retrieve all notifications! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to retrieve all notifications! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Update a notification
    updateNotification: async (
        id: string,
        notification: NotificationData
    ): Promise<any> => {
        try {
            const response = await apiClient.put(`/${id}`, notification);
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to update notification! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to update notification! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Mark a notification as read
    markNotificationAsRead: async (id: string): Promise<any> => {
        try {
            const response = await apiClient.patch(`/${id}/read`);
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to mark notification as read! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to mark notification as read! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Mark a notification as unread
    markNotificationAsUnread: async (id: string): Promise<any> => {
        try {
            const response = await apiClient.patch(`/${id}/unread`);
            return response.data;
        } catch (error: any) {
            console.error(
                `Failed to mark notification as unread! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to mark notification as unread! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Delete a notification by ID
    deleteNotification: async (id: string): Promise<void> => {
        try {
            await apiClient.delete(`/${id}`);
        } catch (error: any) {
            console.error(
                `Failed to delete notification! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to delete notification! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Delete all notifications for a specific user
    deleteAllNotificationsForUser: async (userId: string): Promise<void> => {
        try {
            await apiClient.delete(`/user/${userId}/deleteAll`);
        } catch (error: any) {
            console.error(
                `Failed to delete all notifications for user! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Failed to delete all notifications for user! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },
};
