package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {
}
