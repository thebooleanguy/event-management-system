<script>
  import { onMount } from 'svelte';
  import { eventService } from '$lib/services/eventService';

  let events = [];
  let error = '';

  onMount(async () => {
    try {
      events = await eventService.getAllEvents();
    } catch (err) {
      error = 'Failed to load events';
    }
  });
</script>

<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  {#each events as event}
    <div class="bg-white rounded-lg overflow-hidden shadow-md">
      <img src="placeholder-image-url" alt={event.title} class="w-full h-48 object-cover" />
      <div class="p-4">
        <h3 class="text-xl font-semibold mb-2">{event.title}</h3>
        <p class="text-gray-600 mb-2">{event.date} - {event.location}</p>
        <p class="text-sm text-gray-500">{event.description.slice(0, 100)}...</p>
      </div>
    </div>
  {/each}
</div>

{#if error}
  <p class="text-red-500 mt-4">{error}</p>
{/if}