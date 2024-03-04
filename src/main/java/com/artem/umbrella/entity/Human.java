package com.artem.umbrella.entity;

import com.artem.umbrella.enumeration.HealthStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "human")
public class Human {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private HealthStatus healthStatus;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany
    @JoinTable(name = "human_virus",
            joinColumns = @JoinColumn(name = "human_id"),
            inverseJoinColumns = @JoinColumn(name = "virus_id"))
    private List<Virus> viruses;
}
