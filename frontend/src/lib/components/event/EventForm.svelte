<script>
	import { eventService } from '$lib/services/eventService';
	import { userService } from '$lib/services/userService'; // Import userService to get user info
	import { goto } from '$app/navigation'; // Import goto for navigation

	export let event = {
		title: '',
		description: '',
		date: '',
		location: '',
		category: 'OTHER'
	};
	export let onSubmit;

	let error = '';
	let isLoading = false; // To handle loading state

	async function getCurrentUserId() {
		try {
			return await userService.getUserId();
		} catch (err) {
			console.error('Failed to fetch user ID:', err);
			return null; // Return null if there's an error or user is not logged in
		}
	}

	async function handleSubmit() {
		isLoading = true; // Set loading state
		try {
			const organizerId = await getCurrentUserId();
			const eventData = { ...event };

			let newEventId;
			if (event.id) {
				await eventService.updateEvent(event.id, eventData);
				newEventId = event.id;
			} else {
				const createdEvent = await eventService.createEvent(eventData, organizerId);
				newEventId = createdEvent.id; // Assuming the response contains the new event ID
			}

			// Redirect to TicketSettings with the event ID
			goto(`/tickets/ticket-settings/${newEventId}`);
		} catch (err) {
			error = 'Failed to save event';
			console.error(err);
		} finally {
			isLoading = false; // Reset loading state
		}
	}
</script>

<form
	on:submit|preventDefault={handleSubmit}
	class="bg-card-white rounded-lg shadow-md p-8 max-w-lg mx-auto"
>
	<h2 class="text-2xl font-bold text-header-black mb-6 text-center">
		{event.id ? 'Update Event' : 'Create New Event'}
	</h2>

	<div class="space-y-4">
		<div>
			<label for="title" class="block text-gray-700 font-semibold mb-1">Title</label>
			<input
				type="text"
				id="title"
				bind:value={event.title}
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
				placeholder="Enter event title"
			/>
		</div>

		<div>
			<label for="description" class="block text-gray-700 font-semibold mb-1">Description</label>
			<textarea
				id="description"
				bind:value={event.description}
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
				placeholder="Enter event description"
			></textarea>
		</div>

		<div>
			<label for="date" class="block text-gray-700 font-semibold mb-1">Date</label>
			<input
				type="date"
				id="date"
				bind:value={event.date}
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
			/>
		</div>

		<div>
			<label for="location" class="block text-gray-700 font-semibold mb-1">Location</label>
			<input
				type="text"
				id="location"
				bind:value={event.location}
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
				placeholder="Enter event location"
			/>
		</div>

		<div>
			<label for="category" class="block text-gray-700 font-semibold mb-1">Category</label>
			<select
				id="category"
				bind:value={event.category}
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
			>
				<option value="MUSIC">Music</option>
				<option value="THEATER">Theater</option>
				<option value="CONCERT">Concert</option>
				<option value="SPORT">Sport</option>
				<option value="CONFERENCE">Conference</option>
				<option value="OTHER">Other</option>
			</select>
		</div>
	</div>

	{#if error}
		<p class="text-red-500 mt-4 text-center">{error}</p>
	{/if}

	<div class="mt-6 flex justify-center">
		<button
			type="submit"
			class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-accent-blue"
			disabled={isLoading}
		>
			{isLoading ? 'Saving...' : event.id ? 'Update' : 'Create'} Event
		</button>
	</div>
</form>
