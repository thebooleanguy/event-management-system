// src/services/userService.js

const USER_API_URL = 'http://localhost:8081/api/users'; // Base URL for user-related endpoints

async function fetchWithAuth(endpoint, options = {}) {
    let token = '';
    if (typeof window !== 'undefined' && window.localStorage) {
        token = localStorage.getItem('token');
    }
    const headers = new Headers(options.headers || {});
    if (token) {
        headers.set('Authorization', 'Bearer ' + token);
    }
    const response = await fetch(`${USER_API_URL}${endpoint}`, {
        ...options,
        headers,
    });
    
    if (response.ok) {
        return response.json();
    } else if (response.status === 403) {
        throw new Error('Forbidden: You do not have permission to access this resource.');
    } else {
        const errorText = await response.text();
        console.error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
    }
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

    logout: async () => {
        try {
            await fetchWithAuth('/logout', { method: 'POST' });
        } catch (err) {
            console.error('Logout failed:', err);
        } finally {
            localStorage.removeItem('token'); // Remove the token
            user.logout(); // Clear user from the store
        }
    },

    getUserProfile: () =>
        fetchWithAuth('/profile'),

    updateUserProfile: (userData) =>
        fetchWithAuth('/profile', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        }),

        updateUserName: (email, newName) =>
            fetchWithAuth(`/update-name?email=${encodeURIComponent(email)}&newName=${encodeURIComponent(newName)}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' }
            }),

            deleteUser: async (email) => {
                try {
                    await fetchWithAuth(`/delete?email=${encodeURIComponent(email)}`, { method: 'DELETE' });
                } catch (err) {
                    console.error('Delete failed:', err);
                    throw err; // Re-throw to handle in the component
                }
            }
}
;
