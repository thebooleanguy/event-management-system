<script>
	import { onMount } from 'svelte';
	import { eventService } from '$lib/services/eventService';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();
	let searchQuery = '';
	let selectedCategory = 'All';
	let categories = [];

	// Utility function to convert category names to Title Case
	function toTitleCase(str) {
		return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
	}

	async function fetchCategories() {
		try {
			categories = await eventService.getCategories(); // Fetch categories from backend
			categories = ['All', ...categories]; // Add "All" option to the categories
		} catch (err) {
			console.error('Failed to fetch categories:', err);
		}
	}

	async function handleSearch() {
		try {
			const filteredEvents = await eventService.searchEvents(searchQuery, selectedCategory);
			dispatch('search', filteredEvents);
		} catch (err) {
			console.error('Failed to fetch events:', err);
		}
	}

	onMount(() => {
		fetchCategories(); // Fetch categories on mount
	});
</script>

<div class="flex mb-4">
	<select bind:value={selectedCategory} class="bg-blue-200 text-gray-800 px-4 py-2 rounded-l-md">
		{#each categories as category}
			<option value={category}>{toTitleCase(category)}</option>
		{/each}
	</select>
	<input
		type="text"
		bind:value={searchQuery}
		placeholder="Search for an event"
		class="flex-grow px-4 py-2 border-t border-b border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
		on:input={handleSearch}
	/>
	<button class="bg-gray-200 px-4 py-2 rounded-r-md hover:bg-gray-300" on:click={handleSearch}>
		<svg
			xmlns="http://www.w3.org/2000/svg"
			class="h-6 w-6 text-gray-600"
			fill="none"
			viewBox="0 0 24 24"
			stroke="currentColor"
		>
			<path
				stroke-linecap="round"
				stroke-linejoin="round"
				stroke-width="2"
				d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
			/>
		</svg>
	</button>
</div>
