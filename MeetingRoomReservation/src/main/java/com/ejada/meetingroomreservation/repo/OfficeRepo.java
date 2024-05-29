package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepo extends JpaRepository<Office, Long> {

    @Query("SELECT o FROM Office o WHERE o.country.id = :countryId")
    List<Office> findAllByCountryId(@Param("countryId") Long countryId);

}
