<script>
	import { userService } from '$lib/services/userService';
	import { user } from '$lib/stores/userStore';
	import { goto } from '$app/navigation';
	import { faEnvelope, faLock } from '@fortawesome/free-solid-svg-icons';
	import { FontAwesomeIcon } from '@fortawesome/svelte-fontawesome';

	let email = '';
	let password = '';
	let error = '';

	async function handleSubmit() {
		try {
			const response = await userService.login(email, password);
			if (response && response.token) {
				user.login(response.user);
				localStorage.setItem('token', response.token);
				goto('/');
			} else {
				error = 'Invalid email or password';
			}
		} catch (err) {
			error = 'Invalid email or password';
		}
	}
</script>

<div class="flex items-center justify-center min-h-screen bg-[#E5E5E5]">
	<form
		on:submit|preventDefault={handleSubmit}
		class="w-full max-w-lg bg-[#FFFFFF] p-10 rounded-3xl shadow-xl space-y-8"
	>
		<h2 class="text-3xl font-bold text-[#1A1A1A] text-center">Login to Your Account</h2>
		{#if error}
			<p class="text-red-500 text-center">{error}</p>
		{/if}
		<div class="space-y-6">
			<div class="relative">
				<label for="email" class="block text-sm font-medium text-[#1A1A1A]">Email</label>
				<div
					class="flex items-center border border-[#D1D5DB] rounded-md shadow-sm focus-within:ring-2 focus-within:ring-[#7DD3FC]"
				>
					<FontAwesomeIcon icon={faEnvelope} class="text-[#1E40AF] ml-3" />
					<input
						type="email"
						id="email"
						bind:value={email}
						required
						placeholder="Enter your email"
						class="w-full px-3 py-2 text-[#1A1A1A] focus:outline-none rounded-r-md"
					/>
				</div>
			</div>
			<div class="relative">
				<label for="password" class="block text-sm font-medium text-[#1A1A1A]">Password</label>
				<div
					class="flex items-center border border-[#D1D5DB] rounded-md shadow-sm focus-within:ring-2 focus-within:ring-[#7DD3FC]"
				>
					<FontAwesomeIcon icon={faLock} class="text-[#1E40AF] ml-3" />
					<input
						type="password"
						id="password"
						bind:value={password}
						required
						placeholder="Enter your password"
						class="w-full px-3 py-2 text-[#1A1A1A] focus:outline-none rounded-r-md"
					/>
				</div>
			</div>
		</div>
		<button
			type="submit"
			class="w-full py-3 px-4 bg-[#F7D44C] text-[#1A1A1A] rounded-md shadow-lg hover:bg-[#E5B840] transition duration-300 focus:outline-none focus:ring-2 focus:ring-[#F7D44C] focus:ring-opacity-50"
		>
			Login
		</button>
		<p class="text-sm text-center text-[#1A1A1A] mt-6">
			Don't have an account? <a href="/users/register" class="text-[#7DD3FC] hover:underline"
				>Register here</a
			>
		</p>
	</form>
</div>
