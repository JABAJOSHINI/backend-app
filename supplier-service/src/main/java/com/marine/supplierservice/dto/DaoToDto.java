package com.marine.supplierservice.dto;

import com.marine.productservice.DTO.ProductDto;
import com.marine.supplierservice.entity.SupplierDetailsEntity;
import com.marine.supplierservice.entity.SupplierQuoteEntity;
import com.marine.supplierservice.model.SupplierContactsDTO;
import com.marine.supplierservice.model.SupplierQuoteDto;
import com.marine.supplierservice.model.SupplierQuoteIdDetailsDto;
import com.marine.supplierservice.model.SupplierQuotes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DaoToDto {
    public List<SupplierQuoteDto> getSupplierQuoteList(List<SupplierQuotes> supplierQuote) {
        List<SupplierQuoteDto> dtoList = new ArrayList<>();
        if(!supplierQuote.isEmpty()) {
            for (SupplierQuotes dto : supplierQuote) {
                SupplierQuoteDto quotes = new SupplierQuoteDto();  // Create a new instance in each iteration
               quotes.setSupplierQuote(dto.getSupplierQuote());
               quotes.setSupplierEmail(dto.getSupplierEmail());
               quotes.setStatus(dto.getStatus());
               quotes.setCreatedAt(dto.getCreatedAt());
                quotes.setCompany(dto.getCompany());
               dtoList.add(quotes);
            }}
        return dtoList;
    }
    public SupplierQuoteIdDetailsDto getSupplierQuoteDetails(List<SupplierQuoteEntity> quoteDetails) {
        SupplierQuoteIdDetailsDto dto = new SupplierQuoteIdDetailsDto();
        String targetOrderId = null;
        List<ProductDto> productsList = new ArrayList<>();
        for(SupplierQuoteEntity quotes: quoteDetails) {
            targetOrderId = quotes.getQuoteId();
            if (quotes.getQuoteId().equals(targetOrderId)) {
                dto.setId(quotes.getId());
                dto.setSupplierQuoteId(targetOrderId);
                dto.setContacts(getSupplierContacts(quotes.getSupplierDetails()));
            }
           // productsList.add(getProductDto(quotes.));
        }
       // rfq.setProducts(productsList);
        return dto;
    }

    private SupplierContactsDTO getSupplierContacts(SupplierDetailsEntity supplierDetails) {
        SupplierContactsDTO dto = new SupplierContactsDTO();
        dto.setEmail(supplierDetails.getEmail());
        dto.setPhone(supplierDetails.getPhone());
        dto.setCompany(supplierDetails.getCompany());
        return dto;
    }
}
