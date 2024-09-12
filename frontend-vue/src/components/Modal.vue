<template>
    <div v-if="isOpen" class="fixed inset-0 flex items-center justify-center z-50">
        <!-- Overlay -->
        <div class="fixed inset-0 bg-black opacity-50" @click="onClose"></div>

        <!-- Modal Content -->
        <div class="bg-white rounded-lg overflow-hidden shadow-lg p-6 max-w-lg mx-auto z-10">
            <slot></slot>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue';

export default defineComponent({
    name: 'Modal',
    props: {
        isOpen: {
            type: Boolean,
            required: true
        },
        onClose: {
            type: Function as PropType<() => void>,
            required: true
        }
    }
});
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>