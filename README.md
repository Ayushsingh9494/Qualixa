# 🚀 Qualixa - AI-Powered Test Automation Studio

Qualixa is a premium, state-of-the-art web-based **AI QA Engineer** that automates the lifecycle of software testing. From translating product requirements to executing end-to-end automated UI scripts, Qualixa accelerates QA timelines with artificial intelligence.

---

## ✨ Key Features

- **🧠 AI Test Case Generation**: Translates software requirements into comprehensive, structured QA test suites using the Gemini AI API.
- **💻 Automated Selenium Generator**: Automatically generates complete, executable Selenium WebDriver Java classes with TestNG annotations and optimal wait patterns.
- **⚡ Execution & Reports**: Runs tests locally or in simulation, capturing detailed status reports, durations, and SVG-rendered visual execution reports.
- **🛡️ Secure Registration & Login**:
  - **📧 DNS MX Verification**: Validates registration emails by performing a DNS look-up for domain Mail Exchange (MX) records to filter out dummy domains.
  - **🔒 Strong Password Constraints**: Enforces password requirements (length >= 8, uppercase, lowercase, digits, and special characters).
- **🗂️ Isolated User Workspaces**: Total account-based data separation. User dashboards and QA Studio assets are strictly private to each logged-in account.
- **💎 Premium UX**: Features a responsive dark mode glassmorphic UI, harmonious visual palettes, micro-animations, and fluid transitions.

---

## 🛠️ Tech Stack

### Backend
- **Core**: Java 21 & Spring Boot 3
- **Security**: Spring Security (Token-Based Bearer Sessions)
- **Database**: PostgreSQL (JPA/Hibernate)
- **AI**: Gemini 2.5 API Integrations

### Frontend
- **Framework**: Vite + React
- **Styling**: Vanilla CSS, Bootstrap & Lucide React Icons

---

## 🚀 Getting Started

### 📋 Prerequisites
Make sure you have the following installed:
- [Java Development Kit (JDK 21)](https://www.oracle.com/java/technologies/downloads/)
- [Node.js (v18+)](https://nodejs.org/)
- [PostgreSQL Database Server](https://www.postgresql.org/download/)
- A **Gemini API Key** from [Google AI Studio](https://aistudio.google.com/)

---

### ⚙️ Installation & Setup

#### 1. Database Setup
Create a PostgreSQL database named `qacopilot`:
```sql
CREATE DATABASE qacopilot;
```

#### 2. Backend Configuration
Set your Gemini API key as an environment variable:
- **Windows (CMD)**: `set GEMINI_API_KEY=your_gemini_api_key`
- **Windows (PowerShell)**: `$env:GEMINI_API_KEY="your_gemini_api_key"`
- **macOS/Linux**: `export GEMINI_API_KEY="your_gemini_api_key"`

Ensure your database user credentials match `backend/src/main/resources/application.properties`.

---

### 🏃 Running the Application

#### Run the Backend Server
```bash
cd backend
mvn clean compile
mvn spring-boot:run
```
The server will start on **http://localhost:8080**.

#### Run the Frontend Studio
```bash
cd frontend
npm install
npm run dev
```
The application will launch on **http://localhost:5173**.

---

## 📂 Project Structure

```text
├── backend/                  # Spring Boot 3 Java Application
│   ├── src/main/java/        # Service layers, entity schemas, controllers
│   ├── src/main/resources/   # Application configs and static mock views
│   └── pom.xml               # Maven configuration & dependencies
└── frontend/                 # Vite & React SPA Studio
    ├── src/components/       # Reusable UI cards, panels, modals
    ├── src/services/         # API hooks and interceptors
    ├── src/index.css         # Styling system
    └── package.json          # Node configurations
```

---

## 🛡️ License
Distributed under the MIT License. See `LICENSE` for more information.

Enjoy automation with **Qualixa**! 🚀✨
