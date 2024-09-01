<script>
	import { onMount } from 'svelte';
	import { user } from '$lib/stores/userStore';
	import { userService } from '$lib/services/userService';
	import { faUser, faEnvelope, faLock, faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let userProfile = { name: '', email: '', role: 'USER', id: '' };
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
				role: profile.role,
				id: profile.id
			};
			newName = profile.name;
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
			userProfile.name = newName;
			toggleEdit();
		} catch (err) {
			error = 'Failed to update username. Please try again later.';
			console.error('Error updating username:', err);
		}
	};

	const deleteUser = async () => {
		try {
			await userService.deleteUser(userProfile.email);
			window.location.href = '/login';
		} catch (err) {
			error = 'Failed to delete user. Please try again later.';
			console.error('Error deleting user:', err);
		}
	};
</script>

<svelte:head>
	<title>User Profile</title>
</svelte:head>

<div class="min-h-screen bg-background-gray p-6">
	<div class="max-w-4xl mx-auto bg-card-white p-8 rounded-lg shadow-md">
		<h2 class="text-3xl font-bold text-dark-blue mb-6">User Profile</h2>

		{#if loading}
			<p class="text-gray-500">Loading...</p>
		{:else if error}
			<p class="text-red-500 mb-4">{error}</p>
		{/if}

		{#if !loading && !error}
			<div class="mb-6">
				<div class="flex items-center mb-4">
					<FontAwesomeIcon icon={faUser} class="text-dark-blue text-xl mr-2" />
					<label class="text-gray-700 font-semibold">Name</label>
				</div>
				{#if isEditing}
					<input
						type="text"
						bind:value={newName}
						class="bg-gray-200 p-2 rounded-md w-full border border-gray-300"
						placeholder="Enter new name"
					/>
					<div class="mt-2 flex space-x-2">
						<button
							on:click={updateUserName}
							class="bg-accent-blue text-white px-4 py-2 rounded-md hover:bg-dark-blue transition"
						>
							Save
						</button>
						<button
							on:click={toggleEdit}
							class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition"
						>
							Cancel
						</button>
					</div>
				{:else}
					<p class="bg-gray-200 p-2 rounded-md border border-gray-300">{userProfile.name}</p>
					<button
						on:click={toggleEdit}
						class="bg-accent-blue text-white px-4 py-2 rounded-md hover:bg-dark-blue transition mt-2"
					>
						<FontAwesomeIcon icon={faEdit} class="mr-1" />
						Edit
					</button>
				{/if}
			</div>

			<div class="mb-6">
				<div class="flex items-center mb-4">
					<FontAwesomeIcon icon={faEnvelope} class="text-dark-blue text-xl mr-2" />
					<label class="text-gray-700 font-semibold">Email</label>
				</div>
				<p class="bg-gray-200 p-2 rounded-md border border-gray-300">{userProfile.email}</p>
			</div>

			<div class="mb-6">
				<div class="flex items-center mb-4">
					<FontAwesomeIcon icon={faLock} class="text-dark-blue text-xl mr-2" />
					<label class="text-gray-700 font-semibold">Role</label>
				</div>
				<p class="bg-gray-200 p-2 rounded-md border border-gray-300">{userProfile.role}</p>
			</div>

			<div class="mb-6">
				<div class="flex items-center mb-4">
					<FontAwesomeIcon icon={faUser} class="text-dark-blue text-xl mr-2" />
					<label class="text-gray-700 font-semibold">User ID</label>
				</div>
				<p class="bg-gray-200 p-2 rounded-md border border-gray-300">{userProfile.id}</p>
			</div>

			<div class="flex justify-center">
				<button
					on:click={deleteUser}
					class="bg-red-500 text-white px-6 py-3 rounded-md hover:bg-red-600 transition"
				>
					<FontAwesomeIcon icon={faTrash} class="mr-1" />
					Delete Account
				</button>
			</div>
		{/if}
	</div>
</div>
