<script>
  import { onMount } from 'svelte';
  import { api } from '$lib/services/api';

  export let eventId;
  let event = null;
  let error = '';

  onMount(async () => {
    try {
      event = await api.getEventById(eventId);
    } catch (err) {
      error = 'Failed to load event details';
    }
  });
</script>

{#if event}
  <div class="bg-white rounded-lg overflow-hidden shadow-md p-6">
    <h2 class="text-3xl font-bold mb-4">{event.title}</h2>
    <p class="text-xl text-gray-600 mb-4">{event.date} - {event.location}</p>
    <p class="text-gray-700 mb-4">{event.description}</p>
    <p class="text-sm text-gray-500">Category: {event.category}</p>
  </div>
{:else if error}
  <p class="text-red-500">{error}</p>
{:else}
  <p>Loading...</p>
{/if}