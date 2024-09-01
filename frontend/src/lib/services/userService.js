// src/services/userService.js

import { user } from '$lib/stores/userStore'; // Import the user store

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
		headers
	});

	if (response.ok) {
		// Only parse JSON if there is content
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

export const userService = {
	login: async (email, password) => {
		const response = await fetch(`${USER_API_URL}/login`, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ email, password })
		});

		if (response.ok) {
			const data = await response.json();
			localStorage.setItem('token', data.token); // Store the token in localStorage
			user.login(data.user); // Update the store with the user data
			return data; // Return both user data and token
		} else {
			const errorText = await response.text();
			console.error(`Login failed! status: ${response.status}, message: ${errorText}`);
			throw new Error(`Login failed! status: ${response.status}, message: ${errorText}`);
		}
	},

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
			localStorage.removeItem('token'); // Remove the token from localStorage
			user.logout(); // Use the logout method from your custom store
		}
	},

	getUserProfile: () => fetchWithAuth('/profile'),

	getUserId: async () => {
		try {
			const userProfile = await fetchWithAuth('/profile');
			return userProfile?.id; // Adjust based on the actual profile response structure
		} catch (err) {
			console.error('Failed to get user ID:', err);
			return null; // Return null if there's an error or the user is not logged in
		}
	},

	updateUserProfile: (userData) =>
		fetchWithAuth('/profile', {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(userData)
		}),

	updateUserName: (email, newName) =>
		fetchWithAuth(
			`/update-name?email=${encodeURIComponent(email)}&newName=${encodeURIComponent(newName)}`,
			{
				method: 'PUT',
				headers: { 'Content-Type': 'application/json' }
			}
		),

	deleteUser: async (email) => {
		try {
			await fetchWithAuth(`/delete?email=${encodeURIComponent(email)}`, { method: 'DELETE' });
		} catch (err) {
			console.error('Delete failed:', err);
			throw err; // Re-throw to handle in the component
		}
	}
};
