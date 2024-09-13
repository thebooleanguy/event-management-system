<template>
    <div class="min-h-screen bg-gray-100">
        <main class="container mx-auto px-4 py-8">
            <!-- Event Header with Search Functionality -->
            <EventHeader :username="username" @search="handleSearch" />

            <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-8">
                <div class="col-span-2 lg:col-span-3">
                    <!-- Display the list of events -->
                    <EventList :events="events" />
                </div>

                <div class="space-y-4">
                    <Categories :categories="categories" />

                    <div class="mt-8 flex justify-center space-x-4">
                        <SocialMediaIcons />
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import EventHeader from '@/components/event/EventHeader.vue';
import EventList from '@/components/event/EventList.vue';
import Categories from '@/components/Categories.vue';
import SocialMediaIcons from '@/components/SocialMediaIcons.vue';
import { eventService } from '@/services/eventService'; // Adjust the path to your event service

export default defineComponent({
    components: {
        EventHeader,
        EventList,
        Categories,
        SocialMediaIcons,
    },
    setup() {
        const username = ref<string>('');
        const events = ref<any[]>([]);
        const categories = ref<any[]>([]);

        // Function to load events
        const loadEvents = async () => {
            try {
                events.value = await eventService.getAllEvents();
            } catch (error) {
                console.error('Error fetching events:', error);
            }
        };

        // Function to handle search results
        const handleSearch = (filteredEvents: any[]) => {
            events.value = filteredEvents;
        };

        // Lifecycle hook to load events when the component is mounted
        onMounted(() => {
            loadEvents();
        });

        return {
            username,
            events,
            categories,
            handleSearch,
        };
    },
});

</script>


<style scoped>
/* Add any custom styling here */
</style>
