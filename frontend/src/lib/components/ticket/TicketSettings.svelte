<!-- src/lib/components/ticket/TicketSettings.svelte -->
<script>
	import { onMount } from 'svelte';
	import { ticketService } from '$lib/services/ticketService';

	export let eventId;
	let availableTickets = 0;
	let unitPrice = 0;
	let error = '';

	async function fetchTicketData() {
		try {
			const data = await ticketService.getTicketData(eventId);
			availableTickets = data.availableTickets;
			unitPrice = data.unitPrice;
		} catch (err) {
			error = 'Failed to fetch ticket data';
			console.error(err);
		}
	}

	onMount(() => {
		if (eventId) {
			fetchTicketData();
		}
	});

	async function saveChanges() {
		try {
			await ticketService.setAvailableTickets(eventId, availableTickets);
			await ticketService.setUnitPrice(eventId, unitPrice);
			alert('Ticket settings updated successfully!');
		} catch (err) {
			error = 'Failed to update ticket settings';
			console.error(err);
		}
	}
</script>

{#if error}
	<p class="text-red-500">{error}</p>
{/if}

<div class="p-6">
	<h2 class="text-2xl font-bold mb-4">Ticket Settings</h2>

	<div class="mb-4">
		<label for="availableTickets" class="block text-gray-700">Available Tickets</label>
		<input
			type="number"
			id="availableTickets"
			bind:value={availableTickets}
			class="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
		/>
	</div>
	<div class="mb-4">
		<label for="unitPrice" class="block text-gray-700">Unit Price</label>
		<input
			type="number"
			id="unitPrice"
			bind:value={unitPrice}
			class="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
		/>
	</div>

	<div class="flex justify-end space-x-4">
		<button on:click={saveChanges} class="bg-blue-500 text-white px-4 py-2 rounded">
			Save Changes
		</button>
	</div>
</div>
