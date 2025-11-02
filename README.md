# 🧩 Java Spark Web App v1.0

A modern **Java RESTful web application** built using the [Spark Java](http://sparkjava.com/) micro-framework.
It provides **real-time updates for collectibles and bidding** through WebSockets and a layered architecture featuring controllers, services, DAOs, and an in-memory database (H2).

## 📋 Table of Contents

* [Overview](#-overview)
* [Architecture](#-architecture)
* [Technologies](#-technologies)
* [Project Structure](#-project-structure)
* [Installation & Setup](#-installation--setup)
* [Running the Application](#-running-the-application)
* [API Endpoints](#-api-endpoints)

    * [User API](#user-api)
    * [Item API](#item-api)
    * [Offer API](#offer-api)
* [WebSocket Channels](#-websocket-channels)
* [Validation & Error Handling](#-validation--error-handling)
* [Examples](#-examples)
* [Troubleshooting](#-troubleshooting)
* [Contributors](#-contributors)

---

## 🧠 Overview

The **Java Spark Web App** serves as a demonstration of clean, modular web application development in Java.
It’s designed to manage **collectible items**, **users**, and **offers/bids**, providing both REST APIs and server-rendered Mustache views.

Key features include:

* RESTful CRUD APIs for Users, Items, and Offers.
* Real-time updates via WebSocket when items or offers change.
* In-memory H2 database with sample data.
* Validation and structured exception handling.
* Clear separation of concerns (Controller → Service → DAO → Database).

---

## 🧩 Architecture

The application uses a layered structure:

```
HTTP Request
     │
     ▼
Spark Java Routes (Router Layer)
     │
Controllers (API & Web)
     │
Services (Business Logic)
     │
DAOs (Database Access via JDBI)
     │
Database (H2, MySQL, PostgreSQL supported)
```

**Real-time updates** are achieved via a simple in-app EventBus that dispatches domain events to WebSocket handlers, pushing updates to all connected clients.

---

## 🧰 Technologies

| Category           | Technology                                                              |
| ------------------ |-------------------------------------------------------------------------|
| Web Framework      | [Spark Java 2.9.4](http://sparkjava.com/)                               |
| Template Engine    | [Mustache](https://mustache.github.io/)                                 |
| Database           | [H2 (in-memory)](https://www.h2database.com/)                           |
| ORM/Database Layer | [JDBI 3](https://jdbi.org/)                                             |
| JSON Serialization | [Gson](https://github.com/google/gson)                                  |
| Logging            | [SLF4J + Logback](https://logback.qos.ch/)                              |
| Environment Config | [Java Dotenv](https://github.com/cdimascio/java-dotenv)                 |
| Validation         | [Jakarta Validation](https://beanvalidation.org/) (Hibernate Validator) |
| WebSockets         | [Spark WebSocket API](https://sparkjava.com/documentation#websockets)                      |

---

## 📁 Project Structure

```
src/main/java/com/pikolinc/
├── app/
│   ├── initializer/            # Initializers (DB, Routes, WebSockets)
│   ├── ServerInitializer.java  # Bootstraps the app
├── controllers/                # API controllers
├── dao/                        # JDBI DAOs
├── domain/                     # Entities and Enums
├── dto/                        # DTOs for requests/responses
├── exceptions/                 # Exception handling
├── infraestructure/events/     # Domain event system
├── routes/                     # REST and web route registration
├── services/                   # Business logic layer
├── util/                       # Validation utilities
└── ws/                         # WebSocket handlers
```

---

## ⚙️ Installation & Setup

### Prerequisites

* **Java 17+**
* **Maven 3.8+**

### Clone and build

```bash
git clone https://github.com/Kaserola4/java-spark-web-app.git
cd java-spark-web-app
mvn clean install
```

### Run locally

```bash
mvn exec:java
```

The application will start on port **8080** (default).
Visit [http://localhost:8080](http://localhost:8080)

---

## 🚀 Running the Application

Once started, you’ll see logs like:

```
🚀 Application started on port 8080
Running initializer: DatabaseInitializer
Running initializer: RoutesInitializer
```

You can access:

* Web interface: [http://localhost:8080](http://localhost:8080)
* Health check: [http://localhost:8080/health](http://localhost:8080/health)

---

## 🔗 API Endpoints

All REST endpoints are prefixed with `/api/v1`.

### 🧍 User API

| Method   | Endpoint            | Description     |
| -------- | ------------------- | --------------- |
| `GET`    | `/api/v1/users`     | Get all users   |
| `GET`    | `/api/v1/users/:id` | Get user by ID  |
| `POST`   | `/api/v1/users`     | Create new user |
| `PUT`    | `/api/v1/users/:id` | Update user     |
| `DELETE` | `/api/v1/users/:id` | Delete user     |

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","age":30,"email":"john@example.com"}'
```

---

### 📦 Item API

| Method   | Endpoint            | Description     |
| -------- | ------------------- | --------------- |
| `GET`    | `/api/v1/items`     | Get all items   |
| `GET`    | `/api/v1/items/:id` | Get item by ID  |
| `POST`   | `/api/v1/items`     | Create new item |
| `PUT`    | `/api/v1/items/:id` | Update item     |
| `DELETE` | `/api/v1/items/:id` | Delete item     |

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/v1/items \
  -H "Content-Type: application/json" \
  -d '{"name":"Rare Card","description":"Limited edition card","price":150.0}'
```

---

### 💰 Offer API

| Method   | Endpoint                      | Description                 |
| -------- | ----------------------------- | --------------------------- |
| `GET`    | `/api/v1/offers`              | Get all offers              |
| `GET`    | `/api/v1/offers/:id`          | Get offer by ID             |
| `POST`   | `/api/v1/offers`              | Create new offer            |
| `PUT`    | `/api/v1/offers/:id`          | Update offer                |
| `DELETE` | `/api/v1/offers/:id`          | Delete offer                |
| `PATCH`  | `/api/v1/offers/:id/amount`   | Update offer amount (rebid) |
| `PUT`    | `/api/v1/offers/:id/accept`   | Accept offer                |
| `PUT`    | `/api/v1/offers/:id/reject`   | Reject offer                |
| `PUT`    | `/api/v1/offers/:id/complete` | Complete offer              |
| `PUT`    | `/api/v1/offers/:id/cancel`   | Cancel offer                |

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/v1/offers \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"itemId":3,"finalPrice":199.99}'
```

---

## 🌐 WebSocket Channels

| Path         | Description                  |
| ------------ | ---------------------------- |
| `/ws`        | Real-time updates for items  |
| `/ws/offers` | Real-time updates for offers |

Clients can subscribe via standard WebSocket clients:

```javascript
const socket = new WebSocket("ws://localhost:8080/ws/offers");
socket.onmessage = (event) => console.log("Update:", event.data);
```

---

## 🛡️ Validation & Error Handling

All requests are validated using Jakarta Bean Validation.
If validation fails, the API responds with a JSON body:

```json
{
  "error": "Validation failed",
  "details": ["price: must be greater than zero"]
}
```

Common response codes:

* `200` — OK
* `201` — Created
* `400` — Bad Request (invalid data)
* `404` — Not Found
* `409` — Duplicate resource
* `500` — Internal Server Error

---

## 🧪 Examples

* Access homepage: [http://localhost:8080](http://localhost:8080)
* Health check: [http://localhost:8080/health](http://localhost:8080/health)
* View all items (GET): [http://localhost:8080/api/v1/items](http://localhost:8080/api/v1/items)
* View offers for a specific item: [http://localhost:8080/api/v1/offers/item/1](http://localhost:8080/api/v1/offers/item/1)

---

## 🧰 Troubleshooting

| Issue                  | Solution                                                        |
| ---------------------- | --------------------------------------------------------------- |
| `Port already in use`  | Stop the process or set a custom port using `PORT` env variable |
| `mvn exec:java` fails  | Ensure you are in the project root and Java 17+ is installed    |
| WebSocket not updating | Check browser console for WebSocket connection errors           |

---

## 👨‍💻 Contributors

* **[Kaserola4](https://github.com/Kaserola4)** — Creator & Maintainer

---