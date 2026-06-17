# onakawash-backend

Language: [English](./README.md) | [中文](./README.zh-CN.md)

## Project Introduction

onakawash-backend is the backend service for **onakawash**, a Japanese kana learning website for beginners.

This backend is built with **Java Spring Boot**. It currently provides kana data to the frontend and uses an H2 database for local development.

The project is still under development.

## Current Features

* Spring Boot backend project
* REST API for kana data
* Controller / Service / Repository layered structure
* H2 database for local development
* `data.sql` used as seed data
* Hiragana and Katakana data stored in the database
* Frontend connection support through CORS

## Current Data Flow

```text
Frontend → Controller → Service → Repository → Database
```

The frontend sends requests to the backend.

The backend reads kana data from the database and returns it to the frontend as JSON.

Current kana endpoints:

```text
GET /hiragana  -> reads HIRAGANA records from kana_items
GET /katakana  -> reads KATAKANA records from kana_items
```

Both endpoints return section-based JSON data for the frontend pages.

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Maven

## Database Note

The project currently uses **H2** as a local development database.

H2 is used for learning, testing, and local development. It is not planned as the final production database.

Current seed data location:

```text
src/main/resources/data.sql
```

The kana data in `data.sql` is the current main data source.

Older hardcoded kana data in the frontend, controller, or service layer should be treated as legacy data and should not be used as the main data source anymore.

Kana content, audio paths, image paths, section groups, and display order should be updated in `data.sql`.

In the future, the project may switch to a production database such as:

* PostgreSQL
* MySQL
* MariaDB

The expected future structure is not to use multiple databases, but to use one production database with multiple tables, such as:

```text
kana_items
example_sentences
users
study_records
```

## API Endpoints

Current or planned endpoints:

```text
GET /hiragana
GET /katakana
```

These endpoints return kana data grouped by section.

Example sections:

```text
Basic
Dakuten / Han-dakuten
Combination
```

## Local Development

This backend is part of a two-repository project.

Frontend repository:

```text
onakawash
```

Backend repository:

```text
onakawash-backend
```

During local development:

* Frontend runs on `localhost:3000`
* Backend runs on `localhost:8080`

## Future Plans

* Complete Hiragana and Katakana database records
* Add example sentence data
* Add user registration and login
* Add user learning records
* Switch to a production database in the future
* Prepare the project for deployment

## Local Development

The backend project is currently edited with **VS Code** and run with **PowerShell**.

IntelliJ IDEA is not used at the moment because it has been unstable in the current environment.

Current recommended workflow:

```text
VS Code       Edit Java / SQL / README files
PowerShell    Run the Spring Boot backend
PowerShell    Run the Next.js frontend
PowerShell    Run Git commands
```

## Run the Backend

Open PowerShell and enter the backend repository:

```powershell
cd D:\Code\onakawash-backend
```

Run Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

When the backend starts successfully, the console should show messages similar to:

```text
Tomcat started on port 8080
Started OnakawashBackendApplication
```

Example backend API:

```text
http://localhost:8080/hiragana
http://localhost:8080/katakana
```

Note: the PowerShell window running Spring Boot will be occupied by the backend process.
Open another PowerShell window if Git commands or other commands need to be executed.

To stop the backend:

```text
Ctrl + C
```

If a confirmation message appears, type:

```text
Y
```

Then press Enter.

## Run the Frontend

Open another PowerShell window and enter the frontend repository:

```powershell
cd D:\Code\onakawash
```

Run the frontend:

```powershell
npm run dev
```

Frontend page:

```text
http://localhost:3000
```

## Git Commands

It is recommended to use a separate PowerShell window for Git commands.

Enter the backend repository:

```powershell
cd D:\Code\onakawash-backend
```

Check current changes:

```powershell
git status
```

Example commit:

```powershell
git add .
git commit -m "feat: add hiragana database sections"
git push
```

## Development Notes

* Do not use the IDEA run button to start Spring Boot for now.
* If `data.sql` or Java code is changed, restart the backend to apply the changes.
* H2 is currently used as the local development database. Data is initialized from `data.sql` when Spring Boot starts.
* If `Port 8080 already in use` appears, the backend is already running. Stop the old backend process first.
* If an `hs_err_pid*.log` file appears, it is a Java / JVM crash log and should not be committed to Git.


## Author

Ma Yiyuan (Yvonne Buttercup)
