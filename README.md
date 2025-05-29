# Real Estate Web Application

## Overview

This Real Estate Web Application is a full-stack platform designed to streamline property transactions between buyers, sellers, agents, and admins. It offers robust features including user management, property listing and browsing, booking and payment handling, real-time messaging, reviews, and notifications.

## Technology Stack

- **Frontend:**  
  Angular 17, TypeScript, RxJS, Angular Material / Bootstrap (optional)

- **Backend:**  
  Java, Spring Boot (or your preferred Java framework), RESTful APIs

- **Database:**  
  Relational DBMS (MySQL / PostgreSQL recommended)

- **Authentication:**  
  JWT tokens, role-based security

- **Payment Gateway:**  
  Stripe and/or PayPal integration

- **Messaging:**  
  WebSocket or REST-based real-time chat

- **Build Tools:**  
  Maven / Gradle (Java), Angular CLI (frontend)


## Features

### Buyer
- Register/Login securely.
- Search and filter properties by city, price, amenities, and more.
- View detailed property pages with images and descriptions.
- Save and favorite properties for later reference.
- Schedule property visits and book viewings.
- Communicate directly with sellers or agents via real-time chat.
- Compare properties side-by-side.
- Make payments securely via integrated payment gateways (Stripe/PayPal).
- Post ratings and reviews after booking or purchasing.
- Access a personalized dashboard summarizing saved items, bookings, and payments.

### Seller
- Register/Login and manage seller profile.
- Add, edit, and delete property listings with details such as price, location, amenities, and images.
- View listing performance metrics (views, inquiries, bookings).
- Receive and respond to buyer inquiries and booking requests.
- Manage booking confirmations and track earnings.
- Receive and manage buyer reviews.

### Agent (Optional)
- Assist buyers and sellers with listing management.
- Schedule and manage property viewings.
- Facilitate communication between parties.

### Admin
- Secure login for platform management.
- Approve or reject new property listings.
- Manage all user accounts including buyers, sellers, and agents.
- Monitor and manage payments and resolve disputes.
- Oversee reviews and flag inappropriate content.
- Access comprehensive analytics on users, revenue, and platform activity.

## Architecture

### Core Modules
- **User Management:** Role-based authentication (Buyer, Seller, Admin, Agent), registration, and profile management.
- **Property Module:** Listing creation, editing, approval workflows, and status tracking.
- **Search & Filters:** Advanced filtering with location-based autocomplete and keyword search.
- **Booking & Payment:** Viewing scheduling, booking confirmations, and secure payment integration.
- **Messaging System:** Real-time chat stored with timestamps.
- **Reviews & Ratings:** Post-booking reviews with multi-criteria rating.
- **Notification Module:** Alerts for bookings, inquiries, reviews, and administrative actions.
- **Dashboard Module:** Role-specific dashboards with relevant insights and actions.

### Database Entities & Relationships

- **User:** Contains details and roles (Buyer, Seller, Agent, Admin).
- **Property:** Associated with sellers; includes detailed descriptions and images.
- **Booking:** Links buyers to properties with booking and visit dates.
- **Payment:** Tied to bookings, records transaction details.
- **Message:** Records communication between users.
- **Review:** Captures buyer feedback on properties.
- **Notification:** Manages alerts sent to users.
