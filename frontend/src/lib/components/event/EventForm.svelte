<script>
	import { onMount } from 'svelte';
	import { eventService } from '$lib/services/eventService';
	import { userService } from '$lib/services/userService';
	import { goto } from '$app/navigation';

	export let event = {
		title: '',
		description: '',
		date: '',
		location: '',
		category: 'OTHER',
		imageUrl: '/images/default.jpg' // default image URL
	};
	export let onSubmit;

	let error = '';
	let isLoading = false;
	let categories = []; // Initialize categories as an empty array

	// Utility function to convert category names to Title Case
	function toTitleCase(str) {
		return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
	}

	async function getCurrentUserId() {
		try {
			return await userService.getUserId();
		} catch (err) {
			console.error('Failed to fetch user ID:', err);
			return null;
		}
	}

	async function handleSubmit() {
		isLoading = true;
		try {
			const organizerId = await getCurrentUserId();
			if (!organizerId) {
				error = 'User not logged in';
				return;
			}

			const eventData = { ...event };
			let newEventId;

			if (event.id) {
				await eventService.updateEvent(event.id, eventData);
				newEventId = event.id;
			} else {
				const createdEvent = await eventService.createEvent(eventData, organizerId);
				newEventId = createdEvent.id;
			}

			goto(`/bookings/ticket-settings/${newEventId}`);
		} catch (err) {
			error = 'Failed to save event';
			console.error(err);
		} finally {
			isLoading = false;
		}
	}

	// Fetch categories on component mount
	onMount(async () => {
		try {
			categories = await eventService.getCategories();
		} catch (err) {
			error = 'Failed to load categories';
			console.error(err);
		}
	});
</script>

<form
	on:submit|preventDefault={handleSubmit}
	class="bg-card-white rounded-lg shadow-md p-8 max-w-lg mx-auto"
>
	<h2 class="text-2xl font-bold text-header-black mb-6 text-center">
		{event.id ? 'Update Event' : 'Create New Event'}
	</h2>

	<!-- Form Fields -->
	<div class="space-y-4">
		<!-- Title -->
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

		<!-- Description -->
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

		<!-- Date -->
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

		<!-- Location -->
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

		<!-- Category -->
		<div>
			<label for="category" class="block text-gray-700 font-semibold mb-1">Category</label>
			<select
				id="category"
				bind:value={event.category}
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
			>
				{#each categories as category}
					<option value={category}>{toTitleCase(category)}</option>
				{/each}
			</select>
		</div>

		<!-- Image URL -->
		<div>
			<label for="imageUrl" class="block text-gray-700 font-semibold mb-1">Event Image URL</label>
			<input
				type="text"
				id="imageUrl"
				bind:value={event.imageUrl}
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent-blue"
				placeholder="Enter image URL"
			/>
			{#if event.imageUrl}
				<img src={event.imageUrl} alt="Event Image" class="w-full mt-4" />
			{/if}
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
