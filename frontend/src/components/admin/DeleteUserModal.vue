<template>
    <div v-if="user" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="bg-white p-4 rounded-lg shadow-lg w-80">
            <h2 class="text-xl font-semibold mb-4">Delete User</h2>
            <p class="mb-4">Are you sure you want to delete this user?</p>
            <div class="flex justify-between">
                <button @click="deleteUser"
                    class="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600">Delete</button>
                <button @click="$emit('close')"
                    class="bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { userService } from '@/services/userService';

const props = defineProps({
    user: Object
});

const emit = defineEmits(['close', 'delete']);

const deleteUser = async () => {
    try {
        await userService.deleteUser(props.user.email);
        emit('delete');
        emit('close');
    } catch (error) {
        console.error('Failed to delete user:', error);
    }
};
</script>