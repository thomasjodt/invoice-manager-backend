package org.jodt.models;

import lombok.*;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto extends Invoice {
    private List<Payment> payments = new ArrayList<>();

    public InvoiceDto (Invoice invoice) {
        this.id  = invoice.getId();
        this.amount = invoice.getAmount();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.emissionDate = invoice.getEmissionDate();
        this.dueDate = invoice.getDueDate();
        this.vendor = invoice.getVendor();
    }
}
