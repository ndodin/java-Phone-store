# Database Setup - Phone Store

## Step 1: Create Database

Open SQL Server and run:

CREATE DATABASE CHDienThoai;

---

## Step 2: Run Scripts (in order)

1. Run: database/01_schema.sql
2. Run: database/02_fake_data.sql
3. Run: database/03_update.sql

---

## Notes

* Do NOT run all files at once
* Run in order (1 → 2 → 3)
* If error occurs:

  * Check database name
  * Ensure no duplicate data

---

## Default Accounts

Admin:

* username: admin
* password: 123

Staff:

* username: staff
* password: 123

---

## Purpose

* Provide initial data for testing:

  * Login
  * Invoice
  * Invoice History
