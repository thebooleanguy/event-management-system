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

interface BookingDTO {
    eventId: number;
    userId: string; // Assuming userId is a string based on your form
    totalTickets: number;
    paymentMethod?: string;
    totalPrice?: number; // Optional
}

export const bookingService = {
    getAllBookings: () =>
        apiClient.get("/all").then((response) => response.data),

    getBookingById: (id: number) =>
        apiClient.get(`/${id}`).then((response) => response.data),

    getBookingsByUser: (userId: number) =>
        apiClient.get(`/user/${userId}`).then((response) => response.data),

    cancelBooking: (id: number) => apiClient.delete(`/${id}`),

    bookTicket: (bookingData: BookingDTO) =>
        apiClient.post("/book", bookingData).then((response) => response.data),
};
