<script>
  import { page } from '$app/stores';
  import EventDetail from '$lib/components/event/EventDetails.svelte';

  $: eventId = $page.params.id;
</script>

<svelte:head>
  <title>Event Details - ANYEVENT.LK</title>
</svelte:head>

<EventDetail {eventId} />