<script>
    import { user } from '$lib/stores/userStore';
    import { api } from '$lib/services/api';
    import { goto } from '$app/navigation';

    async function handleLogout() {
        try {
            await api.logout();
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
            <button class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
                Create Event
            </button>
            <button class="bg-yellow-300 text-gray-800 px-4 py-2 rounded-md hover:bg-yellow-400">
                My Events
            </button>
            <button class="p-2 rounded-full bg-gray-700 hover:bg-gray-600">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
            </button>
            <button class="p-2 rounded-full bg-gray-700 hover:bg-gray-600">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
            </button>
        </div>
    </div>
</header>
