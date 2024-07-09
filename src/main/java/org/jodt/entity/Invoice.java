package org.jodt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "emission_date")
    private LocalDate emissionDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @OneToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private Double amount;


}
