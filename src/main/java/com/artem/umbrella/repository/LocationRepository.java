package com.artem.umbrella.repository;

import com.artem.umbrella.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByName(String name);
    boolean existsByNameIn(List<String> names);
}
