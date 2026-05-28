# Course Review App

A JavaFX desktop application that allows students to search for courses and read or submit peer reviews. Built with a three-layer architecture (UI, logic, and data) and backed by a SQLite database. This project was completed as an assignment for CS 3140 (Software Development Essentials) in Fall 2025 at the University of Virginia.

## Tech Stack

- **Java / JavaFX** — UI and application logic
- **SQLite** — local persistent storage
- **Gradle** — build and dependency management
- **JUnit Jupiter / Mockito** — unit and mock testing

## Architecture

The app follows a three-layer design:

- **UI Layer** — JavaFX controllers for Login/Register, Course Search, Course Reviews, and My Reviews scenes
- **Logic Layer** — `UserService`, `CourseService`, and `ReviewService` handle business rules
- **Data Layer** — `User`, `Course`, and `Review` models with corresponding unit tests

## Getting Started

**Prerequisites:** Java 17 or higher

```bash
# macOS / Linux
./gradlew run

# Windows
gradlew.bat run
```

The SQLite database comes pre-populated with sample courses and reviews.

## My Contributions

I was responsible for the entire **Logic Layer**:

- `UserService.java` — user authentication and account management
- `CourseService.java` — course lookup and data access logic
- `ReviewService.java` — review creation, retrieval, and validation
