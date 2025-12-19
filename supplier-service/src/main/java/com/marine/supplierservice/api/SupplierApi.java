package com.marine.supplierservice.api;

import com.marine.productservice.DTO.OrderSummaryDTO;
import com.marine.productservice.DTO.RFQDto;
import com.marine.productservice.paginaion.PaginationResponse;
import com.marine.supplierservice.model.SupplierImportFileModel;
import com.marine.supplierservice.model.SupplierQuoteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/supplier")
public interface SupplierApi {
    @GetMapping
    public PaginationResponse<SupplierQuoteDto> getAllSupplierPurchaseQuote(
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) ;
    @PostMapping("/upload-supplier-sheet")
    public ResponseEntity<Map<String, String>> importExcel(@RequestParam("email") String email,
                                                           @RequestParam("company") String company,
                                                           @RequestParam("phone") String phone,
                                                           @RequestParam("file") MultipartFile file);

    @GetMapping("/supplier-quote-details")
    public RFQDto getSupplierOrdersByQuoteId(@RequestParam String quoteId) ;
}
