<script>
	import { onMount } from 'svelte';
	import { ticketService } from '$lib/services/ticketService';
	import { eventService } from '$lib/services/eventService';
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

			// Fetch event titles for the tickets
			userTickets = await Promise.all(
				userTickets.map(async (ticket) => {
					try {
						const event = await eventService.getEventById(ticket.eventId);
						return { ...ticket, eventTitle: event.title };
					} catch (err) {
						console.error('Error fetching event:', err);
						return ticket;
					}
				})
			);
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
		<div class="space-y-4">
			{#each userTickets as ticket (ticket.id)}
				<div class="relative ticket-card">
					<div class="ticket-info">
						<div class="ticket-title">{ticket.eventTitle}</div>
						<div class="ticket-details">Total Tickets: {ticket.totalTickets}</div>
						<div class="ticket-details">Total Price: ${ticket.totalPrice}</div>
					</div>
					<div class="ticket-action">
						<button on:click={() => cancelTicket(ticket.id)}>
							<FontAwesomeIcon icon={faTrashAlt} class="text-lg mr-2" /> Cancel
						</button>
					</div>
				</div>
			{/each}
		</div>
	{/if}
</div>

<style>
	.ticket-card {
		background: linear-gradient(135deg, #f0f0f0, #ffffff);
		border: 2px dashed #d1d5db;
		border-radius: 12px;
		box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
		padding: 16px;
		margin-bottom: 16px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.ticket-info {
		display: flex;
		flex-direction: column;
	}

	.ticket-title {
		font-size: 1.25rem;
		font-weight: 600;
		color: #1f2937;
	}

	.ticket-details {
		color: #6b7280;
		font-size: 0.875rem;
	}

	.ticket-action {
		text-align: center;
	}

	.ticket-action button {
		background-color: #f87171;
		color: white;
		padding: 8px 16px;
		border: none;
		border-radius: 8px;
		cursor: pointer;
		transition: background-color 0.3s;
	}

	.ticket-action button:hover {
		background-color: #ef4444;
	}

	.ticket-card::before,
	.ticket-card::after {
		content: '';
		width: 20px;
		height: 20px;
		background: white;
		border-radius: 50%;
		border: 2px dashed #d1d5db;
		position: absolute;
	}

	.ticket-card::before {
		top: -10px;
		left: -10px;
	}

	.ticket-card::after {
		bottom: -10px;
		right: -10px;
	}
</style>
