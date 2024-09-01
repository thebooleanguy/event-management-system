<script>
	import { userService } from '$lib/services/userService';
	import { user } from '$lib/stores/userStore';
	import { goto } from '$app/navigation';
	import { faUser, faEnvelope, faLock } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let name = '';
	let email = '';
	let password = '';
	let error = '';

	async function handleSubmit() {
		try {
			const response = await userService.register({ name, email, password });
			user.login(response.user);
			localStorage.setItem('token', response.token);
			goto('/');
		} catch (err) {
			error = 'Registration failed. Please try again.';
		}
	}
</script>

<div class="flex items-center justify-center min-h-screen bg-background-gray">
	<form
		on:submit|preventDefault={handleSubmit}
		class="w-full max-w-lg bg-card-white p-10 rounded-3xl shadow-xl space-y-8"
	>
		<h2 class="text-3xl font-bold text-dark-gray text-center">Create Your Account</h2>
		{#if error}
			<p class="text-red-500 text-center">{error}</p>
		{/if}
		<div class="space-y-6">
			<div class="relative">
				<label for="name" class="block text-sm font-medium text-dark-gray">Name</label>
				<div
					class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue"
				>
					<FontAwesomeIcon icon={faUser} class="text-dark-gray ml-3" />
					<input
						type="text"
						id="name"
						bind:value={name}
						required
						placeholder="Enter your name"
						class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md"
					/>
				</div>
			</div>
			<div class="relative">
				<label for="email" class="block text-sm font-medium text-dark-gray">Email</label>
				<div
					class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue"
				>
					<FontAwesomeIcon icon={faEnvelope} class="text-dark-gray ml-3" />
					<input
						type="email"
						id="email"
						bind:value={email}
						required
						placeholder="Enter your email"
						class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md"
					/>
				</div>
			</div>
			<div class="relative">
				<label for="password" class="block text-sm font-medium text-dark-gray">Password</label>
				<div
					class="flex items-center border border-gray-300 rounded-md shadow-sm focus-within:ring-2 focus-within:ring-accent-blue"
				>
					<FontAwesomeIcon icon={faLock} class="text-dark-gray ml-3" />
					<input
						type="password"
						id="password"
						bind:value={password}
						required
						placeholder="Enter your password"
						class="w-full px-3 py-2 text-dark-gray focus:outline-none rounded-r-md"
					/>
				</div>
			</div>
		</div>
		<button
			type="submit"
			class="w-full py-3 px-4 bg-button-yellow text-black rounded-md shadow-lg hover:bg-yellow-500 transition duration-300 focus:outline-none focus:ring-2 focus:ring-button-yellow focus:ring-opacity-50"
		>
			Register
		</button>
		<p class="text-sm text-center text-dark-gray mt-6">
			Already have an account? <a href="/users/login" class="text-accent-blue hover:underline"
				>Login here</a
			>
		</p>
	</form>
</div>
