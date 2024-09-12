import axios, {
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
    AxiosHeaders,
} from "axios";

const BOOKING_API_URL = "http://localhost:8083/api/bookings/"; // Base URL for booking-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: BOOKING_API_URL,
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
                ...config.headers,
                Authorization: `Bearer ${token}`,
            });
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export const bookingService = {
    getAllBookings: () =>
        apiClient.get("/all").then((response) => response.data),

    getBookingById: (id: string) =>
        apiClient.get(`/${id}`).then((response) => response.data),

    getBookingsByUser: (userId: string) =>
        apiClient.get(`/user/${userId}`).then((response) => response.data),

    cancelBooking: (id: string) => apiClient.delete(`/${id}`),

    bookTicket: (bookingData: any) =>
        apiClient.post("/", bookingData).then((response) => response.data),
};
