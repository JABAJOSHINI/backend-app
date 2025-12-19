package com.marine.supplierservice.feignClient;

import com.marine.productservice.DTO.ProductDto;
import com.marine.supplierservice.model.SupplierImportFileModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:9102/api/product")
public interface ProductServiceClient {
    @PostMapping("/by-names")
    List<ProductDto> getProductIdsByNames(@RequestBody List<String> productNames);

}
