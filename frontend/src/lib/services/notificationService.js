const NOTIFICATION_API_URL = 'http://localhost:8085/api/notifications'; // Base URL for notification-related endpoints

async function fetchWithAuth(endpoint, options = {}) {
	const token = localStorage.getItem('token');
	if (token) {
		options.headers = {
			...options.headers,
			Authorization: `Bearer ${token}`
		};
	}
	const response = await fetch(`${NOTIFICATION_API_URL}${endpoint}`, options);
	if (!response.ok) {
		throw new Error(`HTTP error! Status: ${response.status}`);
	}
	return response.json();
}

export const notificationService = {
	// Send a new notification
	sendNotification: (notification) => {
		return fetchWithAuth('/send', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(notification)
		});
	},

	// Retrieve all notifications for a specific user
	getNotificationsForUser: (userId) => {
		return fetchWithAuth(`/user?userId=${userId}`);
	},

	// Retrieve a specific notification by ID
	getNotificationById: (id) => {
		return fetchWithAuth(`/${id}`);
	},

	// Mark a notification as read
	markNotificationAsRead: (id) => {
		return fetchWithAuth(`/${id}/read`, { method: 'PATCH' });
	}
};
