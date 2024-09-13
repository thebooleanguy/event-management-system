<template>
    <div v-if="user" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="bg-white p-4 rounded-lg shadow-lg w-80">
            <h2 class="text-xl font-semibold mb-4">Edit User</h2>
            <form @submit.prevent="updateUser">
                <div class="mb-4">
                    <label for="email" class="block text-gray-700">Username:</label>
                    <input v-model="form.name" id="name" class="border p-2 rounded-lg w-full" required />
                </div>
                <div class="mb-4">
                    <label for="role" class="block text-gray-700">Role:</label>
                    <select v-model="form.role" id="role" class="border p-2 rounded-lg w-full" required>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div class="flex justify-between">
                    <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600">
                        <font-awesome-icon :icon="saveIcon" class="mr-2" />
                        Update
                    </button>
                    <button @click="$emit('close')"
                        class="bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">
                        <font-awesome-icon :icon="cancelIcon" class="mr-2" />
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { userService } from '@/services/userService';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faSave as faSaveIcon, faTimes as faTimesIcon } from '@fortawesome/free-solid-svg-icons';

const props = defineProps({
    user: Object
});

const emit = defineEmits(['close', 'update']);

const form = ref({
    name: '',
    role: 'USER'
});

const saveIcon = faSaveIcon;
const cancelIcon = faTimesIcon;

const fetchUserProfile = async () => {
    try {
        const userProfile = await userService.getUserProfile();
        form.value.name = userProfile.name;
        form.value.role = userProfile.role;
    } catch (error) {
        console.error('Failed to fetch user profile:', error);
    }
};

onMounted(() => {
    if (props.user) {
        fetchUserProfile();
    }
});

watch(() => props.user, (newValue) => {
    if (newValue) {
        fetchUserProfile();
    }
});

const updateUser = async () => {
    try {
        await userService.updateUserName(form.value.email, form.value.name);
        await userService.updateUserRole(form.value.email, form.value.role);
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
