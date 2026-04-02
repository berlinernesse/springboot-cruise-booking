package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.model.Customer;
import com.ernesse.kpopcruisebooking.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@Controller
public class AuthController {

    private final CustomerRepository customerRepository;

    public AuthController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // SHOW REGISTER PAGE
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    // HANDLE REGISTER FORM
    @PostMapping("/register")
    public String registerCustomer(
            @Valid @ModelAttribute("customer") Customer customer,
            BindingResult result,
            Model model
    ) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            result.rejectValue("email", "error.customer", "An account with this email already exists");
        }

        if (customer.getBirthDate() != null) {
            LocalDate today = LocalDate.now();
            int age = Period.between(customer.getBirthDate(), today).getYears();

            if (age < 21) {
                result.rejectValue("birthDate", "error.customer", "You must be at least 21 years old to register.");
            }
        }

        if (result.hasErrors()) {
            return "register";
        }

        customerRepository.save(customer);

        return "redirect:/login";
    }

    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // HANDLE LOGIN
    @PostMapping("/login")
    public String loginCustomer(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {

        var customerOptional = customerRepository.findByEmail(email);

        if (customerOptional.isPresent() &&
                customerOptional.get().getPassword().equals(password)) {

            session.setAttribute("loggedInCustomer", customerOptional.get());

            return "redirect:/reservation";
        }

        model.addAttribute("loginError", "Invalid email or password");
        return "login";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}