package com.marine.productservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marine.productservice.DTO.*;
import com.marine.productservice.model.SupplierQuoteModel;
import com.marine.productservice.paginaion.PaginationResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/rfq")
public interface RFQApi {
    @GetMapping
    public PaginationResponse<OrderSummaryDTO> getAllRFQs(
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam("filters") String filterJson,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws JsonProcessingException;

    @GetMapping("/order-details")
    public RFQDto getRFQByOrderId(@RequestParam String orderId) ;
    @PostMapping("/supplier-quote")
    public String getSupplierQuote(@RequestBody SupplierQuoteModel supplierQuoteModel) throws MessagingException, IOException;

    @PostMapping("/upload-supplier-sheet")
    public List<PurchaseQuoteDto> importExcel(@RequestParam("file") MultipartFile file,@RequestParam("purchaseQuoteId") String purchaseQuoteId);
}

