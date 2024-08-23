# 🎟️ Event Management System

![Event Management System Interface](misc/images/ui-mockup.jpeg)

## Overview

The Event Management System is a university project designed to demonstrate the use of microservices architecture using Spring Boot. The system enables users to browse, book, and manage tickets for various events like musical shows, concerts, and theater performances. Organizers can create and manage events, while the system tracks ticket sales and user interactions.

## 🏗️ Microservices Architecture

The application is divided into the following microservices:

1. **👤 User Service**: Handles user registration, authentication, and profile management.
2. **🎫 Event Service**: Manages the creation, editing, and retrieval of events.
3. **🛒 Ticket Service**: Manages the booking, cancellation, and retrieval of tickets.
4. **💳 Payment Service**: Processes payments and maintains payment history.
5. **🔔 Notification Service**: Sends notifications to users about bookings, cancellations, and event updates.
6. **📊 Analytics Service**: Tracks user interactions, ticket sales, and event metrics.

## 🌟 Key Features

- **👤 User Service**:
  - Registration, login, and logout functionality.
  - Profile management for users.
  - RESTful endpoints: `/register`, `/login`, `/logout`, `/profile`.
  
- **🎫 Event Service**:
  - CRUD operations for event management.
  - List and detailed view of events.
  - RESTful endpoints: `/events`, `/events/{id}`.
  
- **🛒 Ticket Service**:
  - Booking, viewing, and cancellation of tickets.
  - RESTful endpoints: `/tickets`, `/tickets/{id}`, `/tickets/user/{userId}`.
  
- **💳 Payment Service**:
  - Processes payments for ticket bookings.
  - RESTful endpoints: `/payments`, `/payments/user/{userId}`, `/payments/ticket/{ticketId}`.
  
- **🔔 Notification Service**:
  - Sends notifications related to bookings, cancellations, and updates.
  - RESTful endpoints: `/notifications`, `/notifications/user/{userId}`, `/notifications/{id}`.
  
- **📊 Analytics Service**:
  - Tracks and analyzes user interactions and sales metrics.
  - RESTful endpoints: `/analytics/user/{userId}`, `/analytics/event/{eventId}`, `/analytics/ticket/{ticketId}`.

## 💻 Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Frontend**: React
- **Database**: MySQL
- **API Design**: RESTful APIs
- **Other**: Maven

## 🗂️ Project Structure

```markdown
event-management-system/
|
├── backend/                              # 🖥️ Contains all the Spring Boot microservices
|   |
│   ├── user-service/                     # 👤 Microservice for user management
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   │   └── lk/nibm/userservice/    # 📂 Java package for User Service
│   │   │   │   │       ├── controller/         # 🚏 REST controllers for handling HTTP requests
│   │   │   │   │       ├── service/            # 🛠️ Business logic and service classes
│   │   │   │   │       ├── model/              # 🗄️ Entity models representing database tables
│   │   │   │   │       └── repository/         # 📚 Repository interfaces for database operations
│   │   │   │   └── resources/
│   │   │   │       ├── application.properties  # ⚙️ Configuration file for Spring Boot
│   │   │   └── test/
│   │   │       └── java/
│   │   │           └── lk/nibm/userservice/    # 🧪 Unit and integration tests for User Service
│   │   └── pom.xml                            # 📝 Maven build file for User Service
│   ├── event-service/                         # 🎫 Microservice for event management
│   ├── ticket-service/                        # 🛒 Microservice for ticket management
│   ├── payment-service/                       # 💳 Microservice for payment processing
│   ├── notification-service/                  # 🔔 Microservice for sending notifications
│   └── analytics-service/                     # 📊 Microservice for analytics and reporting
|
├── frontend/                                  # 🌐 React frontend application
│   ├── public/
│   │   ├── index.html                         # 📄 Main HTML file for the React app
│   │   └── favicon.ico                        # 🔖 Favicon for the React app
│   ├── src/
│   │   ├── components/                        # 🧩 Reusable UI components (e.g., Navbar, Footer)
│   │   ├── pages/                             # 📑 Pages representing different views (e.g., HomePage, EventPage)
│   │   ├── services/                          # 🛠️ API service modules for interacting with backend
│   │   ├── App.js                             # 🏠 Main application component
│   │   ├── index.js                           # 🚀 Entry point for the React application
│   │   └── styles/                            # 🎨 CSS or SCSS stylesheets for the application
│   ├── package.json                           # 📦 Project dependencies and scripts for the React app
│   └── .env                                   # 🌍 Environment variables for frontend configuration
|
└── misc/                                      # 🗃️ Miscellaneous files (e.g., images, SQL scripts)
```
