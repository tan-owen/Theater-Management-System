# Theater Management System

A console-based Customer Relationship Management (CRM) system for a theater company, built in Java. It supports three user roles — **Customer**, **Support Staff**, and **Manager** — and manages the full lifecycle of support tickets through CSV-backed persistence.

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or later

### Running the Application

Double-click `TheaterCRM.bat`, or run it from a terminal:

The .bat file will:
1. Compile all `.java` source files into the `bin/` directory
2. Launch the application from the project root so all `data/` file paths resolve correctly

---

## Project Structure

```
Theater-Management-System/
├── main.java                   # Entry point
├── TheaterCRM.bat              # Build & run program
│
├── boundary/                   # CLI layer — menus and user interaction
│   ├── StartMenu.java          # Login / Register / Exit
│   ├── CustomerMode.java       # Customer dashboard
│   ├── SupportStaffMode.java   # Support staff dashboard
│   └── ManagerMode.java        # Manager dashboard
│
├── control/                    # Business logic handlers
│   ├── LoginRegistrationHandler.java
│   ├── TicketSubmissionHandler.java
│   ├── TicketHandler.java              
│   ├── MyTicketsHandler.java
│   ├── StaffTicketViewHandler.java
│   ├── ManagerTicketViewHandler.java
│   ├── TicketAssignmentHandler.java
│   ├── DiscussionHandler.java
│   ├── InteractionHistoryHandler.java
│   ├── StaffInteractionHistoryHandler.java
│   └── PerformanceReportHandler.java
│
├── entity/                     # Class entities
│   ├── User.java
│   ├── Customer.java
│   ├── SupportStaff.java
│   ├── Manager.java
│   ├── Ticket.java
│   ├── RefundTicket.java
│   ├── TechnicalDifficultyTicket.java
│   ├── ChangeRequestTicket.java
│   ├── ProblemTicket.java
│   ├── OtherTicket.java
│   ├── Comment.java
│   ├── InteractionLog.java
│   └── Discussable.java       
│
├── doa/                        # CSV read & write
│   ├── UserFileLoader.java
│   ├── TicketFileLoader.java
│   └── DiscussionFileLoader.java
│
├── utility/                    # Utilities
│   ├── ConsoleUtil.java        # Clear screen, formatting helpers
│   └── PasswordHasher.java     # Salt + hash for secure password storage
│
├── data/                       # Flat-file persistence (CSV)
│   ├── accounts_unhashed.csv   # Plain-text reference (development only)
│   ├── accounts.csv            # Hashed user accounts
│   ├── discussion.csv          # Discussion thread comments
│   ├── tickets.csv             # All tickets
│   └── interaction_log.csv     # Audit log of all interactions
│
└── bin/                      # precompiled .class files
```

---

## File Structure

| Layer | Package | Responsibility |
|---|---|---|
| **Boundary** | `boundary` | Console menus, user input/output |
| **Control** | `control` | Business logic, orchestration |
| **Entity** | `entity` | Domain objects (no persistence logic) |
| **DOA** | `doa` | CSV file reading and writing |
| **Utility** | `utility` | Shared helpers (passwords, console) |

---

## Features

### Customer
- **Register / Login** with salted password hashing
- **Submit tickets** — Refund, Technical Difficulty, Change Request, Problem, or Other
- **View own tickets** — status, details, and discussion thread
- **Join ticket discussions** — add comments to any of their tickets

### Support Staff
- **View assigned tickets** — manage tickets assigned to them
- **Provide resolution steps** — enter and persist steps for Problem Tickets
- **Add comments** — contribute to the discussion thread
- **Close tickets** — marks status as `CLOSED` and logs the action
- **View all tickets** — filtered by type (read-only for unassigned tickets)
- **View interaction history** — chronological log of own actions

### Manager
- **Assign tickets** to support staff
- **View all tickets** — full visibility across all types and statuses
- **View staff performance** — per-staff resolution rate (closed / assigned)
- **Generate monthly report** — overall resolution rate and ticket breakdown by priority
- **View staff interaction history** — select any staff member to see their audit log

---

## Ticket Types

| Prefix | Type | Extra Fields |
|---|---|---|
| `RF` | Refund Ticket | Transaction ID, Refund Amount |
| `TD` | Technical Difficulty Ticket | Device Type |
| `CR` | Change Request Ticket | Movie Ticket ID |
| `PR` | Problem Ticket | Resolution Steps |
| `TK` | Other / General Inquiry | — |

---

## Data Persistence

All data is stored as tab-delimited CSV files in the `data/` directory. No database is required.

### `tickets.csv` column layout

```
ticketID  title  description  createdAt  customerID  staffID  priority  status  [type-specific fields...]
```

Ticket status (`OPEN` / `CLOSED`) is persisted and restored on every load — closed tickets remain closed across restarts.

### `interaction_log.csv` column layout

```
ticketID  timestamp  userID  actionDetail
```

Every significant action (ticket creation, assignment, comment, resolution steps, closure) is appended here as an immutable audit trail.

---

## Security

Passwords are never stored in plain text in `accounts.csv`. The `PasswordHasher` utility generates a random salt per user and stores `SHA-256(password + salt)` alongside the salt. `accounts_unhashed.csv` exists for development reference only and should not be included in production deployments.
