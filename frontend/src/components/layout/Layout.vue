<template>
    <div>
        <!-- Use the Header component -->
        <Header />

        <!-- Main content slot -->
        <main>
            <router-view />
        </main>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
// Import the header component (adjust the path based on your folder structure)
import Header from '@/components/layout/Header.vue';
import '@/main.css';

export default defineComponent({
    components: {
        Header,
    },
});
</script>

<style scoped>
/* You can add scoped styles here if needed */
</style>