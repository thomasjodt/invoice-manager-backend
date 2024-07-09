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
    protected Long id;

    @Column(name = "invoice_number")
    protected String invoiceNumber;

    @Column(name = "emission_date")
    protected LocalDate emissionDate;

    @Column(name = "due_date")
    protected LocalDate dueDate;

    @OneToOne
    @JoinColumn(name = "vendor_id")
    protected Vendor vendor;

    protected Double amount;
}
