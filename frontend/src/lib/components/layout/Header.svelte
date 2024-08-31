<script>
  import { user } from '$lib/stores/userStore';
  import { userService } from '$lib/services/userService';
  import { goto } from '$app/navigation';

  async function handleLogout() {
    try {
      await userService.logout();
      goto('/login'); // Redirect to login page after logout
    } catch (err) {
      console.error('Logout failed:', err);
    }
  }
</script>

<header class="bg-gray-800 text-white p-4">
  <div class="container mx-auto flex justify-between items-center">
    <a href="/" class="text-2xl font-bold">ANYEVENT.LK</a>
    <div class="flex items-center space-x-4">
      <!-- Remove the username display from the main header -->
      {#if $user}
        <a href="/users/profile" class="p-2 rounded-full bg-gray-700 hover:bg-gray-600">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
        </a>
        <button on:click={handleLogout} class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600">
          Logout
        </button>
      {:else}
        <a href="/users/login" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
          Login
        </a>
        <a href="/users/register" class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600">
          Register
        </a>
      {/if}
    </div>
  </div>
</header>
