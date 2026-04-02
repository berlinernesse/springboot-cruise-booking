package com.ernesse.kpopcruisebooking.controller;

import com.ernesse.kpopcruisebooking.repository.CruiseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CruiseController {

    private final CruiseRepository cruiseRepository;

    public CruiseController(CruiseRepository cruiseRepository) {
        this.cruiseRepository = cruiseRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredCruises", cruiseRepository.findAll());
        return "index";
    }

    @GetMapping("/cruises")
    public String showCruises(Model model) {
        model.addAttribute("cruises", cruiseRepository.findAll());
        return "cruises";
    }
}