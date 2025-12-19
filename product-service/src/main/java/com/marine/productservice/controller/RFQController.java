package com.marine.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marine.productservice.DTO.*;
import com.marine.productservice.api.RFQApi;
import com.marine.productservice.entity.OrderEntity;
import com.marine.productservice.model.SupplierQuoteModel;
import com.marine.productservice.paginaion.PaginationResponse;
import com.marine.productservice.repository.OrderRepository;
import com.marine.productservice.service.RFQService;
import com.marine.productservice.util.DtoToDao;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
public class RFQController implements RFQApi {
    @Autowired
    private DtoToDao dtoToDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RFQService rfqService;
    public PaginationResponse<OrderSummaryDTO> getAllRFQs(
            @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
            @RequestParam("filters") String filterJson,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderSummary> orderPage = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Filter> filterList = mapper.readValue(filterJson, new TypeReference<>() {});
        List<OrderSummaryDTO> ordersList;
        Long totalElements;
        int totalPages;
        if (!"null".equalsIgnoreCase(filterJson) && !filterJson.trim().isEmpty()) {
            Page<OrderEntity> filterdOrders = rfqService.getFilteredRecords(filterList, pageable); // ✅ FIXED: assign result
            ordersList =  rfqService.buildFilteredOrderDto(filterdOrders);
            totalElements = filterdOrders.getTotalElements();
            totalPages = filterdOrders.getTotalPages();
        } else {
            orderPage = rfqService.getAllRFQs(pageable);
            ordersList  = dtoToDao.getRfqList(orderPage.getContent());
            totalElements = orderPage.getTotalElements();
            totalPages = orderPage.getTotalPages();
        }

        return new PaginationResponse<>(ordersList,totalElements, totalPages, pageable.getPageNumber());
    }




    @Override
    public RFQDto getRFQByOrderId(String orderId) {
        return rfqService.getRFQByOrderId(orderId);
    }

    @Override
    public String getSupplierQuote(SupplierQuoteModel purchaseQuotModel) throws MessagingException, IOException {
        return rfqService.getSupplierQuote(purchaseQuotModel);
    }

    public List<PurchaseQuoteDto> importExcel(@RequestParam("file") MultipartFile file,String purchaseQuoteId) {
        try {
            String fileName = file.getOriginalFilename(); // ✅ This gives you the file name
            System.out.println("File Name: " + fileName);

            List<PurchaseQuoteDto> productList = rfqService.reimportSupplierExcel(file.getInputStream(), purchaseQuoteId);
            return productList;
        } catch (Exception e) {
            return null;
        }
    }

}
