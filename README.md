This **README.md** file for  completed **E-Commerce Order Management System** project. This file provides all the necessary information for setting up and running the project.

---

# **E-Commerce Order Management System**

## **Project Overview**
The **E-Commerce Order Management System** allows users to place, update, and cancel orders in an online e-commerce store. Built with **Java**, **Spring Boot**, **Microservices Architecture**, and **Kafka/RabbitMQ** for messaging queues, this system efficiently handles order, product, and customer management. It also utilizes **Redis** for caching and **MySQL** for database storage, ensuring high availability and performance.

## **Technologies Used**
- **Java 11**
- **Spring Boot** for building REST APIs
- **Microservices Architecture** for decoupling services
- **Kafka/RabbitMQ** for asynchronous messaging
- **Redis** for caching frequently accessed data
- **MySQL** for persistent storage
- **JUnit & Mockito** for testing
- **Maven** for dependency management

## **Project Structure**

The system is divided into microservices for each domain:
1. **Order Service**: Manages all order-related operations.
2. **Product Service**: Manages product information and inventory.
3. **Customer Service**: Manages customer data.

### **Microservices Communication**
- **REST APIs**: Exposed by each microservice for communication.
- **Kafka/RabbitMQ**: Used for asynchronous communication like order status updates and notifications.

## **System Design**

### **Order Module**
- **Order**: The core component for managing orders with fields like `orderId`, `productId`, `customerId`, `orderDate`, `status`, and `totalAmount`.
- **Operations**: Place, update, and cancel orders.

### **Microservices Architecture**
Each service runs independently, communicating with other services via REST APIs and asynchronous messaging queues. The **Order Service** communicates with the **Product Service** and **Customer Service** to fetch necessary details and perform operations.

### **Messaging Queue (Kafka/RabbitMQ)**
- **Kafka/RabbitMQ** handles asynchronous communication. Example use case: Order status updates, notifications.

### **Database & Caching**
- **MySQL** stores order data, product information, and customer details.
- **Redis** caches frequently accessed data like product details for improved performance.

### **Unit & Integration Testing**
- **JUnit** and **Mockito** are used for writing unit and integration tests to ensure the system works as expected.

## **API Endpoints**

### **Order Service**
#### **Create Order**
- **POST** `/orders/place`
  - Creates a new order.
  - Request Body:
    ```json
    {
  "productId": 1,
  "quantity": 500,
  "customerId": 2,
  "orderDate": "2024-12-16T12:00:00",
  "status": "PENDING",
  "totalAmount": 250.00
}
    ```
  - Response:
    ```json
    {
    "orderId": 99,
    "productId": 1,
    "quantity": 500,
    "customerId": 2,
    "orderDate": "2024-12-19T01:29:45.6459872",
    "status": "PENDING",
    "price": 70000.5,
    "totalAmount": 3.500025E7
    }
    ```

#### **Update Order Status**
- **PUT** `/orders/{orderId}`
  - Updates the status of an order.
  - Request Body:
    ```json
     "productId": 3,
  "customerId": 2,
  "orderDate": "2024-12-16T12:00:00",
  "status": "PROCESSING",
  "totalAmount": 250.00
}
    ```
  - Response:
    ```json
    {
    "orderId": 88,
    "productId": 1,
    "quantity": 500,
    "customerId": 2,
    "orderDate": "2024-12-18T21:15:53.130461",
    "status": "PROCESSING",
    "price": 70000.5,
    "totalAmount": 3.500025E7
}
    ```

#### **Cancel Order**
- **DELETE** `/orders/{orderId}`
  - Cancels an existing order.
  - Response:
    ```json
    Order cancelled successfully.
    ```

### **Product Service**
#### **Get Product Details**
- **GET** `/products/{productId}`
  - Retrieves details of a product.
  - Response:
    ```json
    {
      "productId": "001",
      "name": "Product A",
      "price": 50.00,
      "stockQuantity": 100
    }
    ```

### **Customer Service**
#### **Get Customer Details**
- **GET** `/customers/{customerId}`
  - Retrieves details of a customer.
  - Response:
    ```json
    {
      "customerId": "12345",
      "name": "John Doe",
      "email": "johndoe@example.com"
    }
    ```

## **Project Setup**

### **Prerequisites**
Before setting up the project, ensure you have the following installed:
1. **Java 11** or higher
2. **Maven** for building the project
3. **Kafka** (or RabbitMQ) for asynchronous messaging
4. **Redis** for caching
5. **MySQL** for storing order-related data

### **Clone the Repository**
```bash
git clone https://github.com/AmolNisargan/amol_nisargan_java_project2
cd ecommerce-order-management-system
```

### **Build the Project**
1. **Install Dependencies**:
   ```bash
   mvn clean install
   ```

2. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

### **Kafka or RabbitMQ Setup**
- **Kafka**: Follow [Kafka Setup Guide](https://kafka.apache.org/quickstart) to set up and run Kafka.
- **RabbitMQ**: Follow [RabbitMQ Setup Guide](https://www.rabbitmq.com/getstarted.html) to set up RabbitMQ.

### **Testing the Application**
Run unit tests to verify the functionality of the system:
```bash
mvn test
```

## **Contributing**

We welcome contributions! If you'd like to contribute to this project, please follow these steps:
1. Fork this repository.
2. Create a new branch for your changes (`git checkout -b feature-name`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to your branch (`git push origin feature-name`).
5. Open a Pull Request.

## **License**
This project is licensed under the MIT License.

---

This **README.md** file provides a structured overview of project and guides others on how to set up, run, and contribute to the system.
