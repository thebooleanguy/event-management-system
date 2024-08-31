<script>
  import { onMount } from 'svelte';
  import EventList from '$lib/components/event/EventList.svelte';
  import Categories from '$lib/components/Categories.svelte';
  import SocialMediaIcons from '$lib/components/SocialMediaIcons.svelte';
  import { eventService } from '$lib/services/eventService.js';
  import EventHeader from '$lib/components/event/EventHeader.svelte';

  let username = '';
  let events = [];
  let categories = [];

  async function loadEvents() {
    try {
      events = await eventService.getAllEvents();
    } catch (error) {
      console.error('Error fetching events:', error);
    }
  }

  function handleSearch(filteredEvents) {
    events = filteredEvents;
  }

  onMount(() => {
    loadEvents();
  });
</script>

<svelte:head>
  <title>ANYEVENT.LK - Event Management System</title>
</svelte:head>

<div class="min-h-screen bg-gray-100">
  <main class="container mx-auto px-4 py-8">
    <EventHeader {username} on:search={event => handleSearch(event.detail)} />

    <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-8">
      <div class="col-span-2 lg:col-span-3">
        <EventList {events} />
      </div>
      <div class="space-y-4">
        <Categories {categories} />
        <div class="mt-8 flex justify-center space-x-4">
          <SocialMediaIcons />
        </div>
      </div>
    </div>
  </main>
</div>
