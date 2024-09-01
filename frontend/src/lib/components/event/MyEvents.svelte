<script>
	import { onMount } from 'svelte';
	import { eventService } from '$lib/services/eventService';
	import { userService } from '$lib/services/userService'; // Import userService to get user info

	export let events = [];
	let filteredEvents = [];
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
				// Fetch all events
				const allEvents = await eventService.getAllEvents();
				// Filter events to show only those created by the current user
				filteredEvents = allEvents.filter((event) => event.organizerId === userId);
			} else {
				error = 'User not logged in';
			}
		} catch (err) {
			error = 'Failed to load events';
		} finally {
			isLoading = false;
		}
	});
</script>

{#if isLoading}
	<p class="text-gray-500">Loading events...</p>
{/if}

{#if !isLoading && filteredEvents.length === 0}
	<p class="text-gray-500">No events found for you.</p>
{/if}

<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
	{#each filteredEvents as event}
		<a href={`/events/${event.id}`} class="bg-white rounded-lg overflow-hidden shadow-md">
			<img
				src={event.imageUrl || 'placeholder-image-url'}
				alt={event.title}
				class="w-full h-48 object-cover"
			/>
			<div class="p-4">
				<h3 class="text-xl font-semibold mb-2">{event.title}</h3>
				<p class="text-gray-600 mb-2">{event.date} - {event.location}</p>
				<p class="text-sm text-gray-500">{event.description.slice(0, 100)}...</p>
			</div>
		</a>
	{/each}
</div>

{#if error}
	<p class="text-red-500 mt-4">{error}</p>
{/if}
