package com.marine.productservice.api;

import com.example.appcommons.utility.ImportFileResponse;
import com.example.appcommons.utility.Response;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.ProductModel;
import com.marine.productservice.paginaion.PaginationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/api/products")
public interface ProductApi {

    @PostMapping("/save")
    public Response createProduct(@Valid @RequestBody ProductModel productModel);
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) ;
    @GetMapping("/search")
    public List<ProductModel> searchByProductDescOrSKU(@RequestParam("value") String productDescription);

    @PostMapping("/by-names")
    public List<ProductDto> getProductIdsByNames(@RequestBody List<String> productNames) ;
    @GetMapping("/{id}")
    public ProductDto getProductByIds(@PathVariable Long id) ;

    @PostMapping("/import")
    public ImportFileResponse ImportExcel(@RequestParam("file") MultipartFile file);
    @GetMapping("/productsByCategory")
    public ResponseEntity<PaginationResponse<ProductDto>> getProductsByCategory(
            @RequestParam(required = false, defaultValue = "") String categoryName,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size);
    @PostMapping("/enquiry")
    public Response createEnquiry(@Valid @RequestBody EnquiryModel enquiry) throws Exception;
}


