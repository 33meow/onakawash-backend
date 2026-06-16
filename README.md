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
* Hiragana data stored in the database
* Frontend connection support through CORS

## Current Data Flow

```text
Frontend → Controller → Service → Repository → Database
```

The frontend sends requests to the backend.

The backend reads kana data from the database and returns it to the frontend as JSON.

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

## Author

Ma Yiyuan (Yvonne Buttercup)
