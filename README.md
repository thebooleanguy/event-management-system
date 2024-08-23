# Event Management System

![Event Management System Interface](path/to/your/image.png)

## Overview

The Event Management System is a university project designed to demonstrate the use of microservices architecture using Spring Boot. The system enables users to browse, book, and manage tickets for various events like musical shows, concerts, and theater performances. Organizers can create and manage events, while the system tracks ticket sales and user interactions.

## Microservices Architecture

The application is divided into the following microservices:

1. **User Service**: Handles user registration, authentication, and profile management.
2. **Event Service**: Manages the creation, editing, and retrieval of events.
3. **Ticket Service**: Manages the booking, cancellation, and retrieval of tickets.
4. **Payment Service**: Processes payments and maintains payment history.
5. **Notification Service**: Sends notifications to users about bookings, cancellations, and event updates.
6. **Analytics Service**: Tracks user interactions, ticket sales, and event metrics.

## Key Features

- **User Service**:
  - Registration, login, and logout functionality.
  - Profile management for users.
  - RESTful endpoints: `/register`, `/login`, `/logout`, `/profile`.
  
- **Event Service**:
  - CRUD operations for event management.
  - List and detailed view of events.
  - RESTful endpoints: `/events`, `/events/{id}`.
  
- **Ticket Service**:
  - Booking, viewing, and cancellation of tickets.
  - RESTful endpoints: `/tickets`, `/tickets/{id}`, `/tickets/user/{userId}`.
  
- **Payment Service**:
  - Processes payments for ticket bookings.
  - RESTful endpoints: `/payments`, `/payments/user/{userId}`, `/payments/ticket/{ticketId}`.
  
- **Notification Service**:
  - Sends notifications related to bookings, cancellations, and updates.
  - RESTful endpoints: `/notifications`, `/notifications/user/{userId}`, `/notifications/{id}`.
  
- **Analytics Service**:
  - Tracks and analyzes user interactions and sales metrics.
  - RESTful endpoints: `/analytics/user/{userId}`, `/analytics/event/{eventId}`, `/analytics/ticket/{ticketId}`.

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Frontend**: React
- **Database**: MySQL
- **API Design**: RESTful APIs
- **Other**: Maven
