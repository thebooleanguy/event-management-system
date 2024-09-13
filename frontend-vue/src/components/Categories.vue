<template>
    <div>
        <h3 class="text-xl font-semibold mb-4">Explore Events</h3>
        <p v-if="error" class="text-red-500">{{ error }}</p>
        <div class="space-y-2">
            <button v-for="category in categories" :key="category" @click="filterByCategory(category)"
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
    emits: ['search'], // Declare the search event
    setup(_, { emit }) {
        const categories = ref<string[]>([]);
        const error = ref<string>('');

        // Utility function to convert category names to Title Case
        const toTitleCase = (str: string): string => {
            return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
        };

        const fetchCategories = async () => {
            try {
                categories.value = await eventService.getCategories();
            } catch (err) {
                error.value = 'Failed to load categories';
                console.error(err);
            }
        };

        // Call fetchCategories when component is mounted
        onMounted(fetchCategories);

        const filterByCategory = (category: string) => {
            emit('search', { category });
        };

        return {
            categories,
            error,
            toTitleCase,
            filterByCategory
        };
    }
};
</script>

<style scoped>
/* Keep existing styles */
</style>
