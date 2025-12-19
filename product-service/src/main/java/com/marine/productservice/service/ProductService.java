package com.marine.productservice.service;

import com.example.appcommons.utility.ImportFileResponse;
import com.marine.productservice.DTO.CartDto;
import com.marine.productservice.DTO.CategoryDto;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.controller.ProductController;
import com.marine.productservice.entity.*;
import com.marine.productservice.exception.ProductAlreadyExistsException;
import com.marine.productservice.exception.ProductNotFoundException;
import com.marine.productservice.model.CartModel;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.ProductModel;
import com.marine.productservice.model.RFQModel;
import com.marine.productservice.paginaion.PaginationResponse;
import com.marine.productservice.repository.*;
import com.marine.productservice.util.DaoToDto;
import com.marine.productservice.util.DtoToDao;
import com.marine.productservice.util.ExcelGenerator;
import com.marine.productservice.util.PDFGenerator;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import jakarta.mail.util.ByteArrayDataSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DtoToDao dtoToDao;
    @Autowired
    private DaoToDto daoTodto;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PDFGenerator pdfGenerator;
    @Autowired
    private CompanyOrdersRepository companyOrdersRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ExcelGenerator excelGenerator;
    @Value("${to-email}")
    private String toEmail;
    Logger logger = LoggerFactory.getLogger(ProductController.class);


    //    public Page<ProductDto> getProductsByCategory(String categoryName, Pageable pageable) {
//        return productRepository.findByCategoryCategoryName(categoryName, pageable);
//    }
    public List<ProductDto> getAllProductDtos() {
        List<Products> productList = productRepository.findAll();
        List<ProductDto> dtoList = new ArrayList<>();
        Set<CategoryDto> categoryList = new HashSet<>();
     for (Products product : productList) {
            ProductDto productDto = new ProductDto();  // Create a new instance in each iteration
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(product.getCategory().getCategoryName());
            productDto.setProductCategory(categoryList);
            categoryList.add(categoryDto);
            dtoList.add(productDto);
        }
          return dtoList;
    }
    public List<ProductDto> getProductFromDb(List<Products> products) {
        List<ProductDto> dtoList = new ArrayList<>();
        Set<CategoryDto> categoryList = new HashSet<>();
        for (Products product : products) {
            ProductDto productDto = new ProductDto();  // Create a new instance in each iteration
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(product.getCategory().getCategoryName());
            productDto.setProductCategory(categoryList);
            categoryList.add(categoryDto);
            dtoList.add(productDto);
        }
        return dtoList;
    }
    public Page<Products> getProductsByCategoryName(String categoryName, Pageable pageable) {
        return productRepository.findByCategory_CategoryName(categoryName, pageable);

    }



    public ImportFileResponse importExcelToDb(MultipartFile file) {
        List<String> failedRecordPartNo = new ArrayList<>();
        List<String> successRecordPartNo = new ArrayList<>();
        Products productToSave = null;
        String partNumber = null;
        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                List<String> productNameList = new ArrayList<>();
                for (int i = firstRowNum + 1; i <= lastRowNum; i++) { // Start from second row (index 1)
                    Row row = sheet.getRow(i);
                    String productName = row.getCell(0) != null ? row.getCell(0).getStringCellValue(): "";
                    Cell cellimpaCode = row.getCell(1);
                    String impaCode = null;
                    if (cellimpaCode != null) {
                        if (cellimpaCode.getCellType() == CellType.NUMERIC) {
                            impaCode = String.valueOf(cellimpaCode.getNumericCellValue());
                        }
                    }
                   // String impaCode = row.getCell(1) != null ? row.getCell(1).getStringCellValue(): "";
                    String filter = row.getCell(2) != null ? row.getCell(2).getStringCellValue(): "";
                    String image = row.getCell(3) != null ? row.getCell(3).getStringCellValue(): "";
                    String productDescription = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
                    String sku = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
                    String equipment = row.getCell(6) != null ? row.getCell(6).getStringCellValue(): "";
                    String make = row.getCell(7) != null ? row.getCell(7).getStringCellValue(): "";
                    String model = row.getCell(8) != null ? row.getCell(8).getStringCellValue(): "";
                    partNumber = row.getCell(9) != null ? row.getCell(9).getStringCellValue(): "";
                    String specification = row.getCell(10) != null ? row.getCell(10).getStringCellValue(): "";
                    String coo = row.getCell(11) != null ? row.getCell(11).getStringCellValue(): "";
                    String categoryName = row.getCell(12) != null ? row.getCell(12).getStringCellValue(): "";
                    Cell cell = row.getCell(13);
                    String price = "";

                    if (cell != null) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            price = String.valueOf(cell.getNumericCellValue());
                        } else if (cell.getCellType() == CellType.STRING) {
                            price = cell.getStringCellValue();
                        }
                    }
                    Cell cellQuantity = row.getCell(14);
                    int quantity = 0;

                    if (cellQuantity != null) {
                        if (cellQuantity.getCellType() == CellType.NUMERIC) {
                            // Convert numeric cell -> int
                            quantity = (int) cellQuantity.getNumericCellValue();
                        }
                    }
                   // String quantity = row.getCell(14) != null ?  String.valueOf(row.getCell(12).getNumericCellValue()): "";
                    String hazodus = row.getCell(15) != null ? row.getCell(15).getStringCellValue(): "";

                    if(sku != null){
                      Products p =  productRepository.findBySku(sku);
                      if(p == null){
                    Products product = new Products();
                    product.setName(productName);
                    product.setImpaCode(impaCode);
                    product.setFilter(filter);
                    product.setImage(image);
                    product.setDescription(productDescription);
                    product.setSku(sku);
                    product.setEquipment(equipment);
                    product.setMake(make);
                    product.setModel(model);
                    product.setPartNumber(partNumber);
                    product.setSpecification(specification);
                    product.setCoo(coo);
                    product.setHazodus(hazodus);
                    product.setPrice(price);
                    ProductInventory productInven = new ProductInventory();
                    productInven.setQuantity(quantity);
                    checkCategory(categoryName,product);
                    Products productInventory = createInventory(quantity,product);
                    if (productInventory.getInventory() != null) {
                        inventoryRepository.save(productInventory.getInventory());
                    }
                    productRepository.save(product);
                    successRecordPartNo.add(partNumber);
                }
                    }
                }
                ImportFileResponse response = new ImportFileResponse();
                response.setMessage(successRecordPartNo.size() + " records saved successfully and " + failedRecordPartNo.size() +" records failed");
                response.setFailedRecords(String.join(", ", failedRecordPartNo));
                return response;
            } catch (IOException e) {
                failedRecordPartNo.add(partNumber);
                ImportFileResponse response = new ImportFileResponse();
                response.setMessage(successRecordPartNo + " records saved successfully and " + String.join(", ", failedRecordPartNo) + "failed");
                return response;
            }
        }
        return null;
    }

    private Products createInventory(Integer quantity, Products products) {
        if(quantity != null){
            LocalDate currentDate = LocalDate.now();
            products.setCreatedAt(currentDate);
            products.setModifiedAt(currentDate);
            if(products.getInventory() != null) {
                ProductInventory inventory = new ProductInventory();
                inventory.setCreatedAt(currentDate);
                inventory.setModifiedAt(currentDate);
                inventory.setQuantity(products.getInventory().getQuantity());
                products.setInventory(inventory);
            }
        }
        return products;
    }

    private void checkCategory(String categoryName, Products products) {
        if(categoryName != null) {
            ProductCategory productCategory = productCategoryRepository.findBycategoryName(categoryName);
            if (productCategory != null) {
                products.setCategory(productCategory);
            }
        }
    }

    public Products createProduct(@Valid ProductModel body) {
        Products product = null;
        if(body.getId() == null && body != null && body.getName() != null) {
            Products products = productRepository.findOneByName(body.getName());
            if(products != null) {
                throw new ProductAlreadyExistsException("Product Already Exist.");
            }
            if(body.getCategory() != null) {
                ProductCategory productCategory = productCategoryRepository.findBycategoryName(body.getCategory());
                if(productCategory == null) {
                    throw new ProductNotFoundException("Product Category not Exist.");
                }
                product = dtoToDao.saveProducts(body, productCategory);
            }
        }
        if(body.getId() != null) {
            Products products = productRepository.findOneById(body.getId());
            product = updateProducts(products, body);
        }
        return product;
    }

    private Products updateProducts(Products products, ProductModel body) {
        products.setName(body.getName());
        products.setImage(body.getImage());
        products.setDescription(body.getDescription());
        products.setSku(body.getSku());
        products.setPrice(body.getPrice());
        products.setEquipment(body.getEquipment());
        products.setMake(body.getMake());
        products.setModel(body.getModel());
        products.setPartNumber(body.getPartNumber());
        products.setSpecification(body.getSpecification());
        products.setCoo(body.getCoo());
        products.setHazodus(body.getHazodus());
        LocalDate currentDate = LocalDate.now();
        products.setModifiedAt(currentDate);
        if (products.getInventory() != null) {
            products.getInventory().setModifiedAt(currentDate);
            products.getInventory().setQuantity(body.getQuantity());
        }
        if(body.getCategory() != null) {
            ProductCategory productCategory = productCategoryRepository.findBycategoryName(body.getCategory());
            products.setCategory(productCategory);
        }
        return products;
    }

    public String createEnquiry(EnquiryModel enquiry) throws Exception {
        List<CartDto> productList = new ArrayList<>();
        List<RFQModel> rfqModelList = new ArrayList<>();
        List<CartDto> productInDB = new ArrayList<>();
// list contains non exist products in db
        List<CartDto> cartList = new ArrayList<>();
        if(enquiry.getCart() != null && !enquiry.getCart().isEmpty()) {
            cartList = checkCart(enquiry.getCart());
            productList.addAll(cartList);
        }
        if(enquiry.getRfq() != null && !enquiry.getRfq().isEmpty()) {
            for (RFQModel rfq : enquiry.getRfq()) {
                List<Products> products = productRepository.findByDescription(rfq.getDescription());
                if (products.isEmpty()) {
                    Products p = new Products();
                    p.setDescription(rfq.getDescription());
                    p.setPartNumber(rfq.getPartNumber());
                    p.setMake(rfq.getMake());
                    p.setModel(rfq.getModel());
                    CartDto dto = new CartDto();
                    dto.setProducts(p);
                    dto.setQuantity(rfq.getQuantity());
                    productList.add(dto);
                    rfqModelList.add(rfq);
                }
                for (Products prod : products) {
                    CartDto dto = new CartDto();
                    dto.setProducts(prod);
                    dto.setQuantity(rfq.getQuantity());
                    productList.add(dto);
                    productInDB.add(dto);
                }
            }
        }
        ClientEntity clientDetails = saveClientDetails(enquiry);
        String orderNumber = generateCompanyAndOrderID();
        // Generate PDF
        ByteArrayOutputStream byteArrayOutputStream = null;
            byteArrayOutputStream = pdfGenerator.generatePdf(enquiry,orderNumber);
            ByteArrayOutputStream excelContent= excelGenerator.generateExcels(productList, orderNumber, enquiry);
            SendEmail(toEmail, orderNumber,  byteArrayOutputStream,clientDetails,orderNumber, excelContent, "toTeam");
            SendEmail(enquiry.getEmail(), orderNumber,  byteArrayOutputStream,clientDetails,orderNumber, null, "toClient");
            saveClientOrders(productInDB,orderNumber,clientDetails );
        logger.debug("mail send successfully!!!");
        return orderNumber;
    }
    private void saveClientOrders(List<CartDto> productInDB,String orderNumber, ClientEntity clientDetails ) {
        List<OrderEntity> orderList = new ArrayList<>();
        try{
           for (CartDto product : productInDB) {
             orderList.add(dtoToDao.getOrderDetails(product.getProducts(),orderNumber,clientDetails));
           }
           orderRepository.saveAll(orderList);
        }catch(Exception e){
            throw e;
        }
    }
    private void saveOrderDetails(List<Products> productList, String orderNumber, ClientEntity clientDetailsToSave) {
        List<OrderEntity> orderList = new ArrayList<>();
        try{
            if (!productList.isEmpty()) {
                for (Products product : productList) {
                    orderList.add(dtoToDao.getOrderDetails(product,orderNumber,clientDetailsToSave));
                }
            } else {
//            ProductCategory productCategory = productCategoryRepository.findBycategoryName(body.getCategory().getCategoryName());
//            Products product = dtoToDao.saveProducts(body,productCategory);
            }
        }
        catch(Exception e){
            throw new IllegalArgumentException(e);
        }
        orderRepository.saveAll(orderList);
    }


    public void SendEmail(String to, String subject, ByteArrayOutputStream pdfContent, ClientEntity clientDetails, String orderNumber, ByteArrayOutputStream excelContent, String emailFlag) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String htmlContent = null;
        try {
            helper = new MimeMessageHelper(message, true);
            // Prepare template model
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("name", clientDetails.getFullName());
            templateModel.put("orderNumber", orderNumber);
            // Prepare Thymeleaf template
            Context context = new Context();
            context.setVariables(templateModel);
            if(emailFlag.equals("toTeam")) {
                htmlContent = templateEngine.process("RFQ-Ack-email", context);
            }else{
                htmlContent = templateEngine.process("RFQ-Ack-ClientEmail", context);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            helper.setTo(to);

        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // Add PDF as attachment
        helper.addAttachment(orderNumber+".pdf",  new ByteArrayDataSource(pdfContent.toByteArray(), "application/pdf"));
        if (excelContent != null) {
            DataSource dataSource = new ByteArrayDataSource(
                    excelContent.toByteArray(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            helper.addAttachment(orderNumber+".xlsx", dataSource);
        }
        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
    private ClientEntity saveClientDetails(EnquiryModel enquiry) {
            ClientEntity clientEntity = new ClientEntity();
            ClientEntity clientDetailsToSave = dtoToDao.createClientDetails(clientEntity, enquiry);
            clientRepository.save(clientDetailsToSave);
            return clientDetailsToSave;
    }
    private List<CartDto> checkCart(List<CartModel> carts) {
        List<CartDto> cartList = new ArrayList<>();
        for (CartModel cart : carts) {
            if (cart.getName() != null && cart.getMake() != null) {
                List<Products> products = productRepository.findByName(cart.getName());
                for (Products prod : products) {
                    CartDto dto = new CartDto();
                    dto.setProducts(prod);
                    dto.setQuantity(cart.getQuantity());
                    cartList.add(dto);
                }
            }
        }
        return cartList;
    }

    private String generateCompanyAndOrderID() {
        String orderNumber = null;
        try {
            CompanyOrdersEntity companyOrders = new CompanyOrdersEntity();
            companyOrders.setCompanyName("KMS");
            CompanyOrdersEntity companyOrder = companyOrdersRepository.save(companyOrders);
             orderNumber = "KMS-RFQ-" + LocalDate.now().getYear() + "-" + companyOrder.getId();
            companyOrders.setOrderNumber(orderNumber);
            companyOrdersRepository.save(companyOrders);
        }catch (Exception e){
            System.out.println(e);
        }
        return orderNumber;
    }


    public  Page<Products>  searchByProductDesc(String productDescription,Pageable pageable) {
        Page<Products> products = productRepository.findByDescriptionOrSKUContaining(productDescription,pageable);
        return products;
    }
    public  List<ProductModel>  searchByProductDescOrSKU(String productDescOrSku) {
        List<Products> products = null;
        try {
            if (productDescOrSku != null) {
                products = productRepository.findByDescriptionOrSKU(productDescOrSku);
                if (products == null) {
                    throw new ProductNotFoundException("Product not found.");
                }
            }
        }catch (Exception e){
            throw e;
        }
        List<ProductModel> productList = new ArrayList<>();
        for(Products prod: products) {
            productList.add(daoTodto.getProductFromDescOrSku(prod));
        }
        return productList;
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDto> getProductIdsByNames(List<String> productNames) {
        List<Products> products = productRepository.findByNames(productNames);
        return dtoToDao.getProductFromDb(products);

    }

    public PaginationResponse<ProductDto> getProductsByCategory(String categoryName, String searchText, Pageable pageable) {
        try {
        Page<Products> productPage = null;
        if(!categoryName.equals("search")) {
            // Get the page of products, which includes total count info
            productPage = getProductsByCategoryName(categoryName, pageable);
        } else if( !"null".equalsIgnoreCase(searchText) && !searchText.trim().isEmpty()){
            productPage = searchByProductDesc(searchText.toUpperCase(), pageable);
        }
            if (productPage == null) {
                return new PaginationResponse<>(Collections.emptyList(), 0L, 0, pageable.getPageNumber());
            }
        List<ProductDto> dtoProductList  = dtoToDao.getProductFromDb(productPage.getContent());
        // Get total elements and total pages from the Page object
        Long totalElements = productPage.getTotalElements();
        int totalPages = productPage.getTotalPages();
        return new PaginationResponse<>(dtoProductList,totalElements, totalPages, pageable.getPageNumber());
        } catch (Exception e) {
            e.printStackTrace(); // Log properly in real app
            return new PaginationResponse<>(Collections.emptyList(), 0L, 0, pageable.getPageNumber());
        }
    }

    public ProductDto getProductByIds(Long id) {
        Products products =  productRepository.findOneById(id);
        return dtoToDao.getProductDto(products);
    }

}
