package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.model.Booking;
import com.ernesse.kpopcruisebooking.model.Customer;
import com.ernesse.kpopcruisebooking.repository.BookingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    private final BookingRepository bookingRepository;

    public PaymentController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("bookingId") Long bookingId,
                                  Model model,
                                  HttpSession session) {

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
        return "payment";
    }

    @PostMapping("/payment")
    public String processPayment(@RequestParam("bookingId") Long bookingId,
                                 @RequestParam("paymentMethod") String paymentMethod,
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

        booking.setPaymentMethod(paymentMethod);
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        return "redirect:/confirmation?bookingId=" + booking.getBookingId();
    }
}