# Enterprise Console-Based Insurance Management System (ECIMS)

## Overview

The **Enterprise Console-Based Insurance Management System (ECIMS)** is a Java console application designed to simulate the core operations of an insurance company. The project focuses on applying Object-Oriented Programming (OOP) principles, clean code practices, and enterprise software development concepts while providing a practical understanding of real-world insurance workflows.

This project was developed as a learning exercise to strengthen skills in Java, software engineering, data structures, exception handling, file management, logging, and enterprise application design.

---

## Project Objectives

The primary objectives of this project are to:

* Practice Object-Oriented Programming.
* Build a modular console application.
* Implement real-world business logic.
* Develop clean and maintainable code.
* Simulate enterprise insurance operations.
* Prepare for backend enterprise application development.

---

## Features

### Authentication

* User login
* Password management
* Role-based access
* Login history

### Customer Management

* Register customer
* Update customer
* Search customer
* Delete customer
* Display all customers

### Policy Management

* Issue policy
* Renew policy
* Cancel policy
* Suspend policy
* View policy details

### Premium Payments

* Record premium payments
* Generate receipts
* View payment history
* Track outstanding balances

### Claims Management

* Lodge claims
* Approve claims
* Reject claims
* Track claim status

### Reports

* Active policies
* Expired policies
* Outstanding premiums
* Pending claims
* Registered customers
* Daily transactions

### Logging

* Audit logs
* Error logs
* Transaction history

---

## Technologies Used

* Java
* Java Collections Framework
* File I/O
* Exception Handling
* Java Time API
* Object-Oriented Programming
* Git and GitHub

---

## Project Structure

```
src/

├── model/
├── service/
├── repository/
├── exception/
├── utility/
├── menu/
├── report/
├── log/
└── Main.java
```

---

## Object-Oriented Design

The project demonstrates:

### Encapsulation

* Private fields
* Getters and setters

### Inheritance

```
Person
│
├── Customer
│
└── Employee
    ├── Agent
    ├── Administrator
    ├── ClaimsOfficer
    └── FinanceOfficer
```

### Abstraction

Abstract base classes for shared functionality.

### Polymorphism

Method overriding and overloading.

### Interfaces

Examples:

* Payable
* Renewable

---

## Main Menu

```
==================================

ENTERPRISE INSURANCE SYSTEM

==================================

1. Login
2. Customer Management
3. Policy Management
4. Premium Payments
5. Claims
6. Reports
7. Administration
8. Exit

==================================
```

---

## Data Management

### Phase I

Store data using Java Collections.

### Phase II

Persist data using files.

### Phase III

Integrate with a relational database using JDBC.

---

## Logging System

Every system activity should be recorded.

Example:

```
2026-06-09 14:30

User: Agent001

Action: Policy Renewal

Policy: LIFE001

Status: SUCCESS
```

Error example:

```
Policy Renewal Failed

Reason:
Outstanding Premium Balance
```

---

## Exception Handling

Custom exceptions include:

* CustomerNotFoundException
* PolicyExpiredException
* PaymentException
* ClaimRejectedException

---

## Clean Code Principles

This project follows the following guidelines:

### Meaningful Names

Good:

```
customerName
policyNumber
premiumAmount
```

Avoid:

```
a
temp
x
```

### Single Responsibility Principle

Each class should have one responsibility.

Each method should perform one task.

### Small Functions

Target:

* 20 lines

Maximum:

* 40 lines

### Modular Design

Separate:

* Models
* Services
* Utilities
* Repositories
* Reports
* Exceptions

---

## Git Workflow

Recommended commit sequence:

```
Initial Commit

Authentication Module

Customer Module

Policy Module

Payment Module

Claims Module

Reports Module

Logging Module

Refactoring

Final Release
```

---

## Testing

The application should be tested against:

### Valid Input

* Customer registration
* Policy issuance
* Premium payment

### Invalid Input

* Duplicate customers
* Invalid policies
* Invalid payments

### Edge Cases

* Expired policies
* Missing customers
* Outstanding balances

---

## Learning Outcomes

By completing this project, the developer should gain experience in:

### Java

* OOP
* Collections
* File handling
* Exception handling
* Date and Time API
* Interfaces
* Abstract classes

### Software Engineering

* Clean code
* Modular architecture
* Logging
* Documentation
* Refactoring
* Testing

### Enterprise Development

* Authentication
* Business rules
* Transaction processing
* Audit trails
* Report generation
* Role-based access

---

## Future Improvements

Potential enhancements include:

* JDBC database integration
* Password encryption
* Search optimization
* Automatic policy expiry
* Batch processing
* Multi-user support
* REST API integration
* Web interface
* Unit testing with JUnit
* Maven or Gradle build system

---

## Development Guidelines

While implementing this project:

* Write readable code.
* Keep methods focused.
* Handle exceptions properly.
* Commit changes frequently.
* Refactor when necessary.
* Document important decisions.
* Avoid code duplication.

Remember:

> "Code is read more often than it is written."

---

## Getting Started

Clone the repository:

```bash
git clone <repository-url>
```

Navigate to the project:

```bash
cd ECIMS
```

Compile:

```bash
javac Main.java
```

Run:

```bash
java Main
```

---

## Project Status

**Status:** In Development

This project is being developed incrementally as a practical exercise in enterprise Java application development.

---

## Author

**Wycliffe Odhiambo Omondi**

---

## License

This project is intended for educational and portfolio purposes.

---

## Final Goal

The goal of ECIMS is not simply to build a console application, but to develop the engineering habits required for enterprise software development:

* Think before coding.
* Design for maintainability.
* Write clean and testable code.
* Solve business problems systematically.
* Build software that another developer can understand and extend.
