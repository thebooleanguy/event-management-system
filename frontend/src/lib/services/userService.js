import axios from 'axios';
import { user } from '$lib/stores/userStore'; // Import the user store

const USER_API_URL = 'http://localhost:8081/api/users/'; // Base URL for user-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
	baseURL: USER_API_URL,
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

export const userService = {
	login: async (email, password) => {
		try {
			const response = await apiClient.post('/login', { email, password });
			const data = response.data;
			localStorage.setItem('token', data.token); // Store the token in localStorage
			user.login(data.user); // Update the store with the user data
			return data; // Return both user data and token
		} catch (error) {
			console.error(`Login failed! status: ${error.response?.status}, message: ${error.message}`);
			throw new Error(`Login failed! status: ${error.response?.status}, message: ${error.message}`);
		}
	},

	register: (userData) => apiClient.post('/register', userData).then((response) => response.data),

	logout: async () => {
		try {
			await apiClient.post('/logout');
		} catch (error) {
			console.error('Logout failed:', error);
		} finally {
			localStorage.removeItem('token'); // Remove the token from localStorage
			user.logout(); // Use the logout method from your custom store
		}
	},

	getUserProfile: () => apiClient.get('/profile').then((response) => response.data),

	getUserId: async () => {
		try {
			const response = await apiClient.get('/profile');
			return response.data?.id; // Adjust based on the actual profile response structure
		} catch (error) {
			console.error('Failed to get user ID:', error);
			return null; // Return null if there's an error or the user is not logged in
		}
	},

	updateUserProfile: (userData) =>
		apiClient.put('/profile', userData).then((response) => response.data),

	updateUserName: (email, newName) =>
		apiClient
			.put(`/update-name`, null, {
				params: { email, newName }
			})
			.then((response) => response.data),

	deleteUser: async (email) => {
		try {
			await apiClient.delete(`/delete`, {
				params: { email }
			});
		} catch (error) {
			console.error('Delete failed:', error);
			throw error; // Re-throw to handle in the component
		}
	}
};
