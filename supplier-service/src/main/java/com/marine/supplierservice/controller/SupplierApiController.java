package com.marine.supplierservice.controller;

import com.marine.productservice.DTO.OrderSummary;
import com.marine.productservice.DTO.OrderSummaryDTO;
import com.marine.productservice.DTO.RFQDto;
import com.marine.productservice.controller.ProductController;
import com.marine.productservice.paginaion.PaginationResponse;
import com.marine.productservice.util.DtoToDao;
import com.marine.supplierservice.api.SupplierApi;
import com.marine.supplierservice.dto.DaoToDto;
import com.marine.supplierservice.model.SupplierImportFileModel;
import com.marine.supplierservice.model.SupplierQuoteDto;
import com.marine.supplierservice.model.SupplierQuotes;
import com.marine.supplierservice.service.SupplierApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class SupplierApiController implements SupplierApi {
    @Autowired
    private SupplierApiService supplierApiService;
    @Autowired
    private DaoToDto daoToDto;
    Logger logger = LoggerFactory.getLogger(SupplierApiController.class);


    @Override
    public PaginationResponse<SupplierQuoteDto> getAllSupplierPurchaseQuote(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierQuotes> orderPage = null;
        if( !"null".equalsIgnoreCase(searchText) && !searchText.trim().isEmpty()){

        } else {
            orderPage = supplierApiService.getAllSupplierPurchaseQuote(pageable);
        }
        List<SupplierQuoteDto> ordersList  = daoToDto.getSupplierQuoteList(orderPage.getContent());
        Long totalElements = orderPage.getTotalElements();
        int totalPages = orderPage.getTotalPages();
        return new PaginationResponse<>(ordersList,totalElements, totalPages, pageable.getPageNumber());
    }

    @Override
    public ResponseEntity<Map<String, String>> importExcel(String email, String company, String phone, MultipartFile file) {
        logger.error("Supplier Quote command api received");

        try {
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }

            System.out.println("File Name: " + fileName);
            String purchaseQuoteId = fileName.replace("RFQ", "PQ");
            return supplierApiService.ImportAndSaveSupplierExcel(file, purchaseQuoteId, email, company, phone  );
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public RFQDto getSupplierOrdersByQuoteId(String quoteId) {
            return supplierApiService.getSupplierOrdersByQuoteId(quoteId);
    }

}

