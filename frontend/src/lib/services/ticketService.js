const API_URL = 'http://localhost:8083/api/tickets'; // Adjust this to your backend URL

async function fetchWithAuth(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    if (token) {
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };
    }
    const response = await fetch(`${API_URL}${endpoint}`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

export const ticketService = {
    // Book a new ticket
    bookTicket: (ticketData) =>
        fetchWithAuth('', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ticketData)
        }),

    // Get all tickets
    getAllTickets: () =>
        fetchWithAuth(''),

    // Get a ticket by ID
    getTicketById: (id) =>
        fetchWithAuth(`/${id}`),

    // Get tickets by user ID
    getTicketsByUser: (userId) =>
        fetchWithAuth(`/user/${userId}`),

    // Cancel a ticket
    cancelTicket: (id) =>
        fetchWithAuth(`/${id}`, { method: 'DELETE' })
};
