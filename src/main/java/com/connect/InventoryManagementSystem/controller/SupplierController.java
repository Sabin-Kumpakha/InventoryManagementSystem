package com.connect.InventoryManagementSystem.controller;

import com.connect.InventoryManagementSystem.dto.request.SupplierRequest;
import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import com.connect.InventoryManagementSystem.service.supplier.SupplierService;
import com.connect.InventoryManagementSystem.utils.CSVUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllSuppliers() {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = supplierService.getAllSuppliers();
            response.put("status", HttpStatus.OK);
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{supplierId}")
    public ResponseEntity<Map<String, Object>> getSupplierById(Long supplierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = supplierService.getSupplierById(supplierId);
            response.put("status", HttpStatus.OK);
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = supplierService.createSupplier(request);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Supplier created successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{supplierId}")
    public ResponseEntity<Map<String, Object>> updateSupplier(@PathVariable Long supplierId, @Valid @RequestBody SupplierRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = supplierService.updateSupplier(supplierId, request);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Supplier updated successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/status/{supplierId}")
    public ResponseEntity<Map<String, Object>> updateSupplierStatus(@PathVariable Long supplierId, @RequestParam SupplierStatus newStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = supplierService.updateSupplierStatus(supplierId, newStatus);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Supplier status updated successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN'")
    @PostMapping("/delete/{supplierId}")
    public ResponseEntity<Map<String, Object>> deleteSupplier(@PathVariable Long supplierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            supplierService.deleteSupplier(supplierId);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Supplier deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importSuppliers(@RequestParam("suppliersCsv") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            int numberOfImports = supplierService.importSuppliersFromCsv(file);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Successfully imported " + numberOfImports + " suppliers.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN'")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToCsv() {
        String filename = "SuppliersExport.csv";
        return CSVUtils.getResponseEntity(supplierService.exportToCsv(), filename);
    }
}
