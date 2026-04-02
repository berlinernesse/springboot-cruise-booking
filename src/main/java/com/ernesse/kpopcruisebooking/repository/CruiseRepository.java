package com.ernesse.kpopcruisebooking.repository;

import com.ernesse.kpopcruisebooking.model.Cruise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CruiseRepository extends JpaRepository<Cruise, Long> {
}