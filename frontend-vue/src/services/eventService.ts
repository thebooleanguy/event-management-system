// eventService.ts
import axios, {
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
    AxiosHeaders,
} from "axios";

const EVENT_API_URL = "http://localhost:8082/api/events/";

const apiClient = axios.create({
    baseURL: EVENT_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers = new AxiosHeaders({
                ...config.headers,
                Authorization: `Bearer ${token}`,
            });
        }
        return config;
    },
    (error) => Promise.reject(error)
);

interface EventData {
    title: string;
    description?: string;
    date?: string;
    location?: string;
}

interface SearchParams {
    title?: string;
    category?: string;
}

export const eventService = {
    getAllEvents: () => apiClient.get("/all").then((response) => response.data),
    getEventById: (id: number) =>
        apiClient.get(`/${id}`).then((response) => {
            console.log("Response:", response);
            console.log("Response data:", response.data);
            return response.data;
        }),
    searchEvents: (params: SearchParams) =>
        apiClient.get("/search", { params }).then((response) => response.data),
    createEvent: (eventData: EventData, organizerId: number) =>
        apiClient
            .post("/", { ...eventData, organizerId })
            .then((response) => response.data),
    updateEvent: (id: number, eventData: EventData) =>
        apiClient.put(`/${id}`, eventData).then((response) => response.data),
    deleteEvent: (id: number) => apiClient.delete(`/${id}`),
    getCategories: () =>
        apiClient.get("/categories").then((response) => response.data),
    getAvailableTickets: (id: number) =>
        apiClient
            .get(`/tickets/available/${id}`)
            .then((response) => response.data),
    setAvailableTickets: (id: number, availableTickets: number) =>
        apiClient.put(`/tickets/available/${id}`, null, {
            params: { availableTickets },
        }),
    getUnitPrice: (id: number) =>
        apiClient.get(`/tickets/price/${id}`).then((response) => response.data),
    setUnitPrice: (id: number, unitPrice: number) =>
        apiClient.put(`/tickets/price/${id}`, null, { params: { unitPrice } }),
};
