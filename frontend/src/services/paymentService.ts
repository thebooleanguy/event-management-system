import axios from "axios";

// Define the API base URL
const API_BASE_URL = "http://localhost:8084/api/payments";

// Define TypeScript interfaces to match the Payment entity
interface Payment {
    id?: number;
    userId: number;
    bookingId: number;
    amount: number;
    paymentMethod: string;
    transactionId?: string;
    success?: boolean;
}

// Create a Payment Service
class PaymentService {
    // Process a payment
    async processPayment(payment: Payment): Promise<Payment> {
        try {
            const response = await axios.post<Payment>(
                `${API_BASE_URL}/process`,
                payment
            );
            return response.data;
        } catch (error) {
            console.error("Error processing payment:", error);
            throw new Error("Failed to process payment");
        }
    }

    // Retrieve all payments
    async getAllPayments(): Promise<Payment[]> {
        try {
            const response = await axios.get<Payment[]>(API_BASE_URL);
            return response.data;
        } catch (error) {
            console.error("Error fetching payments:", error);
            throw new Error("Failed to fetch payments");
        }
    }

    // Retrieve a payment by ID
    async getPaymentById(id: number): Promise<Payment> {
        try {
            const response = await axios.get<Payment>(`${API_BASE_URL}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching payment with ID ${id}:`, error);
            throw new Error("Failed to fetch payment");
        }
    }

    // Update an existing payment
    async updatePayment(id: number, payment: Payment): Promise<Payment> {
        try {
            const response = await axios.put<Payment>(
                `${API_BASE_URL}/${id}`,
                payment
            );
            return response.data;
        } catch (error) {
            console.error(`Error updating payment with ID ${id}:`, error);
            throw new Error("Failed to update payment");
        }
    }

    // Delete a payment
    async cancelPayment(id: number): Promise<void> {
        try {
            await axios.delete(`${API_BASE_URL}/${id}`);
        } catch (error) {
            console.error(`Error canceling payment with ID ${id}:`, error);
            throw new Error("Failed to cancel payment");
        }
    }

    // Retrieve payments by user ID
    async getPaymentsByUser(userId: number): Promise<Payment[]> {
        try {
            const response = await axios.get<Payment[]>(
                `${API_BASE_URL}/user/${userId}`
            );
            console.log(`Fetching payments for user ID ${userId}:`);
            return response.data;
        } catch (error: any) {
            console.error(
                `Error fetching payments for user ID ${userId}:`,
                error.response || error.message
            );
            throw new Error("Failed to fetch payments by user");
        }
    }

    // Retrieve payments with amount greater than a specified value
    async getPaymentsByAmountGreaterThan(amount: number): Promise<Payment[]> {
        try {
            const response = await axios.get<Payment[]>(
                `${API_BASE_URL}/amountGreaterThan/${amount}`
            );
            return response.data;
        } catch (error) {
            console.error(
                `Error fetching payments greater than ${amount}:`,
                error
            );
            throw new Error("Failed to fetch payments by amount");
        }
    }
}

export default new PaymentService();
