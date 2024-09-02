<script>
	import { onMount } from 'svelte';
	import { ticketService } from '$lib/services/ticketService';
	import { userService } from '$lib/services/userService';
	import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let userId = '';
	let userTickets = [];
	let error = '';
	let isLoading = true;

	onMount(async () => {
		try {
			userId = await userService.getUserId();
			userTickets = await ticketService.getTicketsByUser(userId);
		} catch (err) {
			error = 'Failed to fetch user tickets';
			console.error(err);
		} finally {
			isLoading = false;
		}
	});

	async function cancelTicket(ticketId) {
		try {
			await ticketService.cancelTicket(ticketId);
			userTickets = userTickets.filter((ticket) => ticket.id !== ticketId);
			alert('Ticket canceled successfully!');
		} catch (err) {
			error = 'Failed to cancel ticket';
			console.error(err);
		}
	}
</script>

<div class="p-8">
	<h2 class="text-2xl font-bold text-gray-800 mb-4">My Tickets</h2>

	{#if isLoading}
		<p>Loading...</p>
	{:else if error}
		<p class="text-red-500">{error}</p>
	{:else if userTickets.length === 0}
		<p class="text-gray-600">No tickets found.</p>
	{:else}
		<table class="min-w-full bg-white rounded-lg shadow-md overflow-hidden">
			<thead class="bg-gray-100">
				<tr>
					<th class="py-3 px-6 text-left">Event Name</th>
					<th class="py-3 px-6 text-left">Seat Number</th>
					<th class="py-3 px-6 text-left">Total Tickets</th>
					<th class="py-3 px-6 text-left">Total Price</th>
					<th class="py-3 px-6 text-center">Action</th>
				</tr>
			</thead>
			<tbody>
				{#each userTickets as ticket (ticket.id)}
					<tr class="border-b">
						<td class="py-4 px-6">{ticket.eventName}</td>
						<td class="py-4 px-6">{ticket.seatNumber}</td>
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
