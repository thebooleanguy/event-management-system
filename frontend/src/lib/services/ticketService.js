import axios from 'axios';

const TICKET_API_URL = 'http://localhost:8083/api/tickets/'; // Base URL for ticket-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
	baseURL: TICKET_API_URL,
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

export const ticketService = {
	getAllTickets: () => apiClient.get('/all').then((response) => response.data),

	getTicketById: (id) => apiClient.get(`/${id}`).then((response) => response.data),

	getTicketsByUser: (userId) => apiClient.get(`/user/${userId}`).then((response) => response.data),

	cancelTicket: (id) => apiClient.delete(`/delete/${id}`),

	bookTicket: (ticketData) => apiClient.post('/book', ticketData).then((response) => response.data),

	getAvailableTickets: (eventId) =>
		apiClient.get(`/available-tickets/${eventId}`).then((response) => response.data),

	setAvailableTickets: (eventId, availableTickets) =>
		apiClient.put(`/available-tickets/${eventId}`, null, { params: { availableTickets } }),

	getUnitPrice: (eventId) =>
		apiClient.get(`/unit-price/${eventId}`).then((response) => response.data),

	setUnitPrice: (eventId, unitPrice) =>
		apiClient.put(`/unit-price/${eventId}`, null, { params: { unitPrice } }),

	getTicketData: async (eventId) => {
		const availableTickets = await ticketService.getAvailableTickets(eventId);
		const unitPrice = await ticketService.getUnitPrice(eventId);
		return { availableTickets, unitPrice };
	}
};
