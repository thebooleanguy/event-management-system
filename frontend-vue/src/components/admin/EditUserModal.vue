<template>
    <div v-if="user" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="bg-white p-4 rounded-lg shadow-lg w-80">
            <h2 class="text-xl font-semibold mb-4">Edit User</h2>
            <form @submit.prevent="updateUser">
                <div class="mb-4">
                    <label for="name" class="block text-gray-700">Name:</label>
                    <input v-model="form.name" type="text" id="name" class="border p-2 rounded-lg w-full" required />
                </div>
                <div class="mb-4">
                    <label for="email" class="block text-gray-700">Email:</label>
                    <input v-model="form.email" type="email" id="email" class="border p-2 rounded-lg w-full" required />
                </div>
                <div class="mb-4">
                    <label for="role" class="block text-gray-700">Role:</label>
                    <select v-model="form.role" id="role" class="border p-2 rounded-lg w-full" required>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div class="flex justify-between">
                    <button type="submit"
                        class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600">Update</button>
                    <button @click="$emit('close')"
                        class="bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { userService } from '@/services/userService';

const props = defineProps({
    user: Object
});

const emit = defineEmits(['close', 'update']);

const form = ref({
    name: props.user?.name || '', // Include name field
    email: props.user?.email || '',
    role: props.user?.role || ''
});

// Watch for prop changes to update form
watch(() => props.user, (newUser) => {
    form.value = {
        name: newUser?.name || '',
        email: newUser?.email || '',
        role: newUser?.role || ''
    };
});

const updateUser = async () => {
    try {
        await userService.updateUserProfile(form.value);
        emit('update');
        emit('close');
    } catch (error) {
        console.error('Failed to update user:', error);
    }
};
</script>

<style scoped>
/* Add any additional styles if needed */
</style>
