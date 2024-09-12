<script>
	import { onMount } from 'svelte';
	import { ticketService } from '$lib/services/ticketService';
	import { eventService } from '$lib/services/eventService';
	import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let allTickets = [];
	let error = '';
	let isLoading = true;

	onMount(async () => {
		try {
			allTickets = await ticketService.getAllTickets();

			// Fetch event titles for the bookings
			allTickets = await Promise.all(
				allTickets.map(async (ticket) => {
					try {
						const event = await eventService.getEventById(ticket.eventId);
						return { ...ticket, eventTitle: event.title }; // Change 'eventName' to 'eventTitle'
					} catch (err) {
						console.error('Error fetching event:', err);
						return ticket; // Return ticket without eventTitle if there's an error
					}
				})
			);
		} catch (err) {
			error = 'Failed to fetch all bookings';
			console.error(err);
		} finally {
			isLoading = false;
		}
	});

	async function cancelTicket(ticketId) {
		try {
			await ticketService.cancelTicket(ticketId);
			allTickets = allTickets.filter((ticket) => ticket.id !== ticketId);
			alert('Ticket canceled successfully!');
		} catch (err) {
			error = 'Failed to cancel ticket';
			console.error(err);
		}
	}
</script>

<div class="p-8">
	<h2 class="text-2xl font-bold text-gray-800 mb-4">All Tickets</h2>

	{#if isLoading}
		<p>Loading...</p>
	{:else if error}
		<p class="text-red-500">{error}</p>
	{:else if allTickets.length === 0}
		<p class="text-gray-600">No bookings found.</p>
	{:else}
		<table class="min-w-full bg-white rounded-lg shadow-md overflow-hidden">
			<thead class="bg-gray-100">
				<tr>
					<th class="py-3 px-6 text-left">Event Title</th>
					<th class="py-3 px-6 text-left">User ID</th>
					<!-- <th class="py-3 px-6 text-left">Seat Number</th> -->
					<th class="py-3 px-6 text-left">Total Tickets</th>
					<th class="py-3 px-6 text-left">Total Price</th>
					<th class="py-3 px-6 text-center">Action</th>
				</tr>
			</thead>
			<tbody>
				{#each allTickets as ticket (ticket.id)}
					<tr class="border-b">
						<td class="py-4 px-6">{ticket.eventTitle}</td>
						<td class="py-4 px-6">{ticket.userId}</td>
						<!-- <td class="py-4 px-6">{ticket.seatNumber}</td> -->
						<td class="py-4 px-6">{ticket.totalTickets}</td>
						<td class="py-4 px-6">{ticket.totalPrice}</td>
						<td class="py-4 px-6 text-center">
							<button
								class="text-red-600 hover:text-red-800 focus:outline-none"
								on:click={() => cancelTicket(ticket.id)}
							>
								<FontAwesomeIcon icon={faTrashAlt} />
							</button>
						</td>
					</tr>
				{/each}
			</tbody>
		</table>
	{/if}
</div>
