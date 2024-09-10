<script>
	import { onMount } from 'svelte';
	import { user } from '$lib/stores/userStore';
	import { userService } from '$lib/services/userService';
	import { goto } from '$app/navigation';
	import {
		faUser,
		faSignOutAlt,
		faSignInAlt,
		faBell,
		faTicketAlt
	} from '@fortawesome/free-solid-svg-icons'; // Import new icons
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let isLoggedIn = false;

	// Subscribe to user store
	$: {
		isLoggedIn = $user !== null;
	}

	onMount(async () => {
		// Check login status on mount
		try {
			const profile = await userService.getUserProfile();
			if (profile) {
				user.login(profile); // Update store with user profile
			} else {
				user.logout(); // Ensure the store reflects logout
			}
		} catch (err) {
			console.error('Error fetching user profile:', err);
			user.logout(); // Ensure store is updated on error
		}
	});

	async function handleLogout() {
		try {
			await userService.logout();
			goto('/'); // Redirect to login page after logout
		} catch (err) {
			console.error('Logout failed:', err);
		}
	}
</script>

<header class="bg-[#1A1A1A] text-[#FFFFFF] p-4">
	<div class="container mx-auto flex justify-between items-center">
		<a href="/" class="text-2xl font-bold">ANYEVENT.LK</a>
		<div class="flex items-center space-x-4">
			{#if isLoggedIn}
				<!-- User Profile Button -->
				<a
					href="/users/profile"
					class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center"
				>
					<FontAwesomeIcon icon={faUser} class="text-[#FFFFFF] text-lg" />
				</a>

				<!-- Notifications Button -->
				<a
					href="/notifications"
					class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center"
				>
					<FontAwesomeIcon icon={faBell} class="text-[#FFFFFF] text-lg" />
				</a>

				<!-- My Tickets Button -->
				<a
					href="/tickets/my-tickets"
					class="bg-[#1E40AF] hover:bg-[#1E3A8A] p-2 rounded-full flex items-center justify-center"
				>
					<FontAwesomeIcon icon={faTicketAlt} class="text-[#FFFFFF] text-lg" />
				</a>

				<!-- Logout Button -->
				<button
					on:click={handleLogout}
					class="bg-red-600 text-white p-2 rounded-full flex items-center justify-center hover:bg-red-700"
				>
					<FontAwesomeIcon icon={faSignOutAlt} class="text-lg" />
				</button>
			{:else}
				<a
					href="/users/login"
					class="bg-blue-600 text-white p-2 rounded-full flex items-center justify-center hover:bg-blue-700"
				>
					<FontAwesomeIcon icon={faSignInAlt} class="text-lg" />
				</a>
			{/if}
		</div>
	</div>
</header>
