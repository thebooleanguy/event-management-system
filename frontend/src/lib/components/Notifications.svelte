<!-- src/lib/components/Notifications.svelte -->
<script>
	import { onMount } from 'svelte';
	import { notificationService } from '$lib/services/notificationService';
	import { userService } from '$lib/services/userService'; // Import userService to get user info

	let notifications = [];
	let error = '';
	let isLoading = true;

	async function getCurrentUserId() {
		try {
			return await userService.getUserId();
		} catch (err) {
			console.error('Failed to fetch user ID:', err);
			return null; // Return null if there's an error or user is not logged in
		}
	}

	onMount(async () => {
		try {
			const userId = await getCurrentUserId();
			if (userId) {
				notifications = await notificationService.getNotificationsForUser(userId);
			} else {
				error = 'User not logged in';
			}
		} catch (err) {
			error = 'Failed to load notifications';
		} finally {
			isLoading = false;
		}
	});

	async function markAsRead(notificationId) {
		try {
			await notificationService.markNotificationAsRead(notificationId);
			notifications = notifications.map((notification) =>
				notification.id === notificationId ? { ...notification, readStatus: true } : notification
			);
		} catch (err) {
			error = 'Failed to mark notification as read';
		}
	}
</script>

<svelte:head>
	<title>Notifications - ANYEVENT.LK</title>
</svelte:head>

{#if isLoading}
	<div class="text-center p-4">
		<p class="text-gray-500">Loading notifications...</p>
	</div>
{/if}

{#if !isLoading && notifications.length === 0}
	<div class="text-center p-4">
		<p class="text-gray-500">No notifications found.</p>
	</div>
{/if}

{#if !isLoading && notifications.length > 0}
	<div class="space-y-4 p-4">
		{#each notifications as notification}
			<div class="p-4 bg-white rounded-lg shadow-md">
				<h3 class="text-lg font-semibold">{notification.title}</h3>
				<p class="text-gray-600">{notification.content}</p>
				<p class="text-sm text-gray-500">
					Received: {new Date(notification.date).toLocaleString()}
				</p>
				{#if !notification.readStatus}
					<button
						on:click={() => markAsRead(notification.id)}
						class="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
					>
						Mark as Read
					</button>
				{/if}
			</div>
		{/each}
	</div>
{/if}

{#if error}
	<div class="text-center p-4">
		<p class="text-red-500">{error}</p>
	</div>
{/if}
