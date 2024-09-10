#!/bin/bash

# Function to handle termination signal
cleanup() {
    echo "Stopping all microservices..."
    kill $user_service_pid
    kill $event_service_pid
    kill $ticket_service_pid
    # Uncomment and add additional microservices as needed
    kill $notification_service_pid
    # kill $payment_service_pid
    # kill $analytics_service_pid
    kill $eureka_server_pid
    exit 0
}

# Trap Ctrl+C (SIGINT) to call cleanup function
trap cleanup SIGINT

cd backend/common
mvn install
sleep 2

cd ../eureka-server
mvn spring-boot:run &
eureka_server_pid=$!
sleep 2

# Start each microservice and capture their PIDs
cd ../user-service
mvn spring-boot:run &
user_service_pid=$!

cd ../event-service
mvn spring-boot:run &
event_service_pid=$!

cd ../ticket-service
mvn spring-boot:run &
ticket_service_pid=$!

# Uncomment and add additional microservices as needed
cd ../notification-service
mvn spring-boot:run &
notification_service_pid=$!

# cd ../payment-service
# mvn spring-boot:run &
# payment_service_pid=$!

# cd ../analytics-service
# mvn spring-boot:run &
# analytics_service_pid=$!

echo "All microservices are running. Press Ctrl+C to stop."
wait

