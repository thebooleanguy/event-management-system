<template>
    <div class="flex items-center justify-center min-h-screen bg-gray-100">
        <div class="bg-white p-6 rounded-lg shadow-lg">
            <h2 class="text-xl font-semibold mb-4">Unauthorized</h2>
            <p>You do not have permission to view this page. Please log in with an admin account.</p>
            <router-link to="/" class="text-blue-500 hover:underline mt-4 inline-block">Go to Home</router-link>
        </div>
    </div>
</template>

<script setup>
// No specific script needed for this component
</script>