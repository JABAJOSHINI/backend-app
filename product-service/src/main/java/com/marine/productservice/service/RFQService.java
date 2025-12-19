package com.marine.productservice.service;

import com.marine.productservice.DTO.*;
import com.marine.productservice.entity.ClientEntity;
import com.marine.productservice.entity.OrderEntity;
import com.marine.productservice.model.SupplierQuoteModel;
import com.marine.productservice.repository.OrderRepository;
import com.marine.productservice.util.DtoToDao;
import com.marine.productservice.util.ExcelGenerator;
import com.marine.productservice.util.PaginationFilters;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RFQService {
    @Autowired
    private DtoToDao dtoToDao;
    @Autowired
    private ExcelGenerator excelGenerator;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaginationFilters paginationFilters;
    public Page<OrderSummary> getAllRFQs(Pageable pageable) {
        return orderRepository.findGroupedOrders(pageable);
    }

    public RFQDto getRFQByOrderId(String orderId) {
        if(orderId != null) {
            List<OrderEntity> orderDetails =  orderRepository.findByRfqOrderId(orderId);
            if(orderDetails != null){
                RFQDto rfq = dtoToDao.getRFQOrderDetails(orderDetails);
                return rfq;
            }
        }
        return null;
    }

    public String getSupplierQuote(SupplierQuoteModel supplierQuotModel) throws IOException, MessagingException {
        if(supplierQuotModel != null){
            ByteArrayOutputStream excelContent= excelGenerator.generateSupplierExcels(supplierQuotModel);
            if(supplierQuotModel.getEmails().isEmpty()){

            }
            excelGenerator.SendEmail(supplierQuotModel.getEmails(),supplierQuotModel.getPurchaseQuoteId(),excelContent);
        }
        return null;
    }

    public List<PurchaseQuoteDto> reimportSupplierExcel(InputStream excelInputStream, String purchaseQuoteId) throws IOException {
        List<PurchaseQuoteDto> products = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream)) {
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
                PurchaseQuoteDto dto = new PurchaseQuoteDto();
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

    public Page<OrderEntity> getFilteredRecords(List<Filter> filters, Pageable pageable) {
        return orderRepository.findAll((root, query, cb) -> {
            // Join with clientEntity to access name/email
            Join<OrderEntity, ClientEntity> clientJoin = root.join("clientEntity", JoinType.LEFT);

            // Get final combined predicate from filter builder
            Predicate finalPredicate = paginationFilters.buildFilterPredicates(filters, root, query, cb);

            return finalPredicate; // return single predicate, not a collection
        }, pageable);
    }




    public List<OrderSummaryDTO> buildFilteredOrderDto(Page<OrderEntity> filterdOrders) {
        List<OrderSummaryDTO> dtoList = new ArrayList<>();
        for(OrderEntity rfq:  filterdOrders) {
            OrderSummaryDTO orderDto = new OrderSummaryDTO();  // Create a new instance in each iteration
            orderDto.setRFQOrderId(rfq.getRfqOrderId());
            ContactsDTO clientContact = dtoToDao.getClientDetails(rfq.getClientEntity());
            orderDto.setClientID(clientContact.getId());
            orderDto.setClientEmail(clientContact.getEmail());
            orderDto.setCreatedAt(rfq.getCreatedAt());
            dtoList.add(orderDto);
        }
        return dtoList;

    }
}
