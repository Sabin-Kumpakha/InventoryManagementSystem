package com.connect.InventoryManagementSystem.controller;

import com.connect.InventoryManagementSystem.dto.request.OrderRequest;
import com.connect.InventoryManagementSystem.service.order.OrderService;
import com.connect.InventoryManagementSystem.utils.CSVUtils;
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
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = orderService.getAllOrders();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Orders fetched successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = orderService.getOrderById(orderId);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Order fetched successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = orderService.createOrder(request);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Order created successfully");
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/update/{orderId}")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = orderService.updateOrder(orderId, request);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Order updated successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.deleteOrder(orderId);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Order deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importOrdersFromCsv(@RequestParam("orderCsv") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = orderService.importOrdersFromCsv(file);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Orders imported successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportOrdersToCsv() {
        String filename = "OrdersExport.csv";
        return CSVUtils.getResponseEntity(orderService.exportToCsv(), filename);
    }


}

