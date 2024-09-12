<script>
	import { onMount } from 'svelte';
	import { bookingService } from '$lib/services/bookingService';
	import { eventService } from '$lib/services/eventService';
	import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let allBookings = [];
	let error = '';
	let isLoading = true;

	onMount(async () => {
		try {
			const bookings = await bookingService.getAllBookings();

			// Fetch event titles for the bookings
			allBookings = await Promise.all(
				bookings.map(async (booking) => {
					try {
						const event = await eventService.getEventById(booking.eventId);
						return { ...booking, eventTitle: event.title }; // Change 'eventName' to 'eventTitle'
					} catch (err) {
						console.error('Error fetching event:', err);
						return { ...booking, eventTitle: 'Unknown Event' }; // Default to 'Unknown Event' if there's an error
					}
				})
			);
		} catch (err) {
			error = 'Failed to fetch all bookings. Please try again later.';
			console.error('Error fetching bookings:', err);
		} finally {
			isLoading = false;
		}
	});

	async function cancelBooking(bookingId) {
		if (!confirm('Are you sure you want to cancel this booking?')) return;
		try {
			await bookingService.cancelBooking(bookingId);
			allBookings = allBookings.filter((booking) => booking.id !== bookingId);
			alert('Booking canceled successfully!');
		} catch (err) {
			error = 'Failed to cancel booking. Please try again later.';
			console.error('Error canceling booking:', err);
		}
	}
</script>

<div class="p-8">
	<h2 class="text-2xl font-bold text-gray-800 mb-4">All Bookings</h2>

	{#if isLoading}
		<p class="text-gray-600">Loading...</p>
	{:else if error}
		<p class="text-red-500">{error}</p>
	{:else if allBookings.length === 0}
		<p class="text-gray-600">No bookings found.</p>
	{:else}
		<table class="min-w-full bg-white rounded-lg shadow-md overflow-hidden">
			<thead class="bg-gray-100">
				<tr>
					<th class="py-3 px-6 text-left">Event Title</th>
					<th class="py-3 px-6 text-left">User ID</th>
					<th class="py-3 px-6 text-left">Total Tickets</th>
					<th class="py-3 px-6 text-left">Total Price</th>
					<th class="py-3 px-6 text-center">Action</th>
				</tr>
			</thead>
			<tbody>
				{#each allBookings as booking (booking.id)}
					<tr class="border-b">
						<td class="py-4 px-6">{booking.eventTitle}</td>
						<td class="py-4 px-6">{booking.userId}</td>
						<td class="py-4 px-6">{booking.totalTickets}</td>
						<td class="py-4 px-6">${booking.totalPrice.toFixed(2)}</td>
						<td class="py-4 px-6 text-center">
							<button
								class="text-red-600 hover:text-red-800 focus:outline-none"
								on:click={() => cancelBooking(booking.id)}
								aria-label="Cancel booking"
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
