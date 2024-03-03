package com.artem.umbrella.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
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