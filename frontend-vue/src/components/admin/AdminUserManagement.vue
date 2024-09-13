<template>
    <div class="p-4">
        <!-- Check if the user has admin role -->
        <div v-if="isAdmin">
            <!-- Search Bar for Filtering Users -->
            <div class="mb-4 flex items-center">
                <input v-model="searchEmail" @input="filterUsers" type="text" placeholder="Filter by email"
                    class="border p-2 rounded-lg w-full" />
            </div>
            <!-- User List Table -->
            <div class="overflow-x-auto">
                <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                        <tr class="bg-gray-100 border-b">
                            <th class="p-4 text-left">ID</th>
                            <th class="p-4 text-left">Email</th>
                            <th class="p-4 text-left">Role</th>
                            <th class="p-4 text-left">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="user in filteredUsers" :key="user.id">
                            <td class="p-4">{{ user.id }}</td>
                            <td class="p-4">{{ user.email }}</td>
                            <td class="p-4">{{ user.role }}</td>
                            <td class="p-4">
                                <button @click="openEditModal(user)" class="text-blue-500 hover:underline">Edit</button>
                                <button @click="openDeleteModal(user)"
                                    class="text-red-500 hover:underline ml-4">Delete</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Modal for Editing User -->
            <EditUserModal v-if="showEditModal" :user="selectedUser" @close="closeModals" @update="fetchUsers" />

            <!-- Modal for Deleting User -->
            <DeleteUserModal v-if="showDeleteModal" :user="selectedUser" @close="closeModals" @delete="fetchUsers" />
        </div>
        <div v-else>
            <p class="text-red-500">You do not have permission to view this page.</p>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { userService } from '@/services/userService';
import EditUserModal from '@/components/admin/EditUserModal.vue';
import DeleteUserModal from '@/components/admin/DeleteUserModal.vue';
import { useStore } from 'vuex';

const store = useStore();
const user = computed(() => store.state.user);
const isAdmin = computed(() => user.value?.role === 'ADMIN');

const users = ref([]);
const filteredUsers = ref([]);
const searchEmail = ref('');
const selectedUser = ref(null);

const showEditModal = ref(false);
const showDeleteModal = ref(false);

const fetchUsers = async () => {
    try {
        const response = await userService.getAllUsers();
        users.value = response;
        filteredUsers.value = response;
    } catch (error) {
        console.error('Failed to fetch users:', error);
    }
};

const filterUsers = () => {
    filteredUsers.value = users.value.filter(user =>
        user.email.toLowerCase().includes(searchEmail.value.toLowerCase())
    );
};

const openEditModal = (user) => {
    selectedUser.value = user;
    showEditModal.value = true;
    showDeleteModal.value = false; // Ensure delete modal is closed
};

const openDeleteModal = (user) => {
    selectedUser.value = user;
    showEditModal.value = false; // Ensure edit modal is closed
    showDeleteModal.value = true;
};

const closeModals = () => {
    showEditModal.value = false;
    showDeleteModal.value = false;
    selectedUser.value = null;
};

onMounted(fetchUsers);
</script>

<style scoped>
/* Add any additional styles if needed */
</style>
