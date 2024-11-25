package com.connect.InventoryManagementSystem.service.supplier;

import com.connect.InventoryManagementSystem.dto.request.SupplierRequest;
import com.connect.InventoryManagementSystem.dto.response.SupplierResponse;
import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SupplierService {

    String getSupplierById(Long supplierId);

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse createSupplier(SupplierRequest request);

    SupplierResponse updateSupplier(Long supplierId, SupplierRequest request);

    SupplierResponse updateSupplierStatus(Long supplierId, SupplierStatus status);

    void deleteSupplier(Long supplierId);

    boolean existsById(Long supplierId);

    // import from csv
    int importSuppliersFromCsv(MultipartFile file);

    // export to csv
    byte[] exportToCsv();

}
