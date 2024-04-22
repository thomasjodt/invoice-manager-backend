package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Vendor;
import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;
import org.jodt.repository.IVendorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class VendorService implements IVendorService {
    @Inject
    IVendorRepository repository;

    @Inject
    InvoiceIService invoiceService;


    @Override
    public ResponseDTO<List<VendorDto>> findByName(String vendorName) {
        ResponseDTO<List<Vendor>> response = repository.findByName(vendorName);
        List<Vendor> vendors = response.getData();
        List<VendorDto> list = new ArrayList<>();

        vendors.forEach(vendor -> {
            ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(vendor.getId());
            AtomicReference<Double> allPaid = new AtomicReference<>(0D);
            AtomicReference<Double> allAmount = new AtomicReference<>(0D);

            invoices.getData().forEach((invoice) -> {
                allAmount.updateAndGet(v -> v + invoice.getAmount());
                invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
            });

            VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
            dto.setBalance(allAmount.get() - allPaid.get());

            list.add(dto);
        });
        ResponseDTO<List<VendorDto>> res = new ResponseDTO<>();
        res.setData(list);

        return res;
    }

    @Override
    public ResponseDTO<List<VendorDto>> findByName(String vendorName, Integer page, Integer offset) {
        ResponseDTO<List<Vendor>> response = repository.findByName(vendorName, page, offset);
        List<Vendor> vendors = response.getData();
        List<VendorDto> list = new ArrayList<>();

        vendors.forEach(vendor -> {
            ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(vendor.getId());
            AtomicReference<Double> allPaid = new AtomicReference<>(0D);
            AtomicReference<Double> allAmount = new AtomicReference<>(0D);

            invoices.getData().forEach((invoice) -> {
                allAmount.updateAndGet(v -> v + invoice.getAmount());
                invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
            });

            VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
            dto.setBalance(allAmount.get() - allPaid.get());

            list.add(dto);
        });
        ResponseDTO<List<VendorDto>> res = new ResponseDTO<>();
        res.setData(list);
        res.setCount(response.getCount());

        return res;
    }

    @Override
    public ResponseDTO<List<VendorDto>> getAll() {
        ResponseDTO<List<Vendor>> response = repository.getAll();
        List<Vendor> vendors = response.getData();
        List<VendorDto> list = new ArrayList<>();

        vendors.forEach(vendor -> {
            ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(vendor.getId());
            AtomicReference<Double> allPaid = new AtomicReference<>(0D);
            AtomicReference<Double> allAmount = new AtomicReference<>(0D);

            invoices.getData().forEach((invoice) -> {
                allAmount.updateAndGet(v -> v + invoice.getAmount());
                invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
            });

            VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
            dto.setBalance(allAmount.get() - allPaid.get());

            list.add(dto);
        });
        ResponseDTO<List<VendorDto>> res = new ResponseDTO<>();
        res.setData(list);

        return res;
    }

    @Override
    public ResponseDTO<List<VendorDto>> getAll(Integer page, Integer offset) {
        ResponseDTO<List<Vendor>> response = repository.getAll(page, offset);
        List<Vendor> vendors = response.getData();
        List<VendorDto> list = new ArrayList<>();

        vendors.forEach(vendor -> {
            ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(vendor.getId());
            AtomicReference<Double> allPaid = new AtomicReference<>(0D);
            AtomicReference<Double> allAmount = new AtomicReference<>(0D);

            invoices.getData().forEach((invoice) -> {
                allAmount.updateAndGet(v -> v + invoice.getAmount());
                invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
            });

            VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
            dto.setBalance(allAmount.get() - allPaid.get());

            list.add(dto);
        });
        ResponseDTO<List<VendorDto>> res = new ResponseDTO<>();
        res.setData(list);
        res.setCount(response.getCount());

        return res;
    }

    @Override
    public Optional<VendorDto> findById(Long id) {
        Vendor vendor = repository.findById(id);
        ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(vendor.getId());

        AtomicReference<Double> allPaid = new AtomicReference<>(0D);
        AtomicReference<Double> allAmount = new AtomicReference<>(0D);

        invoices.getData().forEach((invoice) -> {
            allAmount.updateAndGet(v -> v + invoice.getAmount());
            invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
        });

        VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
        dto.setBalance(allAmount.get() - allPaid.get());

        return Optional.of(dto);
    }

    @Override
    public VendorDto save(VendorDto vendorDto) {
        Vendor vendor = new Vendor(vendorDto.getId(), vendorDto.getName(), vendorDto.getFullName());
        var newVendor = repository.save(vendor);

        ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(newVendor.getId());

        AtomicReference<Double> allPaid = new AtomicReference<>(0D);
        AtomicReference<Double> allAmount = new AtomicReference<>(0D);

        invoices.getData().forEach((invoice) -> {
            allAmount.updateAndGet(v -> v + invoice.getAmount());
            invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
        });

        VendorDto dto = new VendorDto(newVendor.getId(), newVendor.getName(), newVendor.getFullName(), 0D);
        dto.setBalance(allAmount.get() - allPaid.get());

        return dto;
    }

    @Override
    public VendorDto update(VendorDto vendorDto) {
        Vendor vendor = new Vendor(vendorDto.getId(), vendorDto.getName(), vendorDto.getFullName());
        Vendor updatedVendor = repository.update(vendor);

        ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(updatedVendor.getId());

        AtomicReference<Double> allPaid = new AtomicReference<>(0D);
        AtomicReference<Double> allAmount = new AtomicReference<>(0D);

        invoices.getData().forEach((invoice) -> {
            allAmount.updateAndGet(v -> v + invoice.getAmount());
            invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
        });

        VendorDto dto = new VendorDto(updatedVendor.getId(), updatedVendor.getName(), updatedVendor.getFullName(), 0D);
        dto.setBalance(allAmount.get() - allPaid.get());

        return dto;
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
