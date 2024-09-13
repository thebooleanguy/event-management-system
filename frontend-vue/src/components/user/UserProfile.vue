<template>
    <div class="min-h-screen bg-background-gray p-6">
        <div class="max-w-4xl mx-auto bg-card-white p-8 rounded-lg shadow-lg">
            <h2 class="text-4xl font-extrabold text-dark-blue mb-6">User Profile</h2>

            <p v-if="loading" class="text-gray-500 text-lg">Loading...</p>
            <p v-if="error" class="text-red-500 text-lg mb-4">{{ error }}</p>

            <div v-if="!loading && !error">
                <div class="mb-8">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faUser" class="text-dark-blue text-2xl mr-2" />
                        <label class="text-gray-700 font-semibold text-lg">Name</label>
                    </div>
                    <div v-if="isEditing">
                        <input type="text" v-model="newName"
                            class="bg-gray-100 p-3 rounded-md w-full border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-accent-blue"
                            placeholder="Enter new name" />
                        <div class="mt-4 flex space-x-3">
                            <button @click="updateUserName"
                                class="bg-accent-blue text-white px-5 py-2 rounded-md shadow-md hover:bg-dark-blue transition">
                                <font-awesome-icon :icon="faSave" class="mr-2" />
                                Save
                            </button>
                            <button @click="toggleEdit"
                                class="bg-gray-600 text-white px-5 py-2 rounded-md shadow-md hover:bg-gray-700 transition">
                                <font-awesome-icon :icon="faTimes" class="mr-2" />
                                Cancel
                            </button>
                        </div>
                    </div>
                    <div v-else>
                        <p class="bg-gray-100 p-3 rounded-md border border-gray-300 shadow-sm">{{ userProfile.name }}
                        </p>
                        <button @click="toggleEdit"
                            class="bg-accent-blue text-white px-4 py-2 rounded-md shadow-md hover:bg-dark-blue transition mt-4">
                            <font-awesome-icon :icon="faEdit" class="mr-2" />
                            Edit
                        </button>
                    </div>
                </div>

                <div class="mb-8">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faEnvelope" class="text-dark-blue text-2xl mr-2" />
                        <label class="text-gray-700 font-semibold text-lg">Email</label>
                    </div>
                    <p class="bg-gray-100 p-3 rounded-md border border-gray-300 shadow-sm">{{ userProfile.email }}</p>
                </div>

                <div class="mb-8">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faLock" class="text-dark-blue text-2xl mr-2" />
                        <label class="text-gray-700 font-semibold text-lg">Role</label>
                    </div>
                    <p class="bg-gray-100 p-3 rounded-md border border-gray-300 shadow-sm">{{ userProfile.role }}</p>
                </div>

                <div class="mb-8">
                    <div class="flex items-center mb-4">
                        <font-awesome-icon :icon="faUser" class="text-dark-blue text-2xl mr-2" />
                        <label class="text-gray-700 font-semibold text-lg">User ID</label>
                    </div>
                    <p class="bg-gray-100 p-3 rounded-md border border-gray-300 shadow-sm">{{ userProfile.id }}</p>
                </div>

                <div class="flex justify-center">
                    <button @click="deleteUser"
                        class="bg-red-600 text-white px-6 py-3 rounded-md shadow-md hover:bg-red-700 transition">
                        <font-awesome-icon :icon="faTrash" class="mr-2" />
                        Delete Account
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'; // Import useStore from Vuex
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUser, faEnvelope, faLock, faEdit, faTrash, faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import { userService } from '@/services/userService'; // Import userService

export default {
    name: 'UserProfile',
    components: {
        FontAwesomeIcon
    },
    setup() {
        const store = useStore(); // Use Vuex store
        const userProfile = computed(() => store.getters.getUser); // Access user profile from Vuex store
        const newName = ref(userProfile.value?.name || '');
        const error = ref<string>('');
        const loading = ref<boolean>(true);
        const isEditing = ref<boolean>(false);
        const router = useRouter();

        onMounted(async () => {
            try {
                await store.dispatch('fetchUserProfile'); // Fetch user profile from Vuex store
                newName.value = userProfile.value?.name || ''; // Initialize newName for editing
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
                // Ensure newName is not empty
                if (newName.value.trim() === '') {
                    error.value = 'Name cannot be empty';
                    return;
                }

                // Update the user name via userService
                await userService.updateUserName(userProfile.value?.email || '', newName.value);

                // Update Vuex store with the new name
                await store.dispatch('updateUser', { name: newName.value });
                toggleEdit();
            } catch (err) {
                error.value = 'Failed to update username. Please try again later.';
                console.error('Error updating username:', err);
            }
        };

        const deleteUser = async () => {
            try {
                // Delete the user via userService
                await userService.deleteUser(userProfile.value?.email || '');

                // Clear Vuex store and redirect to login
                await store.dispatch('logout');
                router.push('/users/login');
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
            faTrash,
            faSave,
            faTimes
        };
    }
};
</script>

<style scoped>
/* Scoped styles if necessary */
</style>
