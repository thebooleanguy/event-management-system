<script>
  import { goto } from '$app/navigation';
  import EventForm from '$lib/components/event/EventForm.svelte';

  function handleSubmit() {
    goto('/events');
  }
</script>

<svelte:head>
  <title>Create Event - ANYEVENT.LK</title>
</svelte:head>

<!-- <h1 class="text-3xl font-bold mb-6">Create Event</h1> -->
<EventForm onSubmit={handleSubmit} />