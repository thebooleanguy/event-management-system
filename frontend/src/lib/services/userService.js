// src/services/userService.js

const USER_API_URL = 'http://localhost:8081/api/users'; // Base URL for user-related endpoints

async function fetchWithAuth(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    if (token) {
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };
    }
    const response = await fetch(`${USER_API_URL}${endpoint}`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

export const userService = {
    login: (email, password) =>
        fetchWithAuth('/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        }),

    register: (userData) =>
        fetchWithAuth('/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        }),

    logout: () =>
        fetchWithAuth('/logout', { method: 'POST' }),

    getUserProfile: () =>
        fetchWithAuth('/profile'),

    updateUserProfile: (userData) =>
        fetchWithAuth('/profile', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        })
};
