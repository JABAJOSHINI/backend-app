package com.marine.productservice.util;

import com.marine.productservice.DTO.*;
import com.marine.productservice.entity.*;
import com.marine.productservice.model.ContactModel;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.ProductModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DtoToDao {

    //create product
    public Products saveProducts(@Valid ProductModel body, ProductCategory productCategory) {

        Products productDao = new Products();
        LocalDate currentDate = LocalDate.now();
        productDao.setCreatedAt(currentDate);
        productDao.setDescription(body.getDescription());
        productDao.setCoo(body.getCoo());
        productDao.setHazodus(body.getHazodus());
        productDao.setModifiedAt(currentDate);
        productDao.setName(body.getName());
        productDao.setMake(body.getMake());
        productDao.setModel(body.getModel());
        productDao.setSku(body.getSku());
        productDao.setSpecification(body.getSpecification());
        productDao.setImage(body.getImage());
        productDao.setPartNumber(body.getPartNumber());
        productDao.setEquipment(body.getEquipment());
        if(body.getPrice() != null){
            productDao.setPrice(body.getPrice());
        }
        if(productCategory != null){
            productDao.setCategory(productCategory);
        }
         ProductInventory inventory = new ProductInventory();
         inventory.setCreatedAt(currentDate);
         inventory.setModifiedAt(currentDate);
         inventory.setQuantity(body.getQuantity());
         productDao.setInventory(inventory);
        return productDao;
    }
    public List<ProductDto> getProductFromDb(List<Products> products) {
        List<ProductDto> dtoList = new ArrayList<>();
        for (Products product : products) {
            dtoList.add(getProductDto(product));
        }
        return dtoList;
    }

    public ProductDto getProductDto(Products product) {
        Set<CategoryDto> categoryList = new HashSet<>();
        ProductDto productDto = new ProductDto();  // Create a new instance in each iteration
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setModifiedAt(product.getModifiedAt());
        productDto.setSku(product.getSku());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setMake(product.getMake());
        productDto.setModel(product.getModel());
        productDto.setSpecification(product.getSpecification());
        productDto.setEquipment(product.getEquipment());
        productDto.setImage(product.getImage());
        productDto.setPartNumber(product.getPartNumber());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(product.getCategory().getCategoryName());
        productDto.setCategoryName(product.getCategory().getCategoryName());
        categoryList.add(categoryDto);
        productDto.setProductCategory(categoryList);
        List<VariantDto> variantDtos = product.getVariants().stream()
                .map(v -> new VariantDto(v.getId(), v.getName()))
                .toList();

        productDto.setVariants(variantDtos);

        return productDto;
    }

    public ClientEntity createClientDetails(ClientEntity clientEntity, EnquiryModel enquiry) {
        String firstName = enquiry.getFullName();
        String lastName  = enquiry.getLastName();

        String fullName;

        if (firstName != null && lastName != null) {
            fullName = firstName + " " + lastName;
        } else if (firstName != null) {
            fullName = firstName;
        } else if (lastName != null) {
            fullName = lastName;
        } else {
            fullName = "N/A";
        }
        if(enquiry.getEmail() != null) {
            clientEntity.setEmail(enquiry.getEmail());
        }
        if(enquiry.getCompanyAddress() != null) {
            clientEntity.setCompanyAddress(enquiry.getCompanyAddress());
        }
        if(enquiry.getCompanyName() != null) {
            clientEntity.setCompanyName(enquiry.getCompanyName());
        }
        if(enquiry.getPhone() != null) {
            clientEntity.setPhone(enquiry.getPhone());
        }
        clientEntity.setFullName(fullName);
        if(enquiry.getCountry() != null && enquiry.getCountry().getName() != null) {
            clientEntity.setCountry(enquiry.getCountry().getName());
        }
        if(enquiry.getCountry() != null &&  enquiry.getCountry().getDialCode() != null) {
            clientEntity.setDialCode(enquiry.getCountry().getDialCode());
        }
        return clientEntity;
    }
    public ClientEntity saveCusomerContacts(ClientEntity clientEntity, ContactModel body) {
        if(body.getEmail() != null) {
            clientEntity.setEmail(body.getEmail());
        }
        if(body.getMessage() != null) {
            clientEntity.setCompanyAddress(body.getMessage());
        }
        if(body.getFullName() != null) {
            clientEntity.setFullName(body.getFullName());
        }
        return clientEntity;
    }

    public List<OrderSummaryDTO> getRfqList(List<OrderSummary> rfqOrders) {
        List<OrderSummaryDTO> dtoList = new ArrayList<>();
        if(!rfqOrders.isEmpty()) {
        for (OrderSummary rfq : rfqOrders) {
            OrderSummaryDTO orderDto = new OrderSummaryDTO();  // Create a new instance in each iteration
            orderDto.setRFQOrderId(rfq.getOrderId());
            orderDto.setClientID(rfq.getClientId());
            orderDto.setClientEmail(rfq.getClientEmail());
            orderDto.setCreatedAt(rfq.getCreatedAt());
            dtoList.add(orderDto);
        }}
        return dtoList;
    }

    public RFQDto getRFQOrderDetails(List<OrderEntity> orderDetailsList) {
        RFQDto rfq = new RFQDto();
        String targetOrderId = null;
        List<ProductDto> productsList = new ArrayList<>();
        for(OrderEntity orderDetails: orderDetailsList) {
            targetOrderId = orderDetails.getRfqOrderId();
            if (orderDetails.getRfqOrderId().equals(targetOrderId)) {
            rfq.setRfqorderId(orderDetails.getRfqOrderId());
            rfq.setContacts(getClientDetails(orderDetails.getClientEntity()));
            rfq.setCreatedAt(orderDetails.getCreatedAt());
            rfq.setModifiedAt(orderDetails.getModifiedAt());
        }
            productsList.add(getProductDto(orderDetails.getProducts()));
        }
        rfq.setProducts(productsList);
        return rfq;
    }

    public ContactsDTO getClientDetails(ClientEntity clientEntity) {
        ContactsDTO contacts = new ContactsDTO();
        contacts.setId(clientEntity.getId());
        contacts.setEmail(clientEntity.getEmail());
        contacts.setFullName(clientEntity.getFullName());
        contacts.setCountry(clientEntity.getCountry());
        contacts.setPhone(clientEntity.getPhone());
        contacts.setCompanyAddress(clientEntity.getCompanyAddress());
        contacts.setCompanyName(clientEntity.getCompanyName());
        return contacts;
    }

    public OrderEntity getOrderDetails(Products product, String orderNumber, ClientEntity clientDetailsToSave) {
        OrderEntity orders = new OrderEntity();
        orders.setRfqOrderId(orderNumber);
        orders.setProducts(product);
        orders.setStatus("ACTIVE");
        orders.setClientEntity(clientDetailsToSave);
        LocalDate currentDate = LocalDate.now();
        orders.setCreatedAt(currentDate);
        orders.setModifiedAt(currentDate);
        return orders;
    }
}
