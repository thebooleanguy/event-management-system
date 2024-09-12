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
	// Retrieve all events
	getAllEvents: () => apiClient.get('/all').then((response) => response.data),

	// Retrieve an event by ID
	getEventById: (id) => apiClient.get(`/${id}`).then((response) => response.data),

	// Search for events by title and/or category
	searchEvents: (title, category) =>
		apiClient.get('/search', { params: { title, category } }).then((response) => response.data),

	// Create a new event
	createEvent: (eventData, organizerId) =>
		apiClient.post('/', { ...eventData, organizerId }).then((response) => response.data),

	// Update an event by ID
	updateEvent: (id, eventData) =>
		apiClient.put(`/${id}`, eventData).then((response) => response.data),

	// Delete an event by ID
	deleteEvent: (id) => apiClient.delete(`/${id}`),

	// Retrieve all available event categories
	getCategories: () => apiClient.get('/categories').then((response) => response.data),

	// Retrieve the number of available tickets for an event by ID
	getAvailableTickets: (id) =>
		apiClient.get(`/tickets/available/${id}`).then((response) => response.data),

	// Set the number of available tickets for an event by ID
	setAvailableTickets: (id, availableTickets) =>
		apiClient.put(`/tickets/available/${id}`, null, { params: { availableTickets } }),

	// Retrieve the ticket price (unit price) for an event by ID
	getUnitPrice: (id) => apiClient.get(`/tickets/price/${id}`).then((response) => response.data),

	// Set the ticket price (unit price) for an event by ID
	setUnitPrice: (id, unitPrice) =>
		apiClient.put(`/tickets/price/${id}`, null, { params: { unitPrice } })
};
