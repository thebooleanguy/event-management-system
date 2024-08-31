<script>
  import { onMount } from 'svelte';
  import SearchBar from '$lib/components/SearchBar.svelte';
  import { createEventDispatcher } from 'svelte';
  import { userService } from '$lib/services/userService'; // Import userService

  const dispatch = createEventDispatcher();

  let userProfile = null;
  let error = '';
  let loading = true;

  onMount(async () => {
    try {
      const profile = await userService.getUserProfile(); // Fetch user profile data
      userProfile = {
        name: profile.name,
        email: profile.email,
        role: profile.role
      };
    } catch (err) {
      console.error('Error fetching user profile:', err);
      error = 'Failed to load user profile.';
    } finally {
      loading = false;
    }
  });

  function handleSearch(event) {
    dispatch('search', event.detail); // Dispatch search results to parent
  }
</script>

<div class="bg-white shadow-md p-4 mb-6 rounded-md flex items-center justify-between">
  <div class="flex items-center flex-grow">
    <h2 class="text-2xl font-semibold">
      {#if loading}
        Loading...
      {:else if userProfile}
        Welcome {userProfile.name}
      {:else}
        Welcome
      {/if}
    </h2>
    <div class="flex-grow flex justify-center mx-4">
      <SearchBar on:search={handleSearch} />
    </div>
  </div>

  <div class="flex space-x-4 items-center">
    <a href="/events" class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
      My Events
    </a>
    <a href="/events/create" class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
      Create Event
    </a>
  </div>
</div>
