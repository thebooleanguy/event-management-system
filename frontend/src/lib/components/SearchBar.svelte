<script>
  let searchQuery = '';
</script>

<div class="flex">
  <select class="bg-blue-200 text-gray-800 px-4 py-2 rounded-l-md">
    <option>All events</option>
    <!-- Add more options as needed -->
  </select>
  <input
    type="text"
    bind:value={searchQuery}
    placeholder="search for an event"
    class="flex-grow px-4 py-2 border-t border-b border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
  />
  <button class="bg-gray-200 px-4 py-2 rounded-r-md hover:bg-gray-300">
    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
    </svg>
  </button>
</div>
