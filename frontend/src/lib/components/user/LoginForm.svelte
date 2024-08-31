<script>
    import { userService } from '$lib/services/userService';
    import { user } from '$lib/stores/userStore';
    import { goto } from '$app/navigation';

    let email = '';
    let password = '';
    let error = '';

    async function handleSubmit() {
        try {
            const response = await userService.login(email, password);
            if (response.token) {
                // Assuming response.token is a JWT
                user.login({ email, token: response.token });
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

<form on:submit|preventDefault={handleSubmit} class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-md">
    <div class="mb-4">
        <label for="email" class="block mb-2 text-gray-700">Email</label>
        <input
            type="email"
            id="email"
            bind:value={email}
            required
            class="w-full px-3 py-2 border rounded-md text-gray-700 focus:outline-none focus:border-blue-500"
        />
    </div>
    <div class="mb-4">
        <label for="password" class="block mb-2 text-gray-700">Password</label>
        <input
            type="password"
            id="password"
            bind:value={password}
            required
            class="w-full px-3 py-2 border rounded-md text-gray-700 focus:outline-none focus:border-blue-500"
        />
    </div>
    {#if error}
        <p class="text-red-500 mb-4">{error}</p>
    {/if}
    <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
        Login
    </button>
</form>
