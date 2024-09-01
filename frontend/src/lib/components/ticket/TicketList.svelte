<script>
    import { onMount } from 'svelte';
    import { ticketService } from '$lib/services/ticketService';
    import { userService } from '$lib/services/userService'; 

    let tickets = [];
    let error = '';

    onMount(async () => {
        try {
            const userId = await userService.getUserId();
            tickets = await ticketService.getTicketsByUser(userId);
        } catch (err) {
            error = 'Failed to load tickets';
        }
    });
</script>

<div class="space-y-4">
    {#if tickets.length > 0}
        {#each tickets as ticket}
            <div class="bg-white rounded-lg shadow-md p-4 mb-4">
                <h3 class="text-xl font-semibold mb-2">{ticket.eventName}</h3>
                <p class="text-gray-600 mb-2">Seat Number: {ticket.seatNumber}</p>
                <p class="text-gray-600 mb-2">Total Tickets: {ticket.totalTickets}</p>
                <p class="text-gray-600 mb-2">Total Price: ${ticket.totalPrice}</p>
                <p class="text-gray-600 mb-2">Booking Date: {ticket.bookingDate}</p>
                <button
                    on:click={() => cancelTicket(ticket.id)}
                    class="bg-red-600 text-white font-semibold px-4 py-2 rounded-md hover:bg-red-700"
                >
                    Cancel
                </button>
            </div>
        {/each}
    {:else}
        <p class="text-gray-600">No tickets found.</p>
    {/if}
    {#if error}
        <p class="text-red-500">{error}</p>
    {/if}
</div>

<script>
    async function cancelTicket(id) {
        try {
            await ticketService.cancelTicket(id);
            tickets = tickets.filter(ticket => ticket.id !== id);
        } catch (err) {
            error = `Failed to cancel ticket: ${err.message}`;
        }
    }
</script>
