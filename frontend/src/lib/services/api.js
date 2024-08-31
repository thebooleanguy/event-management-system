const API_URL = 'http://localhost:8082/api'; // Adjust this to your backend URL

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

export const api = {
    login: (email, password) =>
        fetchWithAuth('/users/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        }),

    register: (userData) =>
        fetchWithAuth('/users/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        }),

    logout: () =>
        fetchWithAuth('/users/logout', { method: 'POST' }),

    getUserProfile: () =>
        fetchWithAuth('/users/profile'),

    updateUserProfile: (userData) =>
        fetchWithAuth('/users/profile', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        }),

    // Event methods
    getAllEvents: () =>
        fetchWithAuth('/events/findAll'),

    getEventById: (id) =>
        fetchWithAuth(`/events/find/${id}`),

    findEventByTitle: (title) =>
        fetchWithAuth(`/events/findByTitle?title=${encodeURIComponent(title)}`),

    createEvent: (eventData) =>
        fetchWithAuth('/events/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        }),

    updateEvent: (id, eventData) =>
        fetchWithAuth(`/events/update/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        }),

    deleteEvent: (id) =>
        fetchWithAuth(`/events/delete/${id}`, { method: 'DELETE' })
};
