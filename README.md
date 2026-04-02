Finance Dashboard Backend
Overview

This is a Finance Dashboard Backend project developed using Spring Boot, MySQL, and Spring Security with JWT Authentication.
It provides user management, financial records management, and dashboard summaries, along with filtering, pagination, and security features.

The API is documented with Swagger for easy testing and integration.

Completed Functionalities
1. User Management
   Create User (POST /api/users)
   Add new users with details like username, password, and role.
   Get All Users (GET /api/users)
   Fetch the list of all users.
   Update User Status (PUT /api/users/{id}/status)
   Change user status (ACTIVE / INACTIVE).
   Duplicate Email Prevention
   User email must be unique; duplicate entries are rejected.
2. Authentication
   JWT-based Authentication (POST /api/auth/login)
   Replaced basic authentication with JWT tokens.
   Tokens expire based on configured JWT settings.
   All endpoints are secured except /auth/login.
3. Financial Records Management
   Create Record (POST /api/records)
   Add a financial record with fields:
   amount, type (INCOME / EXPENSE), category, date, description, createdBy.
   Get All Records (GET /api/records)
   Retrieve all active records.
   Update Record (PUT /api/records/{id})
   Update existing records by ID.
   Soft Delete Record (DELETE /api/records/soft/{id})
   Records are marked inactive, not permanently deleted.
   Hard Delete Record (DELETE /api/records/{id})
   Permanently delete a record (optional).
4. Filtering and Search
   Filter by Category (GET /api/records/filter/category)
   Filter by Type (GET /api/records/filter/type)
   Fetch records by INCOME or EXPENSE.
   Filter by Date (GET /api/records/filter/date)
   Fetch records within a specific date range.

Advanced Filter (GET /api/records/filter)
Combine category + type + date range.
Example:

/api/records/filter?category=Salary&type=INCOME&start=2026-01-01&end=2026-03-31
5. Pagination & Sorting
   Paginated Records (GET /api/records?page=0&size=10&sort=date,desc)
   Supports page number, page size, and sorting (ascending/descending).
   Only active records are returned.
6. Dashboard / Summary
   Dashboard Summary (GET /api/dashboard/summary?recent=5)
   Provides:
   Total Income, Total Expenses, Net Balance
   Category-wise totals
   Recent transactions (default 5)
   Weekly and monthly income/expense summaries
7. Data Integrity & Validation
   Transaction Type Validation
   Only valid types (INCOME / EXPENSE) are allowed.
   Soft Delete Handling
   All filters, listings, and dashboard calculations only include active = true records.
8. Exceptions / Error Handling
   Resource Not Found → Throws ResourceNotFoundException when a record/user is missing.
   Bad Request → Throws BadRequestException for invalid transaction types or invalid inputs.
   📌 Tech Stack
   Spring Boot 3.x / 4.x
   Spring Data JPA / Hibernate
   MySQL
   Spring Security with JWT
   Swagger / OpenAPI
   Lombok
 
9. Notes / Future Improvements
JWT expiration and refresh token handling
Role-based authorization refinement
Soft delete recovery for records
Export records to CSV / PDF
Integration with frontend dashboard