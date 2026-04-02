# University CMS

Spring Boot web application for university management. It allows you to manage schedules, users, courses, groups, rooms, and subjects. 
Role-based access—each role sees only the information relevant to it.

## How it works

Users log in and are redirected based on their role. Admins manage users, courses, and groups. 
Staff handle lesson scheduling and rooms. 
Lecturers and students view their own schedules and profiles. 
All data is persisted in PostgreSQL with schema managed by Flyway.

You can refer to [user_stories.md](user_stories.md)for usage examples.

## Tech Stack

Java 17, Spring Boot 3.4.1
Spring Security (BCrypt, CSRF, method-level authorization)
Spring Data JPA + JPA Specifications for dynamic filtering
PostgreSQL + Flyway
Thymeleaf, Bootstrap 5 (Bootswatch Cerulean)
MapStruct, Lombok
Testcontainers (integration tests)

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL running on `localhost:5436`

## Setup & Run

1. Clone the repository
```
git clone <repo-url>
cd university-cms11
```

2. Create the database
```sql
CREATE DATABASE "uni-cms";
```

3. Run
```
mvn spring-boot:run
```

App starts on http://localhost:8081

## Pages

| Path | Access |
|------|--------|
| `/login` | Public |
| `/` | All authenticated |
| `/students` | Student |
| `/students/profile` | Student |
| `/lecturers` | Lecturer |
| `/lecturers/profile` | Lecturer |
| `/users` | Staff |
| `/users/rooms` | Staff |
| `/users/courses` | Staff |
| `/users/update-lecturer-subjects` | Staff |
| `/lessons/lesson-setup` | Staff |
| `/lessons/create-lesson` | Staff |
| `/admin` | Admin |
| `/admin/users` | Admin |
| `/admin/create-user` | Admin |
| `/admin/create-course` | Admin |
| `/admin/update-course-groups` | Admin |

## Roles

- **Admin** — manage users (create, delete, change role), manage courses and group assignments
- **Staff** — manage lessons, rooms, and lecturer subject assignments
- **Lecturer** — view personal schedule and profile
- **Student** — view personal schedule and profile