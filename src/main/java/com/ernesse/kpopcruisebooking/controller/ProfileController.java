package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.model.Customer;
import com.ernesse.kpopcruisebooking.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;

@Controller
public class ProfileController {

    private final CustomerRepository customerRepository;

    public ProfileController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Customer customer = customerRepository.findById(loggedInCustomer.getCustomerId()).orElse(null);

        if (customer == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("customer", customer);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid Customer customer,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                @RequestParam("customerId") Long customerId) {

        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        if (loggedInCustomer == null) {
            return "redirect:/login";
        }

        Customer existingCustomer = customerRepository.findById(customerId).orElse(null);

        if (existingCustomer == null) {
            return "redirect:/login";
        }

        if (!existingCustomer.getCustomerId().equals(loggedInCustomer.getCustomerId())) {
            return "redirect:/login";
        }

        customerRepository.findByEmail(customer.getEmail()).ifPresent(foundCustomer -> {
            if (!foundCustomer.getCustomerId().equals(existingCustomer.getCustomerId())) {
                result.rejectValue("email", "error.customer", "An account with this email already exists");
            }
        });

        if (customer.getBirthDate() != null) {
            int age = Period.between(customer.getBirthDate(), LocalDate.now()).getYears();
            if (age < 21) {
                result.rejectValue("birthDate", "error.customer", "You must be at least 21 years old.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("customer", customer);
            return "profile";
        }

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setBirthDate(customer.getBirthDate());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setPostalCode(customer.getPostalCode());

        customerRepository.save(existingCustomer);

        session.setAttribute("loggedInCustomer", existingCustomer);

        return "redirect:/profile?success";
    }
}