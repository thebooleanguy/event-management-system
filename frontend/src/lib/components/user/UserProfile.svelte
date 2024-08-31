<script>
  import { onMount } from 'svelte';
  import { user } from '$lib/stores/userStore'; // Import the user store
  import { userService } from '$lib/services/userService'; // Import userService

  let userProfile = { name: '', email: '', role: 'USER' };
  let error = '';
  let loading = true;
  let isEditing = false;
  let newName = '';

  onMount(async () => {
    try {
      const profile = await userService.getUserProfile();
      userProfile = {
        name: profile.name,
        email: profile.email,
        role: profile.role
      };
      newName = profile.name; // Initialize newName with current name
    } catch (err) {
      error = 'Failed to load user profile. Please try again later.';
      console.error('Error fetching user profile:', err);
    } finally {
      loading = false;
    }
  });

  const toggleEdit = () => {
    isEditing = !isEditing;
  };

  const updateUserName = async () => {
    try {
      await userService.updateUserName(userProfile.email, newName);
      userProfile.name = newName; // Update the local state
      toggleEdit(); // Exit edit mode
    } catch (err) {
      error = 'Failed to update username. Please try again later.';
      console.error('Error updating username:', err);
    }
  };

  const deleteUser = async () => {
    try {
      await userService.deleteUser();
      window.location.href = '/login'; // Redirect to login after deletion
    } catch (err) {
      error = 'Failed to delete user. Please try again later.';
      console.error('Error deleting user:', err);
    }
  };
</script>

<svelte:head>
  <title>User Profile</title>
</svelte:head>

<div class="min-h-screen bg-gray-100 p-6">
  <div class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-md">
    <h2 class="text-2xl font-semibold mb-4">User Profile</h2>

    {#if loading}
      <p class="text-gray-500">Loading...</p>
    {:else if error}
      <p class="text-red-500 mb-4">{error}</p>
    {/if}

    {#if !loading && !error}
      <div class="mb-4">
        <label class="block text-gray-700 font-bold mb-2">Name</label>
        {#if isEditing}
          <input
            type="text"
            bind:value={newName}
            class="bg-gray-200 p-2 rounded-md w-full"
            placeholder="Enter new name"
          />
          <button
            on:click={updateUserName}
            class="bg-blue-500 text-white p-2 rounded-md mt-2"
          >
            Save
          </button>
          <button
            on:click={toggleEdit}
            class="bg-gray-500 text-white p-2 rounded-md mt-2 ml-2"
          >
            Cancel
          </button>
        {:else}
          <p class="bg-gray-200 p-2 rounded-md">{userProfile.name}</p>
          <button
            on:click={toggleEdit}
            class="bg-blue-500 text-white p-2 rounded-md mt-2"
          >
            Edit
          </button>
        {/if}
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 font-bold mb-2">Email</label>
        <p class="bg-gray-200 p-2 rounded-md">{userProfile.email}</p>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 font-bold mb-2">Role</label>
        <p class="bg-gray-200 p-2 rounded-md">{userProfile.role}</p>
      </div>

      <button
        on:click={deleteUser}
        class="bg-red-500 text-white p-2 rounded-md mt-4"
      >
        Delete Account
      </button>
    {/if}
  </div>
</div>
