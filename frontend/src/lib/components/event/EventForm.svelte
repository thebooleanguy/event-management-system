<script>
  import { api } from '$lib/services/api';

  export let event = {
    title: '',
    description: '',
    date: '',
    location: '',
    category: 'OTHER'
  };
  export let onSubmit;

  let error = '';

  async function handleSubmit() {
    try {
      if (event.id) {
        await api.updateEvent(event.id, event);
      } else {
        await api.createEvent(event);
      }
      onSubmit();
    } catch (err) {
      error = 'Failed to save event';
    }
  }
</script>

<form on:submit|preventDefault={handleSubmit} class="bg-white rounded-lg shadow-md p-6">
  <div class="mb-4">
    <label for="title" class="block text-gray-700 font-bold mb-2">Title</label>
    <input type="text" id="title" bind:value={event.title} required class="w-full px-3 py-2 border rounded-md" />
  </div>
  <div class="mb-4">
    <label for="description" class="block text-gray-700 font-bold mb-2">Description</label>
    <textarea id="description" bind:value={event.description} required class="w-full px-3 py-2 border rounded-md"></textarea>
  </div>
  <div class="mb-4">
    <label for="date" class="block text-gray-700 font-bold mb-2">Date</label>
    <input type="date" id="date" bind:value={event.date} required class="w-full px-3 py-2 border rounded-md" />
  </div>
  <div class="mb-4">
    <label for="location" class="block text-gray-700 font-bold mb-2">Location</label>
    <input type="text" id="location" bind:value={event.location} required class="w-full px-3 py-2 border rounded-md" />
  </div>
  <div class="mb-4">
    <label for="category" class="block text-gray-700 font-bold mb-2">Category</label>
    <select id="category" bind:value={event.category} class="w-full px-3 py-2 border rounded-md">
      <option value="MUSIC">Music</option>
      <option value="THEATER">Theater</option>
      <option value="CONCERT">Concert</option>
      <option value="SPORT">Sport</option>
      <option value="CONFERENCE">Conference</option>
      <option value="OTHER">Other</option>
    </select>
  </div>
  {#if error}
    <p class="text-red-500 mb-4">{error}</p>
  {/if}
  <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
    {event.id ? 'Update' : 'Create'} Event
  </button>
</form>