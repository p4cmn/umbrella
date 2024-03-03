package com.artem.umbrella.repository;

import com.artem.umbrella.entity.Virus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VirusRepository extends JpaRepository<Virus, Long> {

    boolean existsByName(String name);
}
