<script>
	import { onMount } from 'svelte';
	import { eventService } from '$lib/services/eventService';
	import TicketForm from '$lib/components/ticket/TicketForm.svelte';
	import TicketSettings from '$lib/components/ticket/TicketSettings.svelte'; // Import TicketSettings component
	import Modal from '$lib/components/Modal.svelte';

	export let eventId;
	let event = null;
	let error = '';
	let showBookingForm = false;
	let showTicketSettings = false; // State for showing the TicketSettings modal

	onMount(async () => {
		try {
			event = await eventService.getEventById(eventId);
		} catch (err) {
			error = 'Failed to load event details';
		}
	});

	function handleBookTicket() {
		showBookingForm = true;
	}

	function handleOpenTicketSettings() {
		showTicketSettings = true;
	}

	function closeModal() {
		showBookingForm = false;
		showTicketSettings = false; // Close TicketSettings modal
	}
</script>

<svelte:head>
	<title>{event ? event.title : 'Event Details'} - ANYEVENT.LK</title>
</svelte:head>

{#if event}
	<div class="bg-white rounded-lg overflow-hidden shadow-lg p-6 max-w-4xl mx-auto mt-8">
		<img
			src={event.imageUrl || 'placeholder-image-url'}
			alt={event.title}
			class="w-full h-64 object-cover rounded-md mb-6"
		/>
		<h2 class="text-4xl font-bold mb-4">{event.title}</h2>
		<p class="text-xl text-gray-600 mb-4">{event.date} - {event.location}</p>
		<p class="text-gray-800 mb-6">{event.description}</p>
		<p class="text-sm text-gray-500">Category: {event.category}</p>

		<div class="mt-6 text-center space-x-4">
			<button
				on:click={handleBookTicket}
				class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
			>
				Book Ticket
			</button>
			<button
				on:click={handleOpenTicketSettings}
				class="bg-green-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
			>
				Manage Ticket Settings
			</button>
		</div>

		<!-- Modal for TicketForm -->
		<Modal isOpen={showBookingForm} onClose={closeModal}>
			<TicketForm {eventId} eventName={event.title} />
		</Modal>

		<!-- Modal for TicketSettings -->
		<Modal isOpen={showTicketSettings} onClose={closeModal}>
			<TicketSettings {eventId} />
		</Modal>
	</div>
{:else if error}
	<div
		class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative max-w-4xl mx-auto mt-8"
		role="alert"
	>
		<strong class="font-bold">Error:</strong>
		<span class="block sm:inline">{error}</span>
	</div>
{:else}
	<div class="max-w-4xl mx-auto mt-8 text-center">
		<p class="text-gray-600">Loading...</p>
	</div>
{/if}
