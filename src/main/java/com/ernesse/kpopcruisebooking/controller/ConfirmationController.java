package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.model.Booking;
import com.ernesse.kpopcruisebooking.model.Customer;
import com.ernesse.kpopcruisebooking.repository.BookingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfirmationController {

    private final BookingRepository bookingRepository;

    public ConfirmationController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(@RequestParam("bookingId") Long bookingId,
                                       HttpSession session,
                                       Model model) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return "redirect:/reservation";
        }

        if (!booking.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
            return "redirect:/reservation";
        }

        model.addAttribute("booking", booking);
        return "confirmation";
    }
}