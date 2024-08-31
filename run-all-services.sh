#!/bin/bash

# Start each service in the background
(cd backend/user-service && ./mvnw spring-boot:run) &
(cd backend/event-service && ./mvnw spring-boot:run) &
(cd backend/notification-service && ./mvnw spring-boot:run) &

# Wait for all background processes to finish
wait
