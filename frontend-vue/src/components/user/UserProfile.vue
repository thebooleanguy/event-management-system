<template>
    <div class="min-h-screen bg-background-gray p-6">
        <div class="max-w-4xl mx-auto bg-card-white p-8 rounded-lg shadow-md">
            <h2 class="text-3xl font-bold text-dark-blue mb-6">User Profile</h2>

            <p v-if="loading" class="text-gray-500">Loading...</p>
            <p v-if="error" class="text-red-500 mb-4">{{ error }}</p>

            <div v-if="!loading && !error">
                <div class="mb-6">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faUser" class="text-dark-blue text-xl mr-2" />
                        <label class="text-gray-700 font-semibold">Name</label>
                    </div>
                    <div v-if="isEditing">
                        <input type="text" v-model="newName"
                            class="bg-gray-200 p-2 rounded-md w-full border border-gray-300"
                            placeholder="Enter new name" />
                        <div class="mt-2 flex space-x-2">
                            <button @click="updateUserName"
                                class="bg-accent-blue text-white px-4 py-2 rounded-md hover:bg-dark-blue transition">
                                Save
                            </button>
                            <button @click="toggleEdit"
                                class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition">
                                Cancel
                            </button>
                        </div>
                    </div>
                    <div v-else>
                        <p class="bg-gray-200 p-2 rounded-md border border-gray-300">{{ userProfile.name }}</p>
                        <button @click="toggleEdit"
                            class="bg-accent-blue text-white px-4 py-2 rounded-md hover:bg-dark-blue transition mt-2">
                            <font-awesome-icon :icon="faEdit" class="mr-1" />
                            Edit
                        </button>
                    </div>
                </div>

                <div class="mb-6">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faEnvelope" class="text-dark-blue text-xl mr-2" />
                        <label class="text-gray-700 font-semibold">Email</label>
                    </div>
                    <p class="bg-gray-200 p-2 rounded-md border border-gray-300">{{ userProfile.email }}</p>
                </div>

                <div class="mb-6">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faLock" class="text-dark-blue text-xl mr-2" />
                        <label class="text-gray-700 font-semibold">Role</label>
                    </div>
                    <p class="bg-gray-200 p-2 rounded-md border border-gray-300">{{ userProfile.role }}</p>
                </div>

                <div class="mb-6">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faUser" class="text-dark-blue text-xl mr-2" />
                        <label class="text-gray-700 font-semibold">User ID</label>
                    </div>
                    <p class="bg-gray-200 p-2 rounded-md border border-gray-300">{{ userProfile.id }}</p>
                </div>

                <div class="flex justify-center">
                    <button @click="deleteUser"
                        class="bg-red-500 text-white px-6 py-3 rounded-md hover:bg-red-600 transition">
                        <font-awesome-icon :icon="faTrash" class="mr-1" />
                        Delete Account
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { userService } from '@/services/userService'; // Adjust path as necessary
import { userStore } from '@/stores/userStore'; // Adjust path as necessary
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUser, faEnvelope, faLock, faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';

export default {
    name: 'UserProfile',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const userProfile = ref({
            name: '',
            email: '',
            role: 'USER',
            id: ''
        });
        const newName = ref<string>('');
        const error = ref<string>('');
        const loading = ref<boolean>(true);
        const isEditing = ref<boolean>(false);
        const router = useRouter();

        onMounted(async () => {
            try {
                const profile = await userService.getUserProfile();
                userProfile.value = {
                    name: profile.name,
                    email: profile.email,
                    role: profile.role,
                    id: profile.id
                };
                newName.value = profile.name;
            } catch (err) {
                error.value = 'Failed to load user profile. Please try again later.';
                console.error('Error fetching user profile:', err);
            } finally {
                loading.value = false;
            }
        });

        const toggleEdit = () => {
            isEditing.value = !isEditing.value;
        };

        const updateUserName = async () => {
            try {
                await userService.updateUserName(userProfile.value.email, newName.value);
                userProfile.value.name = newName.value;
                toggleEdit();
            } catch (err) {
                error.value = 'Failed to update username. Please try again later.';
                console.error('Error updating username:', err);
            }
        };

        const deleteUser = async () => {
            try {
                await userService.deleteUser(userProfile.value.email);
                router.push('/login');
            } catch (err) {
                error.value = 'Failed to delete user. Please try again later.';
                console.error('Error deleting user:', err);
            }
        };

        return {
            userProfile,
            newName,
            error,
            loading,
            isEditing,
            toggleEdit,
            updateUserName,
            deleteUser,
            faUser,
            faEnvelope,
            faLock,
            faEdit,
            faTrash
        };
    }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>