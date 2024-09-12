<script>
	import { onMount } from 'svelte';
	import { ticketService } from '$lib/services/ticketService';
	import { userService } from '$lib/services/userService';

	export let eventId = ''; // Passed as a prop
	export let eventName = ''; // Passed as a prop
	let userId = '';

	let seatNumber = '';
	let totalTickets = 1;
	let totalPrice = 0;
	let availableTickets = 0;
	let unitPrice = 0;
	let error = '';
	let isLoading = false;

	onMount(async () => {
		try {
			// Fetch user ID when the component mounts
			userId = await userService.getUserId(); // Adjust this method as needed
			console.log('Fetched user ID:', userId);

			// Fetch available bookings and unit price when the component mounts
			availableTickets = await ticketService.getAvailableTickets(eventId);
			console.log('Available bookings:', availableTickets);

			unitPrice = await ticketService.getUnitPrice(eventId);
			console.log('Unit price:', unitPrice);

			// Update total price based on the initial totalTickets value
			updateTotalPrice();
		} catch (err) {
			error = 'Failed to fetch user information, available bookings, or unit price';
			console.error('Error fetching data:', err);
		}
	});

	function updateTotalPrice() {
		totalPrice = totalTickets * unitPrice;
	}

	async function handleSubmit() {
		isLoading = true;
		try {
			// Check if requested bookings exceed available bookings
			if (totalTickets > availableTickets) {
				throw new Error('Not enough bookings available');
			}

			const ticketData = {
				eventId,
				// paymentId: 123, // Placeholder for payment ID
				userId,
				seatNumber,
				totalTickets,
				totalPrice
			};

			console.log('Submitting ticket data:', ticketData); // Debugging line

			await ticketService.bookTicket(ticketData);
			alert('Ticket booked successfully!');
		} catch (err) {
			// Type assertion to check if the error is an instance of Error
			if (err instanceof Error) {
				error = `Booking failed: ${err.message}`;
				console.error('Booking error:', err.message); // Log the error message
			} else {
				error = 'Booking failed: Unknown error occurred';
				console.error('Booking error:', err); // Log the unknown error
			}
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

	<!-- Display Event ID, User ID, and Event Name (Non-editable) -->
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
			<label for="userId" class="block text-gray-700 font-semibold mb-1">User ID</label>
			<input
				type="text"
				id="userId"
				value={userId}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
			/>
		</div>

		<div>
			<label for="eventName" class="block text-gray-700 font-semibold mb-1">Event Name</label>
			<input
				type="text"
				id="eventName"
				value={eventName}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
			/>
		</div>

		<!-- Display Available Tickets -->
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

		<!-- Editable Fields for Booking -->
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
		</div>

		<div>
			<label for="totalTickets" class="block text-gray-700 font-semibold mb-1">Total Tickets</label>
			<input
				type="number"
				id="totalTickets"
				bind:value={totalTickets}
				min="1"
				required
				class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
				on:input={updateTotalPrice}
			/>
		</div>

		<div>
			<label for="totalPrice" class="block text-gray-700 font-semibold mb-1">Total Price</label>
			<input
				type="number"
				id="totalPrice"
				value={totalPrice}
				readonly
				class="w-full px-4 py-3 border border-gray-300 rounded-md bg-gray-100 text-gray-600"
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
