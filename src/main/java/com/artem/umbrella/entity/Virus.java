package com.artem.umbrella.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "virus")
public class Virus {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int infectiousnessPercentage;

    @ManyToMany(mappedBy = "viruses")
    private Set<Human> humans;
}