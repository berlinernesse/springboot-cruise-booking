package com.ernesse.kpopcruisebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please enter a valid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Birth date is required")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotEmpty(message = "Address is required")
    @Size(min = 5, max = 120, message = "Address must be between 5 and 120 characters")
    @Column(nullable = false)
    private String address;

    @NotEmpty(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Column(nullable = false)
    private String city;

    @NotEmpty(message = "Postal code is required")
    @Pattern(
            regexp = "^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$",
            message = "Postal code must be in format A1A 1A1"
    )
    @Column(nullable = false)
    private String postalCode;

    public Customer() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}