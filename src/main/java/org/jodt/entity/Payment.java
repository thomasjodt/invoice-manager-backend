package org.jodt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "invoice_id")
    private Long invoiceId;
}
