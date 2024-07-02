# Database Normalization

Normalization is the process of organizing data in a database to reduce redundancy and improve data integrity. The aim is to divide large tables into smaller, more manageable pieces while ensuring that relationships between the data are maintained. There are several normal forms (NF) used to achieve normalization, with the first three being the most commonly used: 1NF, 2NF, and 3NF.

## First Normal Form (1NF)

### Definition

1NF is the most basic level of database normalization. A table is in 1NF if:

- Each column contains atomic (indivisible) values.
- Each entry in a column is of the same data type.
- Each record (row) is unique.

### Example

Consider a table where a single column contains multiple values, such as multiple phone numbers in one cell:

| CustomerID | Name   | Phone Numbers          |
|------------|--------|------------------------|
| 1          | Ann  | 123-4567, 234-5678     |
| 2          | Nite    | 345-6789, 456-7890     |

This table is not in 1NF because the `Phone Numbers` column contains multiple values in a single cell.

**1NF Conversion:**

To convert this table to 1NF, we need to split the phone numbers into separate rows:

| CustomerID | Name   | Phone Number |
|------------|--------|--------------|
| 1          | Ann  | 123-4567     |
| 1          | nn  | 234-5678     |
| 2          | Nite    | 345-6789     |
| 2          | Nite    | 456-7890     |

Now, each column contains only atomic values, and the table is in 1NF.

## Second Normal Form (2NF)

### Definition

A table is in 2NF if:

- It is in 1NF.
- All non-key attributes are fully functionally dependent on the entire primary key. This means no partial dependency of any column on the primary key is allowed.

### Example

Consider a table where each project an employee works on is listed, along with the department they belong to:

| EmployeeID | Project  | Department |
|------------|----------|------------|
| 1          | Alpha    | HR         |
| 2          | Beta     | Finance    |
| 1          | Gamma    | HR         |
| 3          | Alpha    | IT         |

In this table, `Department` depends only on `EmployeeID`, not on the combination of `EmployeeID` and `Project`. This violates the 2NF rules because there is a partial dependency.

**2NF Conversion:**

To convert to 2NF, we split the data into two tables: one for employee projects and another for employee departments.

**Employee Project Table:**

| EmployeeID | Project |
|------------|---------|
| 1          | Alpha   |
| 1          | Gamma   |
| 2          | Beta    |
| 3          | Alpha   |

**Department Table:**

| EmployeeID | Department |
|------------|------------|
| 1          | HR         |
| 2          | Finance    |
| 3          | IT         |

This removes the partial dependency, placing the table in 2NF.

## Third Normal Form (3NF)

### Definition

A table is in 3NF if:

- It is in 2NF.
- All the attributes are functionally dependent only on the primary key and not on any other non-key attributes (no transitive dependency).

### Example

Consider a table where the manager of each department is listed alongside the employee:

| EmployeeID | Department | Manager    |
|------------|------------|------------|
| 1          | HR         | John       |
| 2          | Finance    | Emma       |
| 3          | IT         | Sarah      |

In this table, `Manager` is dependent on `Department`, which in turn is dependent on `EmployeeID`. This creates a transitive dependency.

**3NF Conversion:**

To convert to 3NF, create a separate table for departments and their managers.

**Employee Table:**

| EmployeeID | Department |
|------------|------------|
| 1          | HR         |
| 2          | Finance    |
| 3          | IT         |

**Department Table:**

| Department | Manager    |
|------------|------------|
| HR         | John       |
| Finance    | Emma       |
| IT         | Sarah      |

By eliminating transitive dependencies, the table is now in 3NF.

## Summary of Normal Forms

| Normal Form | Requirements                                                   | Example Situation                                  | Conversion Step                                      |
|-------------|-----------------------------------------------------------------|---------------------------------------------------|-----------------------------------------------------|
| 1NF         | No repeating groups or arrays; each field contains atomic values | Multiple phone numbers in one field              | Separate each value into individual rows            |
| 2NF         | No partial dependencies; non-key attributes depend on the whole primary key | Employee department depending only on EmployeeID | Separate table into employee projects and departments |
| 3NF         | No transitive dependencies; non-key attributes depend only on the primary key | Manager depends on Department, not EmployeeID     | Create separate table for departments and managers  |

