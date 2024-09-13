<template>
    <div class="flex mb-4">
        <select v-model="selectedCategory" class="bg-blue-200 text-gray-800 px-4 py-2 rounded-l-md">
            <option v-for="category in categories" :key="category" :value="category">
                {{ toTitleCase(category) }}
            </option>
        </select>
        <input type="text" v-model="searchQuery" placeholder="Search for an event"
            class="flex-grow px-4 py-2 border-t border-b border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
            @input="handleSearch" />
        <button class="bg-gray-200 px-4 py-2 rounded-r-md hover:bg-gray-300" @click="handleSearch">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { eventService } from '@/services/eventService';

export default defineComponent({
    name: 'SearchBar',
    emits: ['search'],
    setup(_, { emit }) {
        const searchQuery = ref<string>('');
        const selectedCategory = ref<string>('All');
        const categories = ref<string[]>([]);

        const toTitleCase = (str: string) => {
            return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
        };

        const fetchCategories = async () => {
            try {
                categories.value = await eventService.getCategories();
                categories.value = ['All', ...categories.value];
            } catch (err) {
                console.error('Failed to fetch categories:', err);
            }
        };

        const handleSearch = async () => {
            try {
                const params = {
                    title: searchQuery.value,
                    category: selectedCategory.value !== 'All' ? selectedCategory.value : undefined,
                };
                const filteredEvents = await eventService.searchEvents(params);
                emit('search', filteredEvents);
            } catch (err) {
                console.error('Failed to fetch events:', err);
            }
        };

        onMounted(() => {
            fetchCategories();
        });

        return {
            searchQuery,
            selectedCategory,
            categories,
            toTitleCase,
            handleSearch
        };
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
