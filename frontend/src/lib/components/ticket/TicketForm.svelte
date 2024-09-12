<script>
	import { onMount } from 'svelte';
	import { bookingService } from '$lib/services/bookingService';
	import { userService } from '$lib/services/userService';

	export let eventId = ''; // Passed as a prop
	export let eventTitle = ''; // Passed as a prop
	let userId = '';

	// let seatNumber = '';
	let totalTickets = 1;
	let availableTickets = 0; // This field is kept but not used for validation
	let error = '';
	let isLoading = false;

	onMount(async () => {
		try {
			// Fetch user ID when the component mounts
			userId = await userService.getUserId();
			console.log('Fetched user ID:', userId);

			// 	// Fetch available tickets when the component mounts
			// 	availableTickets = await bookingService.getAvailableTickets(eventId);
			// 	console.log('Available tickets:', availableTickets);
		} catch (err) {
			error = 'Failed to fetch user information or available tickets';
			console.error('Error fetching data:', err);
		}
	});

	async function handleSubmit() {
		isLoading = true;
		try {
			const bookingData = {
				eventId,
				userId,
				// seatNumber,
				totalTickets
			};

			console.log('Submitting booking data:', bookingData);

			await bookingService.bookTicket(bookingData);
			alert('Ticket booked successfully!');
		} catch (err) {
			error =
				err instanceof Error
					? `Booking failed: ${err.message}`
					: 'Booking failed: Unknown error occurred';
			console.error('Booking error:', error);
		} finally {
			isLoading = false;
		}
	}
</script>

<!-- Ticket Booking Form -->
<form
	on:submit|preventDefault={handleSubmit}
	class="bg-white rounded-lg shadow-md p-8 max-w-lg mx-auto"
>
	<h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Book Ticket</h2>

	<!-- Display Event ID and Event Title (Non-editable) -->
	<div class="space-y-4">
		<div>
			<label for="eventId" class="block text-gray-700 font-semibold mb-1">Event ID</label>
			<input
				type="text"
				id="eventId"
				value={eventId}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
			/>
		</div>

		<div>
			<label for="eventTitle" class="block text-gray-700 font-semibold mb-1">Event Title</label>
			<input
				type="text"
				id="eventTitle"
				value={eventTitle}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
			/>
		</div>

		<!-- Display Available Tickets (Read-Only) -->
		<div>
			<label for="availableTickets" class="block text-gray-700 font-semibold mb-1"
				>Available Tickets</label
			>
			<input
				type="text"
				id="availableTickets"
				value={availableTickets}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
			/>
		</div>

		<!-- Editable Fields for Booking
		<div>
			<label for="seatNumber" class="block text-gray-700 font-semibold mb-1">Seat Number</label>
			<input
				type="text"
				id="seatNumber"
				bind:value={seatNumber}
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
				placeholder="Enter seat number"
			/>
		</div> -->

		<div>
			<label for="totalTickets" class="block text-gray-700 font-semibold mb-1">Total Tickets</label>
			<input
				type="number"
				id="totalTickets"
				bind:value={totalTickets}
				min="1"
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
			/>
		</div>
	</div>

	<!-- Display Error Message if any -->
	{#if error}
		<p class="text-red-500 mt-4 text-center">{error}</p>
	{/if}

	<!-- Submit Button -->
	<div class="mt-6 flex justify-center">
		<button
			type="submit"
			class="bg-blue-600 text-white font-semibold px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
			disabled={isLoading}
		>
			{isLoading ? 'Booking...' : 'Book Ticket'}
		</button>
	</div>
</form>
