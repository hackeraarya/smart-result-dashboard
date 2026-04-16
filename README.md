# Smart Result Analysis and Performance Dashboard System

An interactive Spring Boot dashboard for student/faculty/admin result analytics, role-based access, JWT authentication, and downloadable reports.

## Key Features

- Role-based login and dashboard routing (Student, Faculty, Admin)
- JWT authentication with password hashing
- Interactive frontend dashboard with charts and performance summaries
- Subject-wise marks display with detailed assessment breakdowns
- Result and analysis pages with CIE / internal / midterm / final / practical marks
- Download-ready UI placeholders for PDF / Excel exports
- H2 console for fast local data inspection

## Technology Stack

- Java 17
- Spring Boot 3.4.4
- Spring Security, Spring Data JPA, Spring Validation
- Thymeleaf + static HTML frontend
- H2 in-memory database and MySQL runtime connector
- JWT authentication via `jjwt`
- Apache POI and OpenPDF for reporting

## Getting Started

### Prerequisites

- Java 17 SDK installed
- Maven or use the included Maven wrapper

### Run locally

```bash
cd backend
./mvnw.cmd spring-boot:run
```

Then open the application at `http://localhost:8080`.

### Build package

```bash
cd backend
./mvnw.cmd clean package
```

## Project Structure

- `src/main/java` — Spring Boot backend controllers, services, models, security, and repositories
- `src/main/resources/static/frontend` — static HTML dashboard pages and frontend JS
- `src/main/resources/templates` — Thymeleaf templates and shared layout fragments
- `pom.xml` — Maven dependencies and build configuration

## GitHub Repo Setup

This project is ready for GitHub upload.

After creating the remote repository on GitHub, run:

```bash
git remote add origin https://github.com/<your-username>/<repo-name>.git
git branch -M main
git push -u origin main
```

## Contributors

- @your-github-username
- @contributor-1
- @contributor-2

> Replace the contributor usernames above with actual GitHub handles.

## Notes

- The current repository contains demo data and frontend placeholders for reporting.
- If you want to add actual user data and results, update the `src/main/resources/static/frontend` pages and backend data sources.
