# 🎮 Video Game Store — E-Commerce API

A backend REST API for an online video game store, built with Spring Boot and MySQL. This project was developed as a solo capstone for Year Up United, simulating the role of a backend developer inheriting and extending an existing e-commerce codebase.

---

## 📋 Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Interesting Code](#interesting-code)
- [Screenshots](#screenshots)

---

## About the Project

This API powers a video game e-commerce storefront. Users can browse products by category, search and filter the product catalog, manage a personal shopping cart, and view/update their profile. The project involved fixing existing bugs in the codebase and implementing several new features from scratch.

---

## Features

- ✅ User registration and JWT-based login
- ✅ Browse and search products by category, price range, and sub-category
- ✅ Full CRUD for categories (admin only)
- ✅ Shopping cart — add, update quantity, and clear cart (persisted per user)
- ✅ User profile — view and update profile information
- ✅ Bug fixes: product search filter logic and product stock update

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL |
| Build Tool | Maven |
| Testing | Insomnia |

---

## Database Schema

![Database Schema](images/schema.png)

> The database includes a `users`, `profiles`, `products`, `categories`, `shopping_cart`, `orders`, and `order_line_items` table.

---

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Setup

1. **Clone the repository**
```bash
git clone https://github.com/Vandrae/e-commerce-api.git
cd e-commerce-api
```

2. **Create the database**

Open MySQL Workbench and run the script located in the `/database` folder.

3. **Configure environment variables**

Create an `application.properties` file or set the following environment variables:

```
DATASOURCE_URL=jdbc:mysql://localhost:3306/your_database_name
DATASOURCE_USERNAME=your_mysql_username
DATASOURCE_PASSWORD=your_mysql_password
```

4. **Run the application**
```bash
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`.

### Demo Users

| Username | Password | Role |
|---|---|---|
| user | password | USER |
| admin | password | ADMIN |
| george | password | USER |

---

## API Endpoints

### Auth
| Method | URL | Description |
|---|---|---|
| POST | `/register` | Register a new user |
| POST | `/login` | Login and receive JWT token |

### Categories
| Method | URL | Auth |
|---|---|---|
| GET | `/categories` | Public |
| GET | `/categories/{id}` | Public |
| GET | `/categories/{id}/products` | Public |
| POST | `/categories` | Admin only |
| PUT | `/categories/{id}` | Admin only |
| DELETE | `/categories/{id}` | Admin only |

### Products
| Method | URL | Auth |
|---|---|---|
| GET | `/products` | Public |
| GET | `/products/{id}` | Public |
| POST | `/products` | Admin only |
| PUT | `/products/{id}` | Admin only |
| DELETE | `/products/{id}` | Admin only |

### Shopping Cart
| Method | URL | Auth |
|---|---|---|
| GET | `/cart` | User |
| POST | `/cart/products/{id}` | User |
| PUT | `/cart/products/{id}` | User |
| DELETE | `/cart` | User |

### Profile
| Method | URL | Auth |
|---|---|---|
| GET | `/profile` | User |
| PUT | `/profile` | User |

---

## Interesting Code

### Shopping Cart — Add or Increment Logic

One of the more interesting pieces of logic is in `ShoppingCartService.addToCart()`. Rather than blindly inserting a new row, the method first checks whether the product already exists in the user's cart. If it does, it increments the quantity by 1. If it doesn't, it inserts a new cart item with a quantity of 1.

```java
public ShoppingCart addToCart(int userId, int productId) {
    CartItem items = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
    if (items == null) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setUserId(userId);
        cartItem.setQuantity(1);
        shoppingCartRepository.save(cartItem);
    } else {
        items.setQuantity(items.getQuantity() + 1);
        shoppingCartRepository.save(items);
    }
    return getByUserId(userId);
}
```

This approach keeps the cart data clean — no duplicate rows per product — and makes the POST endpoint idempotent in a practical sense.

---

## Screenshots

> _Add your screenshots here after taking them from the running app and Insomnia._

**Register New User**
![Product Search](images/product-search.png)

**Shopping Cart Response**
![Shopping Cart](images/shopping-cart.png)

**Update Profile**
![Login](images/login.png)
Video Game E-Commerce README
