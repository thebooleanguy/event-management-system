import axios from 'axios';

const BOOKING_API_URL = 'http://localhost:8083/api/bookings/'; // Base URL for booking-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
	baseURL: BOOKING_API_URL,
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

export const bookingService = {
	getAllBookings: () => apiClient.get('/all').then((response) => response.data),

	getBookingById: (id) => apiClient.get(`/${id}`).then((response) => response.data),

	getBookingsByUser: (userId) => apiClient.get(`/user/${userId}`).then((response) => response.data),

	cancelBooking: (id) => apiClient.delete(`/${id}`),

	bookTicket: (bookingData) => apiClient.post('/', bookingData).then((response) => response.data)
};
