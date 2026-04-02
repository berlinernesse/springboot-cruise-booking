package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.model.Booking;
import com.ernesse.kpopcruisebooking.model.Cruise;
import com.ernesse.kpopcruisebooking.model.Customer;
import com.ernesse.kpopcruisebooking.repository.BookingRepository;
import com.ernesse.kpopcruisebooking.repository.CruiseRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class ReservationController {

    private final BookingRepository bookingRepository;
    private final CruiseRepository cruiseRepository;

    public ReservationController(BookingRepository bookingRepository, CruiseRepository cruiseRepository) {
        this.bookingRepository = bookingRepository;
        this.cruiseRepository = cruiseRepository;
    }

    @GetMapping("/reservation")
    public String showReservationPage(Model model,
                                      HttpSession session,
                                      @RequestParam(value = "message", required = false) String message,
                                      @RequestParam(value = "error", required = false) String error) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingRepository.findByCustomer(loggedInCustomer);

        model.addAttribute("bookings", bookings);
        model.addAttribute("customer", loggedInCustomer);
        model.addAttribute("message", message);
        model.addAttribute("error", error);

        return "reservation";
    }

    @GetMapping("/reservation/new")
    public String showReservationForm(@RequestParam("cruiseId") Long cruiseId,
                                      Model model,
                                      HttpSession session) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Cruise cruise = cruiseRepository.findById(cruiseId).orElse(null);

        if (cruise == null) {
            return "redirect:/cruises";
        }

        Booking booking = new Booking();
        booking.setCruise(cruise);

        model.addAttribute("cruise", cruise);
        model.addAttribute("booking", booking);

        return "reservation-form";
    }

    @PostMapping("/reservation/new")
    public String saveReservation(@Valid @ModelAttribute("booking") Booking booking,
                                  BindingResult result,
                                  @RequestParam("cruiseId") Long cruiseId,
                                  HttpSession session,
                                  Model model) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Cruise cruise = cruiseRepository.findById(cruiseId).orElse(null);

        if (cruise == null) {
            return "redirect:/cruises";
        }

        if (result.hasErrors()) {
            model.addAttribute("cruise", cruise);
            return "reservation-form";
        }

        booking.setCustomer(loggedInCustomer);
        booking.setCruise(cruise);
        booking.setBookingDate(LocalDate.now());
        booking.setDepartureDate(cruise.getDepartureTime().toLocalDate());

        recalculateBooking(booking, cruise);

        booking.setStatus("PENDING_PAYMENT");

        bookingRepository.save(booking);

        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }

    @GetMapping("/reservation/edit")
    public String showEditReservationForm(@RequestParam("bookingId") Long bookingId,
                                          HttpSession session,
                                          Model model) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return "redirect:/reservation?error=Reservation not found";
        }

        if (!booking.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
            return "redirect:/reservation?error=Unauthorized access";
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            return "redirect:/reservation?error=Cancelled reservations cannot be edited";
        }

        model.addAttribute("booking", booking);
        model.addAttribute("cruise", booking.getCruise());

        return "reservation-edit";
    }

    @PostMapping("/reservation/edit")
    public String updateReservation(@RequestParam("bookingId") Long bookingId,
                                    @RequestParam("cabinType") String cabinType,
                                    @RequestParam("adults") int adults,
                                    @RequestParam("children") int children,
                                    HttpSession session,
                                    Model model) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Booking existingBooking = bookingRepository.findById(bookingId).orElse(null);

        if (existingBooking == null) {
            return "redirect:/reservation?error=Reservation not found";
        }

        if (!existingBooking.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
            return "redirect:/reservation?error=Unauthorized access";
        }

        if ("CANCELLED".equalsIgnoreCase(existingBooking.getStatus())) {
            return "redirect:/reservation?error=Cancelled reservations cannot be edited";
        }

        if (adults < 1 || adults > 10 || children < 0 || children > 10) {
            model.addAttribute("booking", existingBooking);
            model.addAttribute("cruise", existingBooking.getCruise());
            model.addAttribute("error", "Please enter valid passenger counts.");
            return "reservation-edit";
        }

        if (!(cabinType.equals("Interior") ||
                cabinType.equals("Ocean View") ||
                cabinType.equals("Balcony") ||
                cabinType.equals("Suite"))) {
            model.addAttribute("booking", existingBooking);
            model.addAttribute("cruise", existingBooking.getCruise());
            model.addAttribute("error", "Please select a valid cabin type.");
            return "reservation-edit";
        }

        existingBooking.setCabinType(cabinType);
        existingBooking.setAdults(adults);
        existingBooking.setChildren(children);

        recalculateBooking(existingBooking, existingBooking.getCruise());

        bookingRepository.save(existingBooking);

        return "redirect:/reservation?message=Reservation updated successfully";
    }

    @GetMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam("bookingId") Long bookingId,
                                    HttpSession session) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return "redirect:/reservation?error=Reservation not found";
        }

        if (!booking.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId())) {
            return "redirect:/reservation?error=Unauthorized access";
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            return "redirect:/reservation?error=Reservation is already cancelled";
        }

        long daysUntilDeparture = ChronoUnit.DAYS.between(LocalDate.now(), booking.getDepartureDate());

        if (daysUntilDeparture < 10) {
            return "redirect:/reservation?error=Reservations can only be cancelled 10 or more days before departure";
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return "redirect:/reservation?message=Reservation cancelled successfully";
    }

    private void recalculateBooking(Booking booking, Cruise cruise) {
        int totalPassengers = booking.getAdults() + booking.getChildren();
        booking.setNoOfPassengers(totalPassengers);

        double cabinMultiplier = 1.0;

        if ("Ocean View".equalsIgnoreCase(booking.getCabinType())) {
            cabinMultiplier = 1.15;
        } else if ("Balcony".equalsIgnoreCase(booking.getCabinType())) {
            cabinMultiplier = 1.30;
        } else if ("Suite".equalsIgnoreCase(booking.getCabinType())) {
            cabinMultiplier = 1.55;
        }

        double adjustedBasePrice = cruise.getPrice() * cabinMultiplier;

        double adultTotal = adjustedBasePrice * booking.getAdults();
        double childTotal = (adjustedBasePrice * 0.60) * booking.getChildren();

        double totalPrice = adultTotal + childTotal;
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;

        booking.setTotalPrice(totalPrice);
    }
}