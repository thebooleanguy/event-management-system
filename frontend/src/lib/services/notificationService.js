import axios from 'axios';

const NOTIFICATION_API_URL = 'http://localhost:8085/api/notifications/'; // Base URL for notification-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
	baseURL: NOTIFICATION_API_URL,
	headers: {
		'Content-Type': 'application/json'
	}
});

// Add an interceptor to include the Authorization header
apiClient.interceptors.request.use(
	(config) => {
		const token = localStorage.getItem('token');
		if (token) {
			config.headers.Authorization = `Bearer ${token}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

export const notificationService = {
	// Send a new notification
	sendNotification: async (notification) => {
		try {
			const response = await apiClient.post('/send', notification);
			return response.data;
		} catch (error) {
			console.error(
				`Failed to send notification! status: ${error.response?.status}, message: ${error.message}`
			);
			throw new Error(
				`Failed to send notification! status: ${error.response?.status}, message: ${error.message}`
			);
		}
	},

	// Retrieve all notifications for a specific user
	getNotificationsForUser: async (userId) => {
		try {
			const response = await apiClient.get(`/user`, { params: { userId } });
			return response.data;
		} catch (error) {
			console.error(
				`Failed to get notifications for user! status: ${error.response?.status}, message: ${error.message}`
			);
			throw new Error(
				`Failed to get notifications for user! status: ${error.response?.status}, message: ${error.message}`
			);
		}
	},

	// Retrieve a specific notification by ID
	getNotificationById: async (id) => {
		try {
			const response = await apiClient.get(`/${id}`);
			return response.data;
		} catch (error) {
			console.error(
				`Failed to get notification by ID! status: ${error.response?.status}, message: ${error.message}`
			);
			throw new Error(
				`Failed to get notification by ID! status: ${error.response?.status}, message: ${error.message}`
			);
		}
	},

	// Mark a notification as read
	markNotificationAsRead: async (id) => {
		try {
			await apiClient.patch(`/${id}/read`);
		} catch (error) {
			console.error(
				`Failed to mark notification as read! status: ${error.response?.status}, message: ${error.message}`
			);
			throw new Error(
				`Failed to mark notification as read! status: ${error.response?.status}, message: ${error.message}`
			);
		}
	}
};
