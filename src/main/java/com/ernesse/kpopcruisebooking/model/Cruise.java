package com.ernesse.kpopcruisebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "cruises")
public class Cruise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cruiseId;

    @NotEmpty(message = "Cruise name is required")
    @Size(min = 3, max = 100, message = "Cruise name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String cruiseName;

    @NotEmpty(message = "Origin is required")
    @Size(min = 2, max = 100, message = "Origin must be between 2 and 100 characters")
    @Column(nullable = false)
    private String origin;

    @NotEmpty(message = "Destination is required")
    @Size(min = 2, max = 100, message = "Destination must be between 2 and 100 characters")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "Departure time is required")
    @Column(nullable = false)
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Min(value = 1, message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;

    @NotEmpty(message = "Tier is required")
    @Column(nullable = false)
    private String tier;

    public Cruise() {
    }

    public Long getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(Long cruiseId) {
        this.cruiseId = cruiseId;
    }

    public String getCruiseName() {
        return cruiseName;
    }

    public void setCruiseName(String cruiseName) {
        this.cruiseName = cruiseName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}