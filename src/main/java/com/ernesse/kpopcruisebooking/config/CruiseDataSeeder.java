package com.ernesse.kpopcruisebooking.config;

import com.ernesse.kpopcruisebooking.model.Cruise;
import com.ernesse.kpopcruisebooking.repository.CruiseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CruiseDataSeeder implements CommandLineRunner {

    private final CruiseRepository cruiseRepository;

    public CruiseDataSeeder(CruiseRepository cruiseRepository) {
        this.cruiseRepository = cruiseRepository;
    }

    @Override
    public void run(String... args) {
        if (cruiseRepository.count() > 0) return;

        List<Cruise> cruises = List.of(
                buildCruise("BTS Ocean Stage World Tour Cruise", "Miami", "Bahamas (Nassau)",
                        LocalDateTime.of(2026, 7, 12, 16, 0),
                        LocalDateTime.of(2026, 7, 16, 8, 0),
                        2400.00, "A"),

                buildCruise("TWICE Mediterranean Summer Voyage", "Barcelona", "French Riviera (Nice)",
                        LocalDateTime.of(2026, 8, 3, 17, 0),
                        LocalDateTime.of(2026, 8, 8, 9, 0),
                        2200.00, "A"),

                buildCruise("Stray Kids Pacific Thunder Cruise", "Vancouver", "Alaska (Glacier Bay)",
                        LocalDateTime.of(2026, 6, 10, 15, 0),
                        LocalDateTime.of(2026, 6, 15, 8, 0),
                        1600.00, "B"),

                buildCruise("ILLIT Caribbean Glow Cruise", "Port Canaveral (Orlando)", "Cozumel, Mexico",
                        LocalDateTime.of(2026, 5, 18, 16, 0),
                        LocalDateTime.of(2026, 5, 22, 8, 0),
                        1400.00, "B"),

                buildCruise("MONSTA X Tokyo Night Lights Cruise", "Busan", "Tokyo",
                        LocalDateTime.of(2026, 9, 7, 18, 0),
                        LocalDateTime.of(2026, 9, 11, 9, 0),
                        1050.00, "C"),

                buildCruise("VIVIZ Southeast Asia Sunset Cruise", "Singapore", "Phuket, Thailand",
                        LocalDateTime.of(2026, 10, 5, 18, 0),
                        LocalDateTime.of(2026, 10, 9, 9, 0),
                        950.00, "C"),

                buildCruise("K-Pop Rising Stars Sea Carnival", "Incheon", "Okinawa",
                        LocalDateTime.of(2026, 4, 15, 17, 0),
                        LocalDateTime.of(2026, 4, 18, 8, 0),
                        650.00, "FESTIVAL")
        );

        cruiseRepository.saveAll(cruises);
        System.out.println("✅ Seeded cruises: " + cruises.size());
    }

    private Cruise buildCruise(String name, String origin, String destination,
                               LocalDateTime depart, LocalDateTime arrive,
                               double price, String tier) {

        Cruise c = new Cruise();
        c.setCruiseName(name);
        c.setOrigin(origin);
        c.setDestination(destination);
        c.setDepartureTime(depart);
        c.setArrivalTime(arrive);
        c.setPrice(price);
        c.setTier(tier);
        return c;
    }
}