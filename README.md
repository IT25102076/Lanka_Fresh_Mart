# 🥬 Lanka Fresh Mart

An online grocery store management system built with **Spring Boot**, **Thymeleaf**, and **MySQL**.

## 🚀 Tech Stack

| Layer          | Technology                          |
| :------------- | :---------------------------------- |
| **Backend**    | Spring Boot 4.1.1, Java 21         |
| **Frontend**   | Thymeleaf, HTML5, CSS3              |
| **Database**   | MySQL (Spring Data JPA)             |
| **Security**   | Spring Security                     |
| **Payments**   | Stripe API                          |
| **Build Tool** | Maven                               |

## 📋 Features

| Module               | Description                                    |
| :------------------- | :--------------------------------------------- |
| **Product Management** | CRUD operations, image upload, catalogue view |
| **Cart Management**    | Add/remove items, quantity updates            |
| **Order Management**   | Place orders, track status, order history      |
| **Delivery Management**| Route planning, driver assignment, tracking    |
| **Inventory Management** | Stock alerts, low-stock notifications        |
| **Payment Management** | Stripe integration, refunds, financial reports |
| **Support**           | Ticket system for customer issues              |
| **Authentication**    | Login, registration, role-based access         |

## 📁 Project Structure

```
Lanka_Fresh_Mart/
├── src/main/java/com/lankafreshmart/lanka_fresh_mart/
│   ├── config/          # Security, Web, DataSeeder configs
│   ├── controller/      # MVC Controllers (12 controllers)
│   ├── dto/             # Data Transfer Objects
│   ├── model/           # JPA Entity classes (13 models)
│   ├── repository/      # Spring Data JPA Repositories
│   └── service/         # Business logic layer (14 services)
├── src/main/resources/
│   ├── templates/       # Thymeleaf HTML templates
│   ├── static/css/      # Stylesheets
│   ├── application.properties
│   └── application-secrets.properties  (gitignored)
├── documentation/       # Project docs, reports, diagrams
├── pom.xml
└── README.md
```

## ⚙️ Prerequisites

- **Java 21** or later
- **Maven 3.9+**
- **MySQL 8.0+**

## 🛠️ Setup & Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Lanka_Fresh_Mart
   ```

2. **Configure the database**

   Create a MySQL database and update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lanka_fresh_mart
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Add secrets**

   Create `src/main/resources/application-secrets.properties` with your API keys:
   ```properties
   stripe.api.key=sk_test_...
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Open in browser**
   ```
   http://localhost:8080
   ```

## 👥 Team

| Name                    | Student ID   | Role                  |
| :---------------------- | :----------- | :-------------------- |
| Ranasinghe R A I M      | IT25102250   | Team Leader / Cart    |
| Balasuriya B. M. S. H   | IT25103034   | Product Management    |
| Padmakumara I. M. M. D  | IT25102076   | Order Management      |
| Nambikandage D. A.      | IT25510376   | Delivery Management   |
| Ananya P. K. O.         | IT25510327   | Inventory Management  |
| Fernando N. A. S.       | IT25101280   | Payment Management    |

## 📄 License

This project is developed as part of the SLIIT IT2140 course (Y2S1).
