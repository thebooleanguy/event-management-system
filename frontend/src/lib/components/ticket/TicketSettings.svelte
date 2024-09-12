<script>
	import { onMount } from 'svelte';
	import { eventService } from '$lib/services/eventService';

	export let eventId;
	let availableTickets = 0;
	let unitPrice = 0;
	let error = '';
	let isLoading = false;

	async function fetchTicketData() {
		if (!eventId) {
			error = 'Event ID is not defined';
			return;
		}

		isLoading = true;
		try {
			const [tickets, price] = await Promise.all([
				eventService.getAvailableTickets(eventId),
				eventService.getUnitPrice(eventId)
			]);
			availableTickets = tickets;
			unitPrice = price;
		} catch (err) {
			error = 'Failed to fetch ticket data';
			console.error('Error fetching ticket data:', err);
		} finally {
			isLoading = false;
		}
	}

	onMount(() => {
		if (eventId) {
			fetchTicketData();
		}
	});

	async function saveChanges() {
		if (!eventId) {
			error = 'Event ID is not defined';
			return;
		}

		isLoading = true;
		try {
			await Promise.all([
				eventService.setAvailableTickets(eventId, availableTickets),
				eventService.setUnitPrice(eventId, unitPrice)
			]);
			alert('Ticket settings updated successfully!');
		} catch (err) {
			error = 'Failed to update ticket settings';
			console.error('Error updating ticket settings:', err);
		} finally {
			isLoading = false;
		}
	}
</script>

{#if error}
	<p class="text-red-500">{error}</p>
{/if}

{#if isLoading}
	<p class="text-gray-500">Loading...</p>
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
			min="0"
			disabled={isLoading}
		/>
	</div>
	<div class="mb-4">
		<label for="unitPrice" class="block text-gray-700">Unit Price</label>
		<input
			type="number"
			id="unitPrice"
			bind:value={unitPrice}
			class="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
			min="0"
			disabled={isLoading}
		/>
	</div>

	<div class="flex justify-end space-x-4">
		<button
			on:click={saveChanges}
			class="bg-blue-500 text-white px-4 py-2 rounded"
			disabled={isLoading}
		>
			{#if isLoading}
				Saving...
			{:else}
				Save Changes
			{/if}
		</button>
	</div>
</div>
