import axios from 'axios';

const EVENT_API_URL = 'http://localhost:8082/api/events/'; // Base URL for event-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
	baseURL: EVENT_API_URL,
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

export const eventService = {
	getAllEvents: () => apiClient.get('/').then((response) => response.data),

	getEventById: (id) => apiClient.get(`/${id}`).then((response) => response.data),

	findEventByTitle: (title) =>
		apiClient.get('/', { params: { title } }).then((response) => response.data),

	searchEvents: (title, category) =>
		apiClient.get('/search', { params: { title, category } }).then((response) => response.data),

	createEvent: (eventData) => apiClient.post('/', eventData).then((response) => response.data),

	updateEvent: (id, eventData) =>
		apiClient.put(`/${id}`, eventData).then((response) => response.data),

	deleteEvent: (id) => apiClient.delete(`/${id}`),

	getCategories: () => apiClient.get('/categories').then((response) => response.data),

	getAvailableTickets: (id) =>
		apiClient.get(`/available-tickets/${id}`).then((response) => response.data),

	setAvailableTickets: (id, availableTickets) =>
		apiClient.put(`/available-tickets/${id}`, { availableTickets }),

	getUnitPrice: (id) => apiClient.get(`/unit-price/${id}`).then((response) => response.data),

	setUnitPrice: (id, unitPrice) => apiClient.put(`/unit-price/${id}`, { unitPrice })
};
