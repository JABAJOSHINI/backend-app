package com.marine.supplierservice.service;

import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.DTO.RFQDto;
import com.marine.productservice.entity.OrderEntity;
import com.marine.supplierservice.dto.DaoToDto;
import com.marine.supplierservice.entity.SupplierDetailsEntity;
import com.marine.supplierservice.entity.SupplierQuoteEntity;
import com.marine.supplierservice.entity.SupplierQuoteItemEntity;
import com.marine.supplierservice.feignClient.ProductServiceClient;
import com.marine.supplierservice.model.SupplierImportFileModel;
import com.marine.supplierservice.model.SupplierQuotes;
import com.marine.supplierservice.repository.SupplierDetailsRepository;
import com.marine.supplierservice.repository.SupplierQuoteItemRepository;
import com.marine.supplierservice.repository.SupplierQuoteRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SupplierApiService {
    @Autowired
    private SupplierQuoteRepository supplierQuoteRepository;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private SupplierQuoteItemRepository supplierQuoteItemRepository;
    @Autowired
    private SupplierDetailsRepository supplierDetailsRepository;
    @Autowired
    private DaoToDto daoToDto;
    public List<SupplierImportFileModel> reimportSupplierExcel(InputStream inputStream, String purchaseQuoteId) throws
            IOException {
        List<SupplierImportFileModel> products = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            // Use the first sheet, regardless of its name
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'Purchase Quote' not found.");
            }
            int headerRowIndex = 17;
            int startRowIndex = headerRowIndex + 1;
            for (int rowIndex = startRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                SupplierImportFileModel dto = new SupplierImportFileModel();
                Cell slNo = row.getCell(0);
                Cell partNoCell = row.getCell(1);
                Cell nameCell = row.getCell(2);
                Cell descriptionCell = row.getCell(3);
                Cell qtyCell = row.getCell(4);
                Cell uomCell = row.getCell(5);
                Cell unitPriceCell = row.getCell(6);
                Cell totalPriceCell = row.getCell(7);
                Cell remarksCell = row.getCell(8);
                // ✅ If Sl.no is null or empty, stop processing
                if (slNo == null || slNo.getCellType() == CellType.BLANK) {
                    return products; // end of table
                }

                if (slNo.getCellType() == CellType.NUMERIC) {
                    dto.setId((long) slNo.getNumericCellValue());
                } else if (slNo.getCellType() == CellType.STRING) {
                    try {
                        dto.setId(Long.parseLong(slNo.getStringCellValue()));
                    } catch (NumberFormatException e) {
                        return products; // invalid Sl.no
                    }
                }

// ✅ Stop if ID is still null
                if (dto.getId() == null) {
                    return products;
                }
                if (partNoCell != null)
                    dto.setPartNumber(partNoCell.getStringCellValue());

                if (nameCell != null)
                    dto.setName(nameCell.getStringCellValue());

                if (descriptionCell != null)
                    dto.setDescription(descriptionCell.getStringCellValue());

                if (qtyCell != null && qtyCell.getCellType() == CellType.NUMERIC)
                    dto.setQuantity((int) qtyCell.getNumericCellValue());

                if (uomCell != null)
                    dto.setUom(uomCell.getStringCellValue());
                if (unitPriceCell != null && unitPriceCell.getCellType() == CellType.NUMERIC)
                    dto.setUnitPrice(unitPriceCell.getNumericCellValue());

                if (totalPriceCell != null && totalPriceCell.getCellType() == CellType.NUMERIC)
                    dto.setTotalPrice(totalPriceCell.getNumericCellValue());

                if (remarksCell != null)
                    dto.setRemarks(remarksCell.getStringCellValue());

                // Optional: Skip completely blank rows
                if (dto.getPartNumber() == null && dto.getName() == null && dto.getDescription() == null)
                    continue;

                products.add(dto);
            }
        }

        return products;
    }

    public ResponseEntity<Map<String, String>> ImportAndSaveSupplierExcel(MultipartFile file, String purchaseQuoteId, String email, String company, String phone) throws IOException {
        List<SupplierImportFileModel> productList = reimportSupplierExcel(file.getInputStream(), purchaseQuoteId);
        SupplierDetailsEntity supplierDetails = saveSupplierDetails(email, company, phone);
        SupplierQuoteEntity supplierQuote = saveSupplierQuote(purchaseQuoteId, supplierDetails);
        saveSupplierQuoteItems(purchaseQuoteId, productList, supplierQuote);
        Map<String, String> result = new HashMap<>();
        result.put("quoteId", supplierQuote.getQuoteId());
        return ResponseEntity.ok(result);
    }

    private SupplierDetailsEntity saveSupplierDetails(String email, String company, String phone) {
        SupplierDetailsEntity supplierDetails = new SupplierDetailsEntity();
        supplierDetails.setEmail(email);
        supplierDetails.setCompany(company);
        supplierDetails.setPhone(phone);
        return supplierDetailsRepository.save(supplierDetails);
    }

    private void saveSupplierQuoteItems(String purchaseQuoteId, List<SupplierImportFileModel> productList, SupplierQuoteEntity supplierQuote) {
        List<SupplierQuoteItemEntity> SupplierQuoteItemEntityList = new ArrayList<>();
        List<String> productNameList = productList.stream()
                .map(SupplierImportFileModel::getName)
                .collect(Collectors.toList());

        List<ProductDto> products = productServiceClient.getProductIdsByNames(productNameList);

        // Validation: Ensure all product names exist
        List<String> foundProductNames = products.stream()
                .map(ProductDto::getName)
                .collect(Collectors.toList());

        for (String name : productNameList) {
            if (!foundProductNames.contains(name)) {
                throw new RuntimeException("Product not found: " + name);
            }
        }

        // Optional: Ensure sizes match (but not strictly necessary if mapping works by name)
        if (products.size() != productList.size()) {
            throw new RuntimeException("Mismatch in product list sizes from file and product service.");
        }
        SupplierQuoteItemEntity item = null;
        for (ProductDto product : products) {
            // Find matching entry from import file by product name
            SupplierImportFileModel matchingImport = productList.stream()
                    .filter(p -> p.getName().equals(product.getName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Matching import entry not found for product: " + product.getName()));

            item = new SupplierQuoteItemEntity();
            item.setSupplierQuoteId(supplierQuote);
            item.setProductId(product.getId());
            item.setQuantity(matchingImport.getQuantity());
            item.setUnitPrice(matchingImport.getUnitPrice());
            item.setTotalPrice(matchingImport.getTotalPrice());
            SupplierQuoteItemEntityList.add(item);
        }
        supplierQuoteItemRepository.save(item); // or accumulate in list and saveAll

    }


    private SupplierQuoteEntity saveSupplierQuote( String purchaseQuoteId, SupplierDetailsEntity supplierDetails) {
        SupplierQuoteEntity supplierQuote = new SupplierQuoteEntity();
        supplierQuote.setQuoteId(purchaseQuoteId);
        supplierQuote.setStatus("ACTIVE");
        LocalDate currentDate = LocalDate.now();
        LocalDate validUntilDate = currentDate.plusDays(20);
        supplierQuote.setValidUntil(validUntilDate);
        supplierQuote.setCreatedAt(currentDate);
        supplierQuote.setSupplierDetails(supplierDetails);
        supplierQuoteRepository.save(supplierQuote);
        return supplierQuote;
    }


    public Page<SupplierQuotes> getAllSupplierPurchaseQuote(Pageable pageable) {
        return supplierQuoteRepository.findAllSupplierPurchaseQuote(pageable);
    }

    public RFQDto getSupplierOrdersByQuoteId(String quoteId) {
        if(quoteId != null) {
            List<SupplierQuoteEntity> orderDetails =  supplierQuoteRepository.findByQuoteId(quoteId);
            if(orderDetails != null){
                RFQDto rfq = daoToDto.getSupplierQuoteDetails(orderDetails);
                return rfq;
            }
        }
        return null;
    }
}
