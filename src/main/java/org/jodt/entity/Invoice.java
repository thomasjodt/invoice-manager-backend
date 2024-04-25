package org.jodt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jodt.models.InvoiceDto;

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
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    protected Vendor vendor;

    protected Double amount;

    public Invoice (InvoiceDto invoiceDto) {
        this.id = invoiceDto.getId();
        this.invoiceNumber = invoiceDto.getInvoiceNumber();
        this.emissionDate = invoiceDto.getEmissionDate();
        this.dueDate = invoiceDto.getDueDate();
        this.vendor = invoiceDto.getVendor();
        this.amount = invoiceDto.getAmount();
    }
}
