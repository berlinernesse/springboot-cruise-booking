package com.ernesse.kpopcruisebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Random;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_reference", nullable = false, unique = true, length = 6)
    private String bookingReference;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "cruise_id", nullable = false)
    private Cruise cruise;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @NotNull(message = "Please select a cabin type")
    @Pattern(
            regexp = "Interior|Ocean View|Balcony|Suite",
            message = "Cabin type must be Interior, Ocean View, Balcony, or Suite"
    )
    @Column(name = "cabin_type")
    private String cabinType;

    @Column(name = "number_of_passengers")
    private int noOfPassengers;

    @Min(value = 1, message = "At least 1 adult is required")
    @Max(value = 10, message = "Adults cannot exceed 10")
    @Column(name = "adults")
    private int adults;

    @Min(value = 0, message = "Children cannot be negative")
    @Max(value = 10, message = "Children cannot exceed 10")
    @Column(name = "children")
    private int children;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status")
    private String status;

    // NEW FIELD
    @Column(name = "payment_method")
    private String paymentMethod;

    public Booking() {
    }

    // Generate 6-digit booking reference automatically
    @PrePersist
    public void generateBookingReference() {
        if (this.bookingReference == null || this.bookingReference.isBlank()) {
            this.bookingReference = generateRandomReference();
        }
    }

    private String generateRandomReference() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder reference = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            reference.append(characters.charAt(index));
        }

        return reference.toString();
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Cruise getCruise() {
        return cruise;
    }

    public void setCruise(Cruise cruise) {
        this.cruise = cruise;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}