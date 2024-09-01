const TICKET_API_URL = 'http://localhost:8083/api/tickets'; // Base URL for ticket-related endpoints

async function fetchWithAuth(endpoint, options = {}) {
	let token = '';
	if (typeof window !== 'undefined' && window.localStorage) {
		token = localStorage.getItem('token');
	}
	const headers = new Headers(options.headers || {});
	if (token) {
		headers.set('Authorization', 'Bearer ' + token);
	}
	const response = await fetch(`${TICKET_API_URL}${endpoint}`, {
		...options,
		headers
	});

	if (response.ok) {
		if (response.headers.get('Content-Type')?.includes('application/json')) {
			return response.json();
		}
		return; // Return nothing if no content
	} else if (response.status === 403) {
		throw new Error('Forbidden: You do not have permission to access this resource.');
	} else {
		const errorText = await response.text();
		console.error(`HTTP error! status: ${response.status}, message: ${errorText}`);
		throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
	}
}

export const ticketService = {
	getAllTickets: () => fetchWithAuth('/'),

	getTicketById: (id) => fetchWithAuth(`/${id}`),

	getTicketsByUser: (userId) => fetchWithAuth(`/user/${userId}`),

	cancelTicket: (id) => fetchWithAuth(`/delete/${id}`, { method: 'DELETE' }),

	bookTicket: (ticketData) =>
		fetchWithAuth('/book', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(ticketData)
		}),

	getAvailableTickets: (eventId) => fetchWithAuth(`/available/${eventId}`),

	setAvailableTickets: (eventId, availableTickets) =>
		fetchWithAuth(`/available/${eventId}?availableTickets=${availableTickets}`, {
			method: 'PUT'
		}),

	getUnitPrice: (eventId) => fetchWithAuth(`/unit-price/${eventId}`),

	setUnitPrice: (eventId, unitPrice) =>
		fetchWithAuth(`/unit-price/${eventId}?unitPrice=${unitPrice}`, {
			method: 'PUT'
		}),

	getTicketData: async (eventId) => {
		const availableTickets = await ticketService.getAvailableTickets(eventId);
		const unitPrice = await ticketService.getUnitPrice(eventId);
		return { availableTickets, unitPrice };
	}
};
