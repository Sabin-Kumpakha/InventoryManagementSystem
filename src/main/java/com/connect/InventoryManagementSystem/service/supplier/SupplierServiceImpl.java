package com.connect.InventoryManagementSystem.service.supplier;

import com.connect.InventoryManagementSystem.dto.request.SupplierRequest;
import com.connect.InventoryManagementSystem.dto.response.SupplierResponse;
import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import com.connect.InventoryManagementSystem.exception.CSVException;
import com.connect.InventoryManagementSystem.exception.NotFoundException;
import com.connect.InventoryManagementSystem.model.Supplier;
import com.connect.InventoryManagementSystem.repository.SupplierRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public String getSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .map(Supplier::getSupplierName)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::supplierResponseBuilder)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = supplierRepository.save(convertToSupplier(request));
        return supplierResponseBuilder(supplier);
    }

    @Override
    public SupplierResponse updateSupplier(Long supplierId, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));

        supplier.setSupplierName(request.getSupplierName());
        supplier.setPhoneNumber(request.getPhoneNumber());
        supplier.setEmail(request.getEmail());
        supplier.setStatus(request.getStatus());

        return supplierResponseBuilder(supplierRepository.save(supplier));
    }

    @Override
    public SupplierResponse updateSupplierStatus(Long supplierId, SupplierStatus newStatus) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
        if (supplier.getStatus() != newStatus) {
            supplier.setStatus(newStatus);
        }
        return supplierResponseBuilder(supplierRepository.save(supplier));
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        // Check if supplier exists
        supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public boolean existsById(Long supplierId) {
        return false;
    }

    @Override
    public int importSuppliersFromCsv(MultipartFile file) {
        try(CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            reader.readNext();
            int count = 0;

            while ((line = reader.readNext()) != null) {
                Supplier supplier = Supplier.builder()
                        .supplierName(line[1])
                        .phoneNumber(line[2])
                        .email(line[3])
                        .status(SupplierStatus.valueOf(line[4]))
                        .build();
                supplierRepository.save(supplier);
                count++;
            }
            reader.close();
            return count;
        } catch (Exception e) {
            throw new CSVException("Failed to import suppliers from CSV file" + e.getMessage());
        }
    }

    @Override
    public byte[] exportToCsv() {
        List<Supplier> suppliersFile = supplierRepository.findAll();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            String[] header = {"ID", "Supplier Name", "Phone Number", "Email", "Status"};
            writer.writeNext(header);

            for (Supplier supplier : suppliersFile) {
                String[] line = {
                        supplier.getSupplierId().toString(),
                        supplier.getSupplierName(),
                        supplier.getPhoneNumber(),
                        supplier.getEmail(),
                        supplier.getStatus().toString()
                };
                writer.writeNext(line);
            }
            writer.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new CSVException("Failed to export suppliers to CSV file" + e.getMessage());
        }
    }

    private SupplierResponse supplierResponseBuilder(Supplier supplier) {
        return new SupplierResponse(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getPhoneNumber(),
                supplier.getEmail(),
                supplier.getStatus()
        );
    }

    private Supplier convertToSupplier(SupplierRequest request) {
        return Supplier.builder()
                .supplierName(request.getSupplierName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .status(request.getStatus())
                .build();
    }
}
