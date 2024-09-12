import axios, {
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
    AxiosHeaders,
} from "axios";

// Define API URL
const EVENT_API_URL = "http://localhost:8082/api/events/"; // Base URL for event-related endpoints

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: EVENT_API_URL,
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

// Define TypeScript types for event data
interface EventData {
    title: string;
    description?: string;
    date?: string;
    location?: string;
    // Add other event fields as needed
}

interface SearchParams {
    title?: string;
    category?: string;
}

export const eventService = {
    // Retrieve all events
    getAllEvents: () => apiClient.get("/all").then((response) => response.data),

    // Retrieve an event by ID
    getEventById: (id: string) =>
        apiClient.get(`/${id}`).then((response) => response.data),

    // Search for events by title and/or category
    searchEvents: (params: SearchParams) =>
        apiClient.get("/search", { params }).then((response) => response.data),

    // Create a new event
    createEvent: (eventData: EventData, organizerId: string) =>
        apiClient
            .post("/", { ...eventData, organizerId })
            .then((response) => response.data),

    // Update an event by ID
    updateEvent: (id: string, eventData: EventData) =>
        apiClient.put(`/${id}`, eventData).then((response) => response.data),

    // Delete an event by ID
    deleteEvent: (id: string) => apiClient.delete(`/${id}`),

    // Retrieve all available event categories
    getCategories: () =>
        apiClient.get("/categories").then((response) => response.data),

    // Retrieve the number of available tickets for an event by ID
    getAvailableTickets: (id: string) =>
        apiClient
            .get(`/tickets/available/${id}`)
            .then((response) => response.data),

    // Set the number of available tickets for an event by ID
    setAvailableTickets: (id: string, availableTickets: number) =>
        apiClient.put(`/tickets/available/${id}`, null, {
            params: { availableTickets },
        }),

    // Retrieve the ticket price (unit price) for an event by ID
    getUnitPrice: (id: string) =>
        apiClient.get(`/tickets/price/${id}`).then((response) => response.data),

    // Set the ticket price (unit price) for an event by ID
    setUnitPrice: (id: string, unitPrice: number) =>
        apiClient.put(`/tickets/price/${id}`, null, { params: { unitPrice } }),
};
