#!/bin/bash

# Start Kitty with multiple tabs
kitty @ new-tab --title 'User Service' bash -c 'cd backend/user-service && mvn spring-boot:run; exec bash' &
kitty @ new-tab --title 'Event Service' bash -c 'cd backend/event-service && mvn spring-boot:run; exec bash' &
kitty @ new-tab --title 'Ticket Service' bash -c 'cd backend/ticket-service && mvn spring-boot:run; exec bash' &

# Uncomment and add additional microservices as needed
# kitty @ new-tab --title 'Notification Service' bash -c 'cd backend/notification-service && mvn spring-boot:run; exec bash' &
# kitty @ new-tab --title 'Payment Service' bash -c 'cd backend/payment-service && mvn spring-boot:run; exec bash' &
# kitty @ new-tab --title 'Analytics Service' bash -c 'cd backend/analytics-service && mvn spring-boot:run; exec bash' &

echo "All microservices are running. Press Ctrl+C to stop."
