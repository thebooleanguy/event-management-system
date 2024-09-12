<template>
    <div>
        <h3 class="text-xl font-semibold mb-4">Explore Events</h3>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div class="space-y-2">
            <button v-for="category in categories" :key="category"
                class="w-full bg-blue-200 px-4 py-2 rounded-md text-left hover:bg-blue-300">
                {{ toTitleCase(category) }}
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService'; // Adjust path as necessary

export default {
    name: 'EventCategories',
    setup() {
        const categories = ref<string[]>([]);
        const error = ref<string>('');

        // Utility function to convert category names to Title Case
        const toTitleCase = (str: string): string => {
            return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
        };

        onMounted(async () => {
            try {
                categories.value = await eventService.getCategories();
            } catch (err) {
                error.value = 'Failed to load categories';
                console.error(err);
            }
        });

        return {
            categories,
            error,
            toTitleCase
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>