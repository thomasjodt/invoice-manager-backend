package org.jodt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Transient
    private Double balance;

    @Transient
    private List<Invoice> invoices;

    @Transient
    private List<Payment> payments;
}
