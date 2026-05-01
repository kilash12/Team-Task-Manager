# Team Task Manager

[![Java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://www.mysql.com/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-3.4-38B2AC.svg)](https://tailwindcss.com/)
[![License](https://img.shields.io/badge/License-ISC-blue.svg)](/LICENSE)

> **A full‑stack project and task management system with role‑based access (Admin/Member).** Built with Spring Boot, Thymeleaf, MySQL, and Tailwind CSS.

**Live Demo:** [Team Task Manager on Railway](#) *(update with your live URL)*

---

## Features

All features work flawlessly in both **Light** and **Dark Mode** and are fully responsive.

### Authentication & Role‑Based Access
- **Secure Signup/Login** – Passwords encrypted with BCrypt.
- **Email Verification** – Users verify their email before accessing the app.
- **OAuth2 Login** – Sign in with Google or GitHub.
- **Two Roles**:
  - **Admin** – Full control: create projects, add members, create/assign tasks.
  - **Member** – View only assigned tasks; update their status.

### Project Management
- **Create Projects** – Admins can create projects with name and description.
- **Team Management** – Admins can add existing users as members to any project.
- **Project Listing** – Admins see all projects they created; members see only the projects they belong to.

### Task Management
- **Create & Assign Tasks** – Admins assign tasks to specific projects and team members.
- **Set Due Dates** – Each task has a title, description, and due date.
- **Status Workflow** – Tasks progress from `TODO` → `IN_PROGRESS` → `DONE`.
- **Status Updates** – Members can update the status of their assigned tasks via a dropdown.

### Live Dashboard & Stats
- **Real‑time Task Summary** – Displays total, completed, pending, and overdue tasks.
- **REST API** – Dashboard data is fetched via `/api/dashboard`.
- **Recent Tasks** – Shows the latest tasks assigned to the logged‑in user.

### Cloudinary Image Storage
- **Profile & Contact Images** – Uploaded to Cloudinary, served via CDN.
- **Automatic Cleanup** – Old images are deleted when updated.

### Dark / Light Mode
- **Persistent Theme** – User preference saved in local storage.
- **System Preference Detection** – Respects the OS theme on first visit.

### Export to Excel
- **One‑Click Export** – Export contact list to `.xlsx` using `TableToExcel`.

---

## Tech Stack

| Category       | Technology |
|----------------|------------|
| **Backend**    | Spring Boot 3.2, Spring Security, Spring Data JPA |
| **Frontend**   | Thymeleaf, Tailwind CSS, Flowbite, Font Awesome |
| **Database**   | MySQL 8+ |
| **Image Host** | Cloudinary |
| **Email**      | JavaMailSender (SMTP) |
| **OAuth2**     | Google, GitHub |
| **Build Tool** | Maven |
| **Deployment** | Railway / Docker |

---

## Test Credentials (local)

| Role   | Email               | Password |
|--------|---------------------|----------|
| Admin  | `admin@gmail.com`   | `admin`  |
| Member | `member@example.com`| `member` |

>  For local testing only – change passwords in production.

---

##  Getting Started

### Prerequisites
- Java 21
- MySQL 8
- Maven (or use the wrapper)
- Cloudinary account
- (Optional) Google/GitHub OAuth credentials
- SMTP server (e.g., Gmail)

### Environment Variables (production)

| Variable | Description |
|----------|-------------|
| `MYSQL_HOST` | Database host |
| `MYSQL_DB` | Database name |
| `MYSQL_PASSWORD` | Database password |
| `BASE_URL` | Public application URL |
| `GOOGLE_CLIENT_ID` | Google OAuth Client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth secret |
| `GITHUB_CLIENT_ID` | GitHub OAuth Client ID |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth secret |
| `CLOUDINARY_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `EMAIL_HOST` | SMTP host (e.g., `smtp.gmail.com`) |
| `EMAIL_USERNAME` | SMTP username |
| `EMAIL_PASSWORD` | SMTP app password |
| `EMAIL_DOMAIN` | "From" email address |

### Build & Run Locally

```bash
git clone https://github.com/kilash12/Team-Task-Manager.git
cd Team-Task-Manager
./mvnw clean package -DskipTests
java -jar target/team-task-manager-*.jar
