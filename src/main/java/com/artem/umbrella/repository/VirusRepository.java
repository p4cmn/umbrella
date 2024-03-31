package com.artem.umbrella.repository;

import com.artem.umbrella.entity.Virus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirusRepository extends JpaRepository<Virus, Long> {

    boolean existsByName(String name);

    @Query(value = "SELECT v.* FROM virus v "
            + "JOIN human_virus hv ON v.id = hv.virus_id "
            + "JOIN human h ON hv.human_id = h.id "
            + "WHERE h.location_id = :locationId", nativeQuery = true)
    List<Virus> findAllByLocationId(Long locationId);
}
