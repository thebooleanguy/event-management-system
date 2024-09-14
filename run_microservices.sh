#!/bin/bash

# Function to handle termination signal
cleanup() {
    echo "Stopping all microservices..."
    kill $user_service_pid
    kill $event_service_pid
    kill $booking_service_pid
    # Uncomment and add additional microservices as needed
    kill $notification_service_pid
    kill $payment_service_pid
    # kill $analytics_service_pid
    # kill $eureka_server_pid
    exit 0
}

# Trap Ctrl+C (SIGINT) to call cleanup function
trap cleanup SIGINT

cd backend

# cd backend/common
# mvn install
# sleep 3

# cd ../eureka-server
# mvn spring-boot:run &
# eureka_server_pid=$!
# sleep 3

# Start each microservice and capture their PIDs
cd ./user-service
mvn spring-boot:run &
user_service_pid=$!
sleep 3

cd ../event-service
mvn spring-boot:run &
event_service_pid=$!
sleep 3

cd ../booking-service
mvn spring-boot:run &
booking_service_pid=$!
sleep 3

# Uncomment and add additional microservices as needed
cd ../notification-service
mvn spring-boot:run &
notification_service_pid=$!
sleep 3

cd ../payment-service
mvn spring-boot:run &
payment_service_pid=$!
sleep 3

# cd ../analytics-service
# mvn spring-boot:run &
# analytics_service_pid=$!
# sleep 3

echo "All microservices are running. Press Ctrl+C to stop."
wait

