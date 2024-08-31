<script>
  import { onMount } from 'svelte';
  import { eventService } from '$lib/services/eventService';

  export let categories = [];

  let error = '';

  // Utility function to convert category names to Title Case
  function toTitleCase(str) {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
  }

  onMount(async () => {
    try {
      categories = await eventService.getCategories();
    } catch (err) {
      error = 'Failed to load categories';
      console.error(err);
    }
  });
</script>

<div>
  <h3 class="text-xl font-semibold mb-4">Explore Events</h3>
  {#if error}
    <p class="text-red-500">{error}</p>
  {/if}
  <div class="space-y-2">
    {#each categories as category}
      <button class="w-full bg-blue-200 px-4 py-2 rounded-md text-left hover:bg-blue-300">
        {toTitleCase(category)}
      </button>
    {/each}
  </div>
</div>
