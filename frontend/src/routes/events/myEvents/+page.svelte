<script>
	import { onMount } from 'svelte';
	import EventList from '$lib/components/event/MyEvents.svelte';
	import SearchBar from '$lib/components/SearchBar.svelte'; // Use the SearchBar component
	import { eventService } from '$lib/services/eventService.js';

	let events = [];
	let initialEvents = []; // Store initial events to reset if needed

	async function loadEvents() {
		try {
			initialEvents = await eventService.getAllEvents();
			events = initialEvents;
		} catch (error) {
			console.error('Error fetching events:', error);
		}
	}

	function handleSearch(filteredEvents) {
		events = filteredEvents;
	}

	onMount(() => {
		loadEvents();
	});
</script>

<svelte:head>
	<title>Event List - ANYEVENT.LK</title>
</svelte:head>

<div class="min-h-screen bg-gray-100">
	<main class="container mx-auto px-4 py-8">
		<!-- Use the SearchBar component and pass the search event handler -->
		<SearchBar on:search={(event) => handleSearch(event.detail)} />

		<!-- Display the list of events -->
		<EventList {events} />
	</main>
</div>
