# Online School Test System

## Overview
The Online School Test System designed to enhance the process of conducting tests within an educational institution. It fits for the needs of students, teachers, and managers by providing specific functionalities to each role.

## Features
### Role-based Access Control:
Students, teachers, and managers each have distinct roles with specific permissions.
### Student Functionality:
Take tests online.
View test results.
### Teacher Functionality:
Create questions.
Create tests.
View statistical information.
request additional time for the test.
Review and adjust test grades.
### Manager Functionality:
View statistical information.
Approve additional time requests.
### Online and Offline Testing:
Tests can be taken online within the system.
Option to download tests as Word files for offline completion, with submission functionality.
### Automatic Test Checks:
Automatic test checks upon teacher's approval.
### Grade Adjustment:
Teachers can modify test grades as necessary.

## Structure
Pay attention to the three modules:
1. **client** - a simple client built using JavaFX and OCSF. We use EventBus (which implements the mediator pattern) in order to pass events between classes (in this case: between SimpleClient and PrimaryController.
2. **server** - a simple server built using OCSF.
3. **entities** - a shared module where all the entities of the project live.

## Running
1. Run Maven install **in the parent project**.
2. Run the server using the exec:java goal in the server module.
3. Run the client using the javafx:run goal in the client module.
4. port : 3028 , host : (wifi ip address)
5. Press the button and see what happens!
