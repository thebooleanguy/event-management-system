import axios, {
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
    AxiosHeaders,
} from "axios";
import store from "@/store/userStore"; // Import the Vuex store

const USER_API_URL = "http://localhost:8081/api/users/"; // Base URL for user-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: USER_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Add an interceptor to include the Authorization header
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        const token = localStorage.getItem("token");
        if (token) {
            // Ensure headers is defined and then add or overwrite the Authorization header
            config.headers = new AxiosHeaders({
                ...(config.headers || {}), // Handle case where headers might be undefined
                Authorization: `Bearer ${token}`,
            });
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Define TypeScript types for user-related data
interface UserData {
    email: string;
    password: string;
    [key: string]: any; // Use this to cover additional fields as needed
}

interface UserProfile {
    id: string;
    email: string;
    [key: string]: any; // Use this to cover additional profile fields as needed
}

interface LoginResponse {
    token: string;
    user: UserProfile;
}

interface User {
    id: string;
    email: string;
    role: string; // Add role property
    [key: string]: any; // Use this to cover additional user fields as needed
}

// Define the userService with TypeScript support
export const userService = {
    // Login and store user data
    login: async (email: string, password: string): Promise<LoginResponse> => {
        try {
            const response = await apiClient.post<LoginResponse>("/login", {
                email,
                password,
            });
            const data = response.data;
            localStorage.setItem("token", data.token); // Store the token in localStorage
            store.dispatch("login", data.user); // Update the Vuex store with the user data
            return data; // Return both user data and token
        } catch (error: any) {
            console.error(
                `Login failed! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Login failed! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Register a new user
    register: async (userData: UserData): Promise<any> => {
        try {
            const response = await apiClient.post("/register", userData);
            return response.data;
        } catch (error: any) {
            console.error(
                `Registration failed! status: ${error.response?.status}, message: ${error.message}`
            );
            throw new Error(
                `Registration failed! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Logout and clear local storage
    logout: async (): Promise<void> => {
        try {
            await apiClient.post("/logout");
        } catch (error: any) {
            console.error("Logout failed:", error);
        } finally {
            localStorage.removeItem("token"); // Remove the token from localStorage
            store.dispatch("logout"); // Use the Vuex action for logout
        }
    },

    // Retrieve user profile
    getUserProfile: async (): Promise<UserProfile> => {
        try {
            const response = await apiClient.get<UserProfile>("/profile");
            return response.data;
        } catch (error: any) {
            console.error("Failed to get user profile:", error);
            throw new Error(
                `Failed to get user profile! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Get user ID based on profile response
    getUserId: async (): Promise<string | null> => {
        try {
            const response = await apiClient.get<UserProfile>("/profile");
            return response.data?.id || null; // Adjust based on the actual profile response structure
        } catch (error: any) {
            console.error("Failed to get user ID:", error);
            return null; // Return null if there's an error or the user is not logged in
        }
    },

    // Update user profile
    updateUserProfile: async (userData: UserData): Promise<any> => {
        try {
            const response = await apiClient.put("/profile", userData);
            return response.data;
        } catch (error: any) {
            console.error("Failed to update user profile:", error);
            throw new Error(
                `Failed to update user profile! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Update user name
    updateUserName: async (email: string, newName: string): Promise<any> => {
        try {
            const response = await apiClient.put("/update-name", null, {
                params: { email, newName },
            });
            return response.data;
        } catch (error: any) {
            console.error("Failed to update user name:", error);
            throw new Error(
                `Failed to update user name! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Delete user account
    deleteUser: async (email: string): Promise<void> => {
        try {
            await apiClient.delete("/delete", {
                params: { email },
            });
        } catch (error: any) {
            console.error("Delete failed:", error);
            throw error; // Re-throw to handle in the component
        }
    },

    // Get all users
    getAllUsers: async (): Promise<User[]> => {
        try {
            const response = await apiClient.get<User[]>("/all");
            return response.data;
        } catch (error: any) {
            console.error("Failed to get all users:", error);
            throw new Error(
                `Failed to get all users! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },

    // Update user role
    updateUserRole: async (email: string, newRole: string): Promise<any> => {
        try {
            const response = await apiClient.put("/update-role", null, {
                params: { email, newRole },
            });
            return response.data;
        } catch (error: any) {
            console.error("Failed to update user role:", error);
            throw new Error(
                `Failed to update user role! status: ${error.response?.status}, message: ${error.message}`
            );
        }
    },
};
