# KpopCruiseBooking

KpopCruiseBooking is a Spring Boot web application for browsing cruises and making cruise reservations.  
The project was built using **Java**, **Spring Boot**, **Thymeleaf**, and **MySQL**.

## Features

- View available cruises
- Register and log in
- Create a cruise reservation
- Edit reservation details
- View booking confirmation
- Profile page
- Payment page

## Technologies Used

- Java
- Spring Boot
- Spring MVC
- Thymeleaf
- MySQL
- HTML
- CSS

## Project Structure

- `controller` - Handles page requests and application flow
- `model` - Contains entity classes such as Booking, Cruise, and Customer
- `repository` - Handles database access
- `config` - Contains configuration and data seeding
- `templates` - Thymeleaf HTML pages
- `static` - CSS and image files

## How to Run the Project

1. Clone this repository
2. Open the project in IntelliJ IDEA or another Java IDE
3. Create a MySQL database named:

```sql
CREATE DATABASE CruiseReservation;
