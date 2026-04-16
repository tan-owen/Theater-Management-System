# Theater Management System

A console-based Customer Relationship Management (CRM) system for a theater company, built in Java. It supports three user roles — **Customer**, **Support Staff**, and **Manager** — and manages the full lifecycle of support tickets through CSV-backed persistence.

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or later
- Windows (the build script is a `.bat` file)

### Running the Application

Double-click `run.bat`, or run it from a terminal:

```bat
.\run.bat
```

The script will:
1. Compile all `.java` source files into the `build/` directory
2. Launch the application from the project root so all `data/` file paths resolve correctly

---

## Project Structure

```
Theater-Management-System/
├── main.java                   # Entry point (~5 lines)
├── run.bat                     # Build & run script
│
├── boundary/                   # UI layer — menus and user interaction
│   ├── StartMenu.java          # Login / Register / Exit
│   ├── CustomerMode.java       # Customer dashboard
│   ├── SupportStaffMode.java   # Support staff dashboard
│   └── ManagerMode.java        # Manager dashboard
│
├── control/                    # Business logic handlers
│   ├── LoginRegistrationHandler.java
│   ├── TicketSubmissionHandler.java
│   ├── TicketHandler.java              # Shared ticket operations (view, comment, close)
│   ├── MyTicketsHandler.java
│   ├── StaffTicketViewHandler.java
│   ├── ManagerTicketViewHandler.java
│   ├── TicketAssignmentHandler.java
│   ├── DiscussionHandler.java
│   ├── InteractionHistoryHandler.java
│   ├── StaffInteractionHistoryHandler.java
│   └── PerformanceReportHandler.java
│
├── entity/                     # Domain model
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
│   └── Discussable.java        # Interface for discussion thread
│
├── doa/                        # Data Object Access — CSV read/write
│   ├── UserFileLoader.java
│   ├── TicketFileLoader.java
│   └── DiscussionFileLoader.java
│
├── utility/
│   ├── ConsoleUtil.java        # Screen clearing, formatting helpers
│   └── PasswordHasher.java     # Salt + hash for secure password storage
│
├── data/                       # Flat-file persistence (CSV)
│   ├── accounts.csv            # Hashed user accounts
│   ├── accounts_unhashed.csv   # Plain-text reference (development only)
│   ├── tickets.csv             # All tickets
│   ├── discussion.csv          # Discussion thread comments
│   └── interaction_log.csv     # Audit log of all interactions
│
└── build/                      # Compiled .class files (generated)
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
