<script>
  import { user } from '$lib/stores/userStore';
  import { userService } from '$lib/services/userService';
  import { goto } from '$app/navigation';

  async function handleLogout() {
    try {
      await userService.logout();
      user.logout();
      localStorage.removeItem('token');
      goto('/login');
    } catch (error) {
      console.error('Logout failed', error);
    }
  }
</script>

<header class="bg-gray-800 text-white p-4">
  <div class="container mx-auto flex justify-between items-center">
    <a href="/" class="text-2xl font-bold">ANYEVENT.LK</a>
    <div class="flex items-center space-x-4">
      {#if $user}
        <span>Welcome {$user.name}</span>
      {/if}
      <select class="bg-blue-200 text-gray-800 px-4 py-2 rounded-md">
        <option>All events</option>
        <!-- Add more options as needed -->
      </select>
      <div class="relative">
        <input 
          type="text" 
          placeholder="search for an event" 
          class="pl-10 pr-4 py-2 rounded-md bg-white text-gray-800"
        />
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <a href="/events/create" class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
        Create Event
      </a>
      <a href="/events" class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
        My Events
      </a>
      {#if $user}
        <a href="/profile" class="p-2 rounded-full bg-gray-700 hover:bg-gray-600">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
        </a>
        <button on:click={handleLogout} class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600">
          Logout
        </button>
      {:else}
        <a href="/login" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
          Login
        </a>
        <a href="/register" class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600">
          Register
        </a>
      {/if}
    </div>
  </div>
</header>