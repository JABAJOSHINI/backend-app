package com.marine.productservice.controller;

import com.example.appcommons.utility.ImportFileResponse;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.api.ProductApi;
import com.marine.productservice.entity.Products;
import com.marine.productservice.exception.APIException;
import com.marine.productservice.exception.ProductNotSavedException;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.ProductModel;
import com.marine.productservice.paginaion.PaginationResponse;
import com.marine.productservice.repository.InventoryRepository;
import com.marine.productservice.repository.ProductCategoryRepository;
import com.marine.productservice.repository.ProductRepository;
import com.marine.productservice.service.ProductService;
import com.marine.productservice.util.DtoToDao;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.appcommons.utility.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
public class ProductController implements ProductApi {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private DtoToDao dtoToDao;
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public Response createProduct(@Valid @RequestBody ProductModel productsModel) {
        Response response = new Response();
        try {
            Products products = productService.createProduct(productsModel);
            if(products == null) {
                throw new ProductNotSavedException("Product with name '" + productsModel.getName() + "'not saved.");
            }
            inventoryRepository.save(products.getInventory());
            productRepository.save(products);

            logger.debug("Product save done!!!");
            response.setData(products.getId());
            response.setMessage("SUCCESS");
            response.setStatus(Boolean.FALSE);
        } catch (APIException e) {
            logger.error("Product save error >>>>>>>>> "+e.getMessage());
            throw e;
        }
        return response;
    }

    @Override
    public ResponseEntity<?> deleteProductById(Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @Override
    public List<ProductModel> searchByProductDescOrSKU(String productDescOrSku) {
        logger.error("Search by product or SKU api command recevied");
        try {
            return productService.searchByProductDescOrSKU(productDescOrSku);
        } catch (APIException e) {
        logger.error("Search by product or SKU api command error >>>>>>>>> "+e.getMessage());
        throw e;
    }
    }

    @Override
    public List<ProductDto> getProductIdsByNames(List<String> productNames) {
        if(productNames.isEmpty()){

        }
        return productService.getProductIdsByNames(productNames);
    }

    @Override
    public ProductDto getProductByIds(Long id) {
        return productService.getProductByIds(id);
    }

    public ResponseEntity<PaginationResponse<ProductDto>> getProductsByCategory(
            @RequestParam String categoryName,
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
           Pageable pageable = PageRequest.of(page, size);
           if(categoryName.equals("products")){
            categoryName = "accommodation-store";
           }
            PaginationResponse<ProductDto> response = productService.getProductsByCategory(categoryName, searchText, pageable);
            return ResponseEntity.ok(response);
        } catch (APIException e) {
            logger.error("Product by category API error >>>>>>>>> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaginationResponse<>(Collections.emptyList(), 0L, 0, page));
        }
    }

    @Override
    public Response createEnquiry(EnquiryModel enquiry) throws Exception {
        logger.debug("RFQ Api command received!!!");
        Response response = new Response();
        try{
        if(enquiry != null){
            String orderId = productService.createEnquiry(enquiry);
            logger.debug("Send Enquiry done!!!");
            response.setData(orderId);
            response.setMessage("SUCCESS");
            response.setStatus(Boolean.FALSE);
        }
        } catch (APIException e) {
            logger.error("RFQ api error >>>>>>>>> "+e.getMessage());
            throw e;
        }
        return response;
    }

    @GetMapping("/findByProduct")
    public ResponseEntity<List<ProductDto>>  findByProduct(@RequestParam String productName) {
        Response response = new Response();
        try {
            List<Products> products = productRepository.findByName(productName);
            List<ProductDto> dtoProductList = productService.getProductFromDb(products);
            return new ResponseEntity<>(dtoProductList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Product save error >>>>>>>>> "+e.getMessage());
            response.setData(null);
            response.setMessage("FAIL");
            response.setStatus(Boolean.TRUE);
        }
        return null;
    }
    @GetMapping("/product/list")
    public List<ProductDto> getAllProductMasters() {
        List<ProductDto> products = productService.getAllProductDtos();
        return products;
    }

    @Override
    public ImportFileResponse ImportExcel(MultipartFile file) {
        return productService.importExcelToDb(file);
    }
}
