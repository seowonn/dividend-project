package com.example.dividend.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "COMPANY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String name;
}
