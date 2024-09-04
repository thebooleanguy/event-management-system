<!-- src/routes/ticket/ticket-settings/[eventId].svelte -->
<script>
	import TicketSettings from '$lib/components/ticket/TicketSettings.svelte';
	export let eventId;
</script>

<TicketSettings {eventId} />
