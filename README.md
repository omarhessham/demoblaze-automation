# Demoblaze Automation Testing 🚀

## 📌 Project Overview
This project automates end-to-end and negative test scenarios for the [Demoblaze](https://www.demoblaze.com/) application using **Selenium**, **Cucumber (BDD)**, and **TestNG**.  
The goal is to validate critical functionalities such as **Sign Up, Login, Add to Cart, and Checkout**, including negative edge cases.

---

## ⚙️ Tools & Technologies Used
- **Java 17**
- **Selenium WebDriver 4.23**
- **Cucumber 7.18**
- **TestNG 7.10**
- **Maven** (build & dependency management)
- **Extent Reports / Cucumber HTML Reports**
- **Faker** (for random test data generation)

---

## 🧪 Test Scenarios
✔️ Positive Scenarios
- Sign up with a new random user.
- Log in successfully with valid credentials.
- Add multiple products to the cart and validate totals.

✔️ Negative Scenarios
1. Try signing up with an existing username → error message.
2. Attempt to purchase with an expired/missing credit card → error message.
3. Add the same product twice → verify cart updates correctly.
4. Attempt to log in with invalid credentials → error message.

---

## ▶️ How to Run Tests
1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/demoblaze-automation.git
   cd demoblaze-automation
