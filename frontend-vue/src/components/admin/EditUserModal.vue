<template>
    <div v-if="user" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="bg-white p-4 rounded-lg shadow-lg w-80">
            <h2 class="text-xl font-semibold mb-4">Edit User</h2>
            <form @submit.prevent="updateUser">
                <!-- Username Field -->
                <div class="mb-4">
                    <label for="username" class="block text-gray-700">Username:</label>
                    <input v-model="form.username" type="text" id="username" class="border p-2 rounded-lg w-full"
                        required />
                </div>
                <!-- Role Field -->
                <div class="mb-4">
                    <label for="role" class="block text-gray-700">Role:</label>
                    <select v-model="form.role" id="role" class="border p-2 rounded-lg w-full" required>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <!-- Action Buttons -->
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
import { ref, onMounted } from 'vue';
import { userService } from '@/services/userService';

// Define props
const props = defineProps({
    user: {
        type: Object,
        required: true
    }
});

// Define emits
const emit = defineEmits(['close', 'update']);

// State for form data
const form = ref({
    username: '',
    role: ''
});

// Load user details based on props.user
onMounted(() => {
    form.value.username = props.user.name || '';
    form.value.role = props.user.role || '';
});

// Update user details
const updateUser = async () => {
    try {
        if (!form.value.username.trim()) {
            console.error('Username cannot be empty');
            return;
        }
        // Update the user details using email from props.user
        await userService.updateUserName(props.user.email, form.value.username);
        await userService.updateUserRole(props.user.email, form.value.role);
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
