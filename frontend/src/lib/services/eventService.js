// src/services/eventService.js

const EVENT_API_URL = 'http://localhost:8082/api/events'; // Base URL for event-related endpoints

async function fetchWithAuth(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    if (token) {
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };
    }
    const response = await fetch(`${EVENT_API_URL}${endpoint}`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

export const eventService = {
    getAllEvents: () =>
        fetchWithAuth('/findAll'),

    getEventById: (id) =>
        fetchWithAuth(`/find/${id}`),

    findEventByTitle: (title) =>
        fetchWithAuth(`/findByTitle?title=${encodeURIComponent(title)}`),

    createEvent: (eventData) =>
        fetchWithAuth('/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        }),

    updateEvent: (id, eventData) =>
        fetchWithAuth(`/update/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        }),

    deleteEvent: (id) =>
        fetchWithAuth(`/delete/${id}`, { method: 'DELETE' })
};
