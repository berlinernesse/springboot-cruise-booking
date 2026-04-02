package com.ernesse.kpopcruisebooking.repository;

import com.ernesse.kpopcruisebooking.model.Booking;
import com.ernesse.kpopcruisebooking.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(Customer customer);
}