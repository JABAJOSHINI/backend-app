package com.marine.productservice.util;

import com.marine.productservice.DTO.CartDto;
import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.DTO.RFQDto;
import com.marine.productservice.entity.ClientEntity;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.SupplierQuoteModel;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelGenerator {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    public void SendEmail(List<String> emails, String orderNumber, ByteArrayOutputStream excelContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        String htmlContent;

        try {
            helper = new MimeMessageHelper(message, true);

            // Prepare template model
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("PurchaseOrderId", orderNumber);

            // Prepare Thymeleaf template
            Context context = new Context();
            context.setVariables(templateModel);
            htmlContent = templateEngine.process("RFQ-Ack-email", context);

            // ✅ Set multiple recipients
            String[] toArray = emails.toArray(new String[0]);
            helper.setTo(toArray);
            helper.setCc("jabajoshini85@gmail.com");
            helper.setSubject("Purchase Quote");
            helper.setText(htmlContent, true);

            // ✅ Add Excel attachment (if present)
            if (excelContent != null) {
                DataSource dataSource = new ByteArrayDataSource(
                        excelContent.toByteArray(),
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );
                helper.addAttachment(orderNumber + ".xlsx", dataSource);
            }

            // ✅ Send the email
            mailSender.send(message);

        } catch (MailAuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }

    public ByteArrayOutputStream generateSupplierExcels(SupplierQuoteModel supplierQuotModel) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("ads");

//        XSSFSheet sheet = workbook.createSheet(supplierQuotModel.getPurchaseQuoteId());

        sheet.setColumnWidth(0, 8000); // Logo + Address
        sheet.setColumnWidth(6, 4000); // RFQ Details
        sheet.setColumnWidth(7, 6000); // RFQ Values
        // Hide Gridlines
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        // ✅ Define a Border Style for Outer Borders
        CellStyle outerBorderStyle = workbook.createCellStyle();
        outerBorderStyle.setBorderTop(BorderStyle.THIN);
        outerBorderStyle.setBorderBottom(BorderStyle.THIN);
        outerBorderStyle.setBorderLeft(BorderStyle.THIN);
        outerBorderStyle.setBorderRight(BorderStyle.THIN);
        // -------------------------------------------
        // 🔹 Merge Cells for Logo (A1:C6) & RFQ Details (D1:H6)
        sheet.addMergedRegion(new CellRangeAddress(0, 9, 0, 2)); // Logo region expanded
        sheet.addMergedRegion(new CellRangeAddress(0, 6, 6, 6)); // RFQ details region
        sheet.addMergedRegion(new CellRangeAddress(0, 6, 7, 7)); // RFQ details region
        // 🔹 Merge Cells for Address (A7:H12) - Adjusted to match rows
        sheet.addMergedRegion(new CellRangeAddress(10, 16, 0, 7));

        // -------------------------------------------
       // 🔹 Add Logo (Left Side)
        // ✅ Ensure Rows Exist and Set Height for Proper Logo Display
        for (int i = 0; i <= 9; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            row.setHeightInPoints(20); // Reduced height to control logo size
        }

        // ✅ Add Logo
        try (InputStream logoStream = new URL("https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png").openStream()) {
            byte[] logoBytes = logoStream.readAllBytes();
            int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG); // PNG for better quality

            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();
            logoAnchor.setCol1(0);
            logoAnchor.setRow1(0);
            logoAnchor.setCol2(2); // Expanded width
            logoAnchor.setRow2(8); // Expanded height to match merged region
            Picture picture = drawing.createPicture(logoAnchor, logoIndex);
            picture.resize(1.1); // Increased scaling factor for better visibility
        }
// -------------------------------------------
// 🔹 Add RFQ Details (Right Side)
// -------------------------------------------
        Row rfqRow = sheet.getRow(0) != null ? sheet.getRow(0) : sheet.createRow(0);
        Cell rfqCell = rfqRow.createCell(6);
        Cell rfqValueCell = rfqRow.createCell(7);
        // Define Fonts
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 12);
        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 12);

        // Create a Rich Text String to Apply Different Fonts
        XSSFRichTextString rfqText = new XSSFRichTextString();
        XSSFRichTextString rfqValueText = new XSSFRichTextString();
        // Format Labels and Values for Proper Alignment
        String[][] details = {
                {"RFQ No\n", supplierQuotModel.getPurchaseQuoteId()},
                {"Customer RFQ No\n", "N/A"},
                {"Date\n", LocalDate.now().toString()}
        };

        // Append Labels (Bold) and Values (Normal) Properly
        for (String[] detail : details) {
            //   String formattedLabel = String.format("%-15s ", detail[0]); // Ensures proper spacing
            rfqText.append(detail[0], (XSSFFont) boldFont);
            rfqValueText.append(": " + detail[1] + "\n", (XSSFFont) normalFont);
        }

        // Set the formatted text in the cell
        rfqCell.setCellValue(rfqText);
        rfqValueCell.setCellValue(rfqValueText);

        // Apply Styling to the Cell
        CellStyle rfqStyle = workbook.createCellStyle();
        rfqStyle.setWrapText(true); // Allow multiline text
        rfqStyle.setAlignment(HorizontalAlignment.LEFT); // Align to left
        rfqStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        rfqCell.setCellStyle(rfqStyle);
        rfqValueCell.setCellStyle(rfqStyle);

// -------------------------------------------
// 🔹 Add Address (Below Logo & RFQ)
// -------------------------------------------
        Row addressRow = sheet.createRow(10); // Adjusted to match merged region
        Cell addressCell = addressRow.createCell(0);
        String addressValue = "KAUFERKART\n" +
                "Block B, Office B24-041\n" +
                "Sharjah Research Technology & Innovation Park\n" +
                "Sharjah, United Arab Emirates\n" +
                "+971 568976712 (WhatsApp only)\n" +
                "www.kauferkart.com\n\n" ;

        addressCell.setCellValue(addressValue);
        CellStyle addressStyle = workbook.createCellStyle();
        addressStyle.setWrapText(true);
        addressStyle.setVerticalAlignment(VerticalAlignment.TOP);
        addressStyle.setAlignment(HorizontalAlignment.LEFT);
        Font addressFont = workbook.createFont();
        addressFont.setFontHeightInPoints((short) 12);
        addressStyle.setFont(addressFont);
        addressCell.setCellStyle(addressStyle);

// Auto-size Columns for Better Visibility
        for (int i = 0; i <= 7; i++) {
            sheet.autoSizeColumn(i);
        }
//-------------------------------------------------
// 🔹 Add Table Below Address
//-------------------------------------------------

// ✅ Define Fixed Column Widths for Table
        int[] columnWidths = {2000,  6000, 12000,12000, 3000, 4000, 5000, 6000, 3000}; // Width in Excel units

// ✅ Define Table Headers
        String[] headers = {"Sl.no", "Part No/Dwg No", "Name","Description","Qty", "Unit", "Price/Pc", "Total Price", "Remarks"};
        int numColumns = headers.length;

// ✅ Apply Fixed Widths to Table Columns
        for (int col = 0; col < numColumns; col++) {
            sheet.setColumnWidth(col, columnWidths[col]); // Apply fixed width
        }

        int tableStartRow = 17; // Start table below the address section

// ✅ Define Styles for Headers and Data
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

// ✅ Define Style for Data Cells (Normal Font)
        CellStyle dataStyle = workbook.createCellStyle();
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
// Locked (non-editable)
        CellStyle lockedStyle = workbook.createCellStyle();
        lockedStyle.setLocked(true);
        lockedStyle.cloneStyleFrom(dataStyle); // preserve existing borders and font
// ✅ Create Table Header Row
        Row tableHeaderRow = sheet.createRow(tableStartRow);
        for (int col = 0; col < numColumns; col++) {
            Cell cell = tableHeaderRow.createCell(col);
            cell.setCellValue(headers[col]);
            cell.setCellStyle(headerStyle);
        }

// ✅ Fill Table Data
        int slno = 1;
        int rowIndex = tableStartRow + 1; // Start below the header row
        for (ProductDto dto : supplierQuotModel.getRfq().getProducts()) {
            Row row = sheet.createRow(rowIndex++);

            // Retrieve Data
            String slNo = String.valueOf(slno++);
            String description = (dto.getDescription() != null && dto.getDescription() != null)
                    ? dto.getDescription()
                    : "N/A";
            String partNumber = ( dto.getPartNumber() != null)
                    ? dto.getPartNumber()
                    : "N/A";
            String name = (dto.getName() != null)
                    ? dto.getName()
                    : "N/A";
            // Create Cells with Data Style
            row.createCell(0).setCellValue(slNo);
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(0).setCellStyle(lockedStyle);

            row.createCell(1).setCellValue(partNumber);
            row.getCell(1).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(lockedStyle);

            row.createCell(2).setCellValue(name);
            row.getCell(2).setCellStyle(dataStyle);
            row.getCell(2).setCellStyle(lockedStyle);

            row.createCell(3).setCellValue(description);
            row.getCell(3).setCellStyle(dataStyle);
            row.getCell(3).setCellStyle(lockedStyle);

            row.createCell(4).setCellValue("");
            row.getCell(4).setCellStyle(dataStyle);

            row.createCell(5).setCellValue("");
            row.getCell(5).setCellStyle(dataStyle);

            row.createCell(6).setCellValue("");
            row.getCell(6).setCellStyle(dataStyle);

            row.createCell(7).setCellValue("");
            row.getCell(7).setCellStyle(dataStyle);

            row.createCell(8).setCellValue("");
            row.getCell(8).setCellStyle(dataStyle);
        }
        sheet.protectSheet("");
// -------------------------------------------
// 🔹 Add TOTAL & DISCOUNT Details (Right Side)
// -------------------------------------------
        int totalRowIndex = rowIndex; // Row after the last product
        CellStyle amountStyle = workbook.createCellStyle();
        Font amountHeadingFont = workbook.createFont();
        amountHeadingFont.setBold(true);
        String[] labels = {"EXW : UAE", "TOTAL AMOUNT     :", "DISCOUNT (5%)      :", "GRAND TOTAL     :"};
        for (int i = 0; i < labels.length; i++) {
            Row row = sheet.createRow(totalRowIndex++);
            Cell labelCell = row.createCell(5);
            labelCell.setCellValue(labels[i]);
            labelCell.setCellStyle(amountStyle);
        }
        // Ensure rows exist up to this point
        for (int i = tableStartRow; i <= totalRowIndex + 2; i++) {
            if (sheet.getRow(i) == null) sheet.createRow(i);
        }

        // ✅ Apply Outer Borders for Merged Cells
        applyOuterBorder(sheet, 0, totalRowIndex + 2, 0, 9, workbook); // Logo Section

// -------------------------------------------
// 🔹 Write the Excel File
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }

    public ByteArrayOutputStream generateExcel(List<CartDto> productList, String orderNumber) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Sheet1");
            // Set up column widths and row heights
            sheet.setColumnWidth(0, 9000); // Increase width for logo
            sheet.setColumnWidth(1, 6000);

// Set row heights to ensure enough space for the logo
            for (int i = 0; i <= 8; i++) { // Ensure rows exist before setting height
                Row row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                row.setHeightInPoints(80); // Adjust height for better image fit
            }

// Merge cells for logo placement
            sheet.addMergedRegion(new CellRangeAddress(0, 8, 0, 3)); // Merging a larger area for the image

// Add the logo
            URL logoUrl = new URL("https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png");
            try (InputStream logoStream = logoUrl.openStream()) {
                byte[] logoBytes = IOUtils.toByteArray(logoStream);
                int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG); // Use PNG for better quality

                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();

                // Set anchor to define logo position
                logoAnchor.setCol1(0);
                logoAnchor.setRow1(0);
                logoAnchor.setCol2(4); // Expand width
                logoAnchor.setRow2(8); // Expand height

                Picture logoPicture = drawing.createPicture(logoAnchor, logoIndex);
                logoPicture.resize(2.5); // Increase scale to make the image larger
            }
            Row headerRow = sheet.createRow(0);

//            XSSFSheet sheet = workbook.createSheet("Sheet1");
//            // Set up column widths
//            sheet.setColumnWidth(0, 6000); // For logo
//            sheet.setColumnWidth(1, 6000); // For name and date
//            // Add a row for the header
//            Row headerRow = sheet.createRow(0);
//            // Merge cells for logo on the left side
//            sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 5));
//            // Add the logo
//            URL logoUrl = new URL("https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png");
//            try (InputStream logoStream = logoUrl.openStream()) {
//                byte[] logoBytes = IOUtils.toByteArray(logoStream);
//                int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_JPEG);
//
//                Drawing<?> drawing = sheet.createDrawingPatriarch();
//                ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();
//                // Set anchor to cover more cells
//                logoAnchor.setCol1(0);
//                logoAnchor.setRow1(0);
//                logoAnchor.setCol2(3); // Expand width
//                logoAnchor.setRow2(9); // Expand height
//                Picture logoPicture = drawing.createPicture(logoAnchor, logoIndex);
//                logoPicture.resize(2.5);
//            }
            // ADDRESS Create a new row below the logo for the address
            Row addressRow = sheet.createRow(5); // Move text to a new row
            addressRow.setHeightInPoints(80); // Set row height for proper display
            Cell addressCell = addressRow.createCell(0);
            CellStyle addressStyle = workbook.createCellStyle();
            addressStyle.setWrapText(true);  // Enable text wrapping
            addressStyle.setVerticalAlignment(VerticalAlignment.TOP); // Align to top
            addressStyle.setBorderTop(BorderStyle.NONE);
            addressCell.setCellStyle(addressStyle);
            String addressValue = "KAUFERKART\n" +
                    "Block B, Office B24-041\n" +
                    "Sharjah Research Technology & Innovation Park\n" +
                    "Sharjah, United Arab Emirates\n" +
                    "+971 568976712 (WhatsApp only)\n" +
                    "www.kauferkart.com\n" +
                    "\n" +
                    "To:" + "company name" + "\n" +
                    "Name" + "name" + "\n" +
                    "Address:" + "address" + "\n" +
                    "\n" +
                    "Dear Sir/Madam,\n" +
                    "Thanks for your enquiry\n" +
                    "We are pleased to send you our price as below\n" +
                    "\n";
            addressCell.setCellValue(addressValue);
            // Merge address across columns (align it with the logo)
            sheet.addMergedRegion(new CellRangeAddress(5, 14, 0, 7));


            // RIGHT SIDE DETAILS Add name and date on the right side
            sheet.addMergedRegion(new CellRangeAddress(0, 4, 6, 7));
            // Create **Bold Style** for Labels
            CellStyle labelStyle = workbook.createCellStyle();
            Font labelFont = workbook.createFont();
            labelFont.setBold(true);
            labelFont.setFontHeightInPoints((short) 12);
            labelStyle.setFont(labelFont);
            labelStyle.setAlignment(HorizontalAlignment.LEFT);

// Create **Normal Style** for Values
            CellStyle valueStyle = workbook.createCellStyle();
            valueStyle.setAlignment(HorizontalAlignment.LEFT);
            int startRow = 0;
            String quotationcell = "QUOTATION\n";
            // Array of labels and their corresponding values
            String[][] details = {
                    {"Date", ": " + java.time.LocalDate.now().toString()},
                    {"Ref no", ": " + orderNumber},
                    {"Customer RFQ no", ": " + ""},
                    {"Valid Until", ": " + java.time.LocalDate.now().plusDays(30).toString()},
                    {"Vessel", ": " + "MV Example"}
            };

            for (int i = 0; i < details.length; i++) {
                Row row = sheet.createRow(startRow + i);
                // First cell (label)
                Cell labelCell = row.createCell(6);
                labelCell.setCellValue(details[i][0]);
                labelCell.setCellStyle(labelStyle);
                // Second cell (value)
                Cell valueCell = row.createCell(7);
                valueCell.setCellValue(details[i][1]);
                valueCell.setCellStyle(valueStyle);
            }

            // Auto-size columns for better visibility
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            // Adjust row height for header row
            headerRow.setHeightInPoints(50);
            // Define a border style
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Add a table below the header
            int tableStartRow = 15; // Start table from row 15
            Row tableHeaderRow = sheet.createRow(tableStartRow);

            String[] headers = {"Sl.no", "Description", "Part No/Dwg No", "Qty", "Unit", "Price/Pc", "Total Price", "Remarks"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = tableHeaderRow.createCell(i);
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                borderStyle.setFont(headerFont);
                cell.setCellStyle(borderStyle);
                cell.setCellValue(headers[i]);
            }

            int rowIndex = tableStartRow + 1; // Start below the header row
            for (CartDto dto : productList) {
                Row row = sheet.createRow(rowIndex++);
                String slNo = (dto.getProducts() != null && dto.getProducts().getDescription() != null)
                        ? dto.getProducts().getDescription()
                        : "N/A";
                String description = (dto.getProducts() != null && dto.getProducts().getDescription() != null)
                        ? dto.getProducts().getDescription()
                        : "N/A";
                String partNumber = (dto.getProducts() != null && dto.getProducts().getPartNumber() != null)
                        ? dto.getProducts().getPartNumber()
                        : "N/A";
                String qty = (dto.getProducts() != null && dto.getProducts().getMake() != null)
                        ? dto.getProducts().getMake()
                        : "N/A";
                String unit = (dto.getProducts() != null && dto.getProducts().getModel() != null)
                        ? dto.getProducts().getModel()
                        : "N/A";
                String price = (dto.getQuantity() != null)
                        ? String.valueOf(dto.getQuantity())
                        : "N/A";
                String totalPrice = (dto.getProducts() != null && dto.getProducts().getModel() != null)
                        ? dto.getProducts().getModel()
                        : "N/A";
                String remarks = (dto.getQuantity() != null)
                        ? String.valueOf(dto.getQuantity())
                        : "N/A";
                // Create cells with borders
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(slNo);
                cell0.setCellStyle(borderStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(description);
                cell1.setCellStyle(borderStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(partNumber);
                cell2.setCellStyle(borderStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(qty);
                cell3.setCellStyle(borderStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(unit);
                cell4.setCellStyle(borderStyle);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(price);
                cell5.setCellStyle(borderStyle);

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(totalPrice);
                cell6.setCellStyle(borderStyle);

                Cell cell7 = row.createCell(7);
                cell7.setCellValue(remarks);
                cell7.setCellStyle(borderStyle);
            }

//// Auto-size columns for better visibility
//            for (int i = 0; i <= 7; i++) {
//                sheet.autoSizeColumn(i);
//            }
            int totalRowIndex = rowIndex; // Row after the last product

// Get the row index where the product list ends
            // Define labels for total, discount, and grand total
            String[] labels = {"EXW : UAE", "TOTAL AMOUNT     :", "DISCOUNT (5%)      :", "GRAND TOTAL     :"};
            //    double[] values = {0, 0, 12, 12}; // Adjust values accordingly

// Create total, discount, and grand total rows dynamically
            for (int i = 0; i < labels.length; i++) {
                Row row = sheet.createRow(totalRowIndex++);
                Cell labelCell = row.createCell(5); // Column F for labels
                labelCell.setCellValue(labels[i]);
                labelCell.setCellStyle(borderStyle);

//                if (i > 1) { // Apply values only for Discount & Grand Total
//                    Cell valueCell = row.createCell(6); // Column G for values
//                    valueCell.setCellValue(values[i]);
//                    valueCell.setCellStyle(borderStyle);
//                }
            }

            workbook.write(outputStream);
        }
        return outputStream;
    }

    //    XSSFSheet sheet = workbook.createSheet("Sheet1");
//    // Set up column widths
//            sheet.setColumnWidth(0, 6000); // For logo
//            sheet.setColumnWidth(1, 6000); // For name and date
//    // Add a row for the header
//    Row headerRow = sheet.createRow(0);
//    // Merge cells for logo on the left side
//            sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 5));
//    // Add the logo
//    URL logoUrl = new URL("https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png");
//            try (InputStream logoStream = logoUrl.openStream()) {
//        byte[] logoBytes = IOUtils.toByteArray(logoStream);
//        int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_JPEG);
//
//        Drawing<?> drawing = sheet.createDrawingPatriarch();
//        ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();
//        logoAnchor.setCol1(0);
//        logoAnchor.setRow1(0);
//        logoAnchor.setCol2(1);
//        logoAnchor.setRow2(4);
//        Picture logoPicture = drawing.createPicture(logoAnchor, logoIndex);
//        logoPicture.resize(1);
//    }
    public ByteArrayOutputStream generateExcels(List<CartDto> productList, String orderNumber, EnquiryModel enquiry) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("RFQ Details");

        sheet.setColumnWidth(0, 8000); // Logo + Address
        sheet.setColumnWidth(6, 4000); // RFQ Details
        sheet.setColumnWidth(7, 6000); // RFQ Values
        // Hide Gridlines
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        // ✅ Define a Border Style for Outer Borders
        CellStyle outerBorderStyle = workbook.createCellStyle();
        outerBorderStyle.setBorderTop(BorderStyle.THIN);
        outerBorderStyle.setBorderBottom(BorderStyle.THIN);
        outerBorderStyle.setBorderLeft(BorderStyle.THIN);
        outerBorderStyle.setBorderRight(BorderStyle.THIN);

// ✅ Apply Outer Borders for Merged Cells
        applyOuterBorder(sheet, 0, 26, 0, 7, workbook); // Logo Section
// -------------------------------------------
// 🔹 Merge Cells for Logo (A1:C6) & RFQ Details (D1:H6)
        sheet.addMergedRegion(new CellRangeAddress(0, 9, 0, 2)); // Logo region expanded
        sheet.addMergedRegion(new CellRangeAddress(0, 6, 6, 6)); // RFQ details region
        sheet.addMergedRegion(new CellRangeAddress(0, 6, 7, 7)); // RFQ details region
// 🔹 Merge Cells for Address (A7:H12) - Adjusted to match rows
        sheet.addMergedRegion(new CellRangeAddress(10, 26, 0, 7));

// -------------------------------------------
// 🔹 Add Logo (Left Side)
//        Row logoRow = sheet.createRow(0);
//        Cell logoCell = logoRow.createCell(0);
//        logoCell.setCellValue(""); // Placeholder for the logo
//
//        try (InputStream logoStream = new URL("https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png").openStream()) {
//            byte[] logoBytes = logoStream.readAllBytes();
//            int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
//
//            Drawing<?> drawing = sheet.createDrawingPatriarch();
//            ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();
//            logoAnchor.setCol1(0);
//            logoAnchor.setRow1(0);
//            logoAnchor.setCol2(2);
//            logoAnchor.setRow2(8); // Adjusted to match merged region
//            Picture picture = drawing.createPicture(logoAnchor, logoIndex);
//            picture.resize(1); // Increased scale for proper display
//        }
        // ✅ Ensure Rows Exist and Set Height for Proper Logo Display
        for (int i = 0; i <= 9; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            row.setHeightInPoints(20); // Reduced height to control logo size
        }

// ✅ Add Logo
        try (InputStream logoStream = new URL("https://cdn.pixabay.com/photo/2014/04/10/11/24/rose-320868_960_720.jpg").openStream()) {
            byte[] logoBytes = logoStream.readAllBytes();
            int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG); // PNG for better quality

            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor logoAnchor = workbook.getCreationHelper().createClientAnchor();
            logoAnchor.setCol1(0);
            logoAnchor.setRow1(0);
            logoAnchor.setCol2(2); // Expanded width
            logoAnchor.setRow2(8); // Expanded height to match merged region

            Picture picture = drawing.createPicture(logoAnchor, logoIndex);
            picture.resize(1.1); // Increased scaling factor for better visibility
        }
// -------------------------------------------
// 🔹 Add RFQ Details (Right Side)
// -------------------------------------------

        Row rfqRow = sheet.getRow(0) != null ? sheet.getRow(0) : sheet.createRow(0);
        Cell rfqCell = rfqRow.createCell(6);
        Cell rfqValueCell = rfqRow.createCell(7);
        // Define Fonts
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 12);
        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 12);

        // Create a Rich Text String to Apply Different Fonts
        XSSFRichTextString rfqText = new XSSFRichTextString();
        XSSFRichTextString rfqValueText = new XSSFRichTextString();
        // Format Labels and Values for Proper Alignment
        String[][] details = {
                {"Vessel\n", enquiry.getVessel() != null ? enquiry.getVessel() : "NA"},
                {"RFQ No\n", orderNumber},
                {"Customer RFQ No\n", "N/A"},
                {"Date\n", LocalDate.now().toString()},
                {"Valid Until\n", LocalDate.now().plusDays(30).toString()}
        };

        // Append Labels (Bold) and Values (Normal) Properly
        for (String[] detail : details) {
            //   String formattedLabel = String.format("%-15s ", detail[0]); // Ensures proper spacing
            rfqText.append(detail[0], (XSSFFont) boldFont);
            rfqValueText.append(": " + detail[1] + "\n", (XSSFFont) normalFont);
        }

        // Set the formatted text in the cell
        rfqCell.setCellValue(rfqText);
        rfqValueCell.setCellValue(rfqValueText);

        // Apply Styling to the Cell
        CellStyle rfqStyle = workbook.createCellStyle();
        rfqStyle.setWrapText(true); // Allow multiline text
        rfqStyle.setAlignment(HorizontalAlignment.LEFT); // Align to left
        rfqStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        rfqCell.setCellStyle(rfqStyle);
        rfqValueCell.setCellStyle(rfqStyle);

// -------------------------------------------
// 🔹 Add Address (Below Logo & RFQ)
// -------------------------------------------
        Row addressRow = sheet.createRow(10); // Adjusted to match merged region
        Cell addressCell = addressRow.createCell(0);
        String addressValue = "KAUFERKART\n" +
                "Block B, Office B24-041\n" +
                "Sharjah Research Technology & Innovation Park\n" +
                "Sharjah, United Arab Emirates\n" +
                "+971 568976712 (WhatsApp only)\n" +
                "www.kauferkart.com\n\n" +
                "To: " + enquiry.getCompanyName() + "\n" +
                "Name:" + enquiry.getFullName() + "\n" +
                "Address: " + enquiry.getCompanyAddress() + "\n\n" +
                "Dear Sir/Madam,\n" +
                "Thanks for your enquiry.\n" +
                "We are pleased to send you our price as below.\n";

        addressCell.setCellValue(addressValue);
        CellStyle addressStyle = workbook.createCellStyle();
        addressStyle.setWrapText(true);
        addressStyle.setVerticalAlignment(VerticalAlignment.TOP);
        addressStyle.setAlignment(HorizontalAlignment.LEFT);
        Font addressFont = workbook.createFont();
        addressFont.setFontHeightInPoints((short) 12);
        addressStyle.setFont(addressFont);
        addressCell.setCellStyle(addressStyle);

// Auto-size Columns for Better Visibility
        for (int i = 0; i <= 7; i++) {
            sheet.autoSizeColumn(i);
        }
//-------------------------------------------------
// 🔹 Add Table Below Address
//-------------------------------------------------

// ✅ Define Fixed Column Widths for Table
        int[] columnWidths = {2000,  6000, 12000,12000, 3000, 3000, 4000, 5000, 6000}; // Width in Excel units

// ✅ Define Table Headers
        String[] headers = {"Sl.no", "Part No/Dwg No","Description","Qty", "Unit", "Price/Pc", "Total Price", "Remarks"};
        int numColumns = headers.length;

// ✅ Apply Fixed Widths to Table Columns
        for (int col = 0; col < numColumns; col++) {
            sheet.setColumnWidth(col, columnWidths[col]); // Apply fixed width
        }

        int tableStartRow = 27; // Start table below the address section

// ✅ Define Styles for Headers and Data
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

// ✅ Define Style for Data Cells (Normal Font)
        CellStyle dataStyle = workbook.createCellStyle();
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

// ✅ Create Table Header Row
        Row tableHeaderRow = sheet.createRow(tableStartRow);
        for (int col = 0; col < numColumns; col++) {
            Cell cell = tableHeaderRow.createCell(col);
            cell.setCellValue(headers[col]);
            cell.setCellStyle(headerStyle);
        }

// ✅ Fill Table Data
        int slno = 1;
        int rowIndex = tableStartRow + 1; // Start below the header row
        for (CartDto dto : productList) {
            Row row = sheet.createRow(rowIndex++);

            // Retrieve Data
            String slNo = String.valueOf(slno++);
            String description = (dto.getProducts() != null && dto.getProducts().getDescription() != null)
                    ? dto.getProducts().getDescription()
                    : "N/A";
            String partNumber = (dto.getProducts() != null && dto.getProducts().getPartNumber() != null)
                    ? dto.getProducts().getPartNumber()
                    : "N/A";
            String qty = (dto.getQuantity() != null)
                    ? String.valueOf(dto.getQuantity())
                    : "N/A";

            // Create Cells with Data Style
            row.createCell(0).setCellValue(slNo);
            row.getCell(0).setCellStyle(dataStyle);

            row.createCell(1).setCellValue(partNumber);
            row.getCell(1).setCellStyle(dataStyle);

            row.createCell(2).setCellValue(description);
            row.getCell(2).setCellStyle(dataStyle);

            row.createCell(3).setCellValue(qty);
            row.getCell(3).setCellStyle(dataStyle);

            row.createCell(4).setCellValue("");
            row.getCell(4).setCellStyle(dataStyle);

            row.createCell(5).setCellValue("");
            row.getCell(5).setCellStyle(dataStyle);

            row.createCell(6).setCellValue("");
            row.getCell(6).setCellStyle(dataStyle);

            row.createCell(7).setCellValue("");
            row.getCell(7).setCellStyle(dataStyle);
        }

// -------------------------------------------
// 🔹 Add TOTAL & DISCOUNT Details (Right Side)
// -------------------------------------------
        int totalRowIndex = rowIndex; // Row after the last product
        CellStyle amountStyle = workbook.createCellStyle();
        Font amountHeadingFont = workbook.createFont();
        amountHeadingFont.setBold(true);
        String[] labels = {"EXW : UAE", "TOTAL AMOUNT     :", "DISCOUNT (5%)      :", "GRAND TOTAL     :"};
        for (int i = 0; i < labels.length; i++) {
            Row row = sheet.createRow(totalRowIndex++);
            Cell labelCell = row.createCell(5);
            labelCell.setCellValue(labels[i]);
            labelCell.setCellStyle(amountStyle);
        }

// -------------------------------------------
// 🔹 Add NOTES & TERMS CONDITIONS Details (Right Side)
// -------------------------------------------
        // Create a bold style for the "NOTES" heading with white font and dark blue background
        CellStyle notesHeadingStyle = workbook.createCellStyle();
        Font notesHeadingFont = workbook.createFont();
        notesHeadingFont.setBold(true);
        notesHeadingFont.setColor(IndexedColors.WHITE.getIndex()); // White font color
        notesHeadingStyle.setFont(notesHeadingFont);
        // ✅ Convert HEX to RGB and apply it
        byte[] rgb = new byte[]{(byte) 0x0F, (byte) 0x26, (byte) 0x57}; // Hex #0F2657 converted to RGB
        XSSFColor customColor = new XSSFColor(rgb, new DefaultIndexedColorMap());
        notesHeadingStyle.setFillForegroundColor(customColor);
        notesHeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        notesHeadingStyle.setAlignment(HorizontalAlignment.LEFT); // Align text to the left

        // Add "NOTES" heading
        Row notesHeadingRow = sheet.createRow(totalRowIndex++);
        Cell notesHeadingCell = notesHeadingRow.createCell(1); // Column F for notes heading
        notesHeadingCell.setCellValue("NOTES");
        notesHeadingCell.setCellStyle(notesHeadingStyle);
        //set blu background
        Cell notesHeadingcolorCell = notesHeadingRow.createCell(2);
        notesHeadingcolorCell.setCellValue("");
        notesHeadingcolorCell.setCellStyle(notesHeadingStyle);
        // Create a style for the note labels with white font and dark blue background
        CellStyle notesStyle = workbook.createCellStyle();
        notesStyle.setAlignment(HorizontalAlignment.LEFT);

// Define note labels
        String[] notes = {"*Spares Category:", "*Terms of Payment:", "*Quoted Currency:", "*Delivery Terms:","* QUOTED ITEMS ARE NON RETURNABLE",
                "* DELIVERY CHARGES ADDITIONAL"};

// Add notes labels with the same style
        for (String note : notes) {
            Row noteRow = sheet.createRow(totalRowIndex++);
            Cell noteCell = noteRow.createCell(1); // Column F for note labels
            noteCell.setCellValue(note);
            noteCell.setCellStyle(notesStyle);
        }
// Add "NOTES" heading
        Row termsAndCondHeadingRow = sheet.createRow(totalRowIndex++);
        Cell termsAndCondHeadingCell = termsAndCondHeadingRow.createCell(1); // Column F for notes heading
        termsAndCondHeadingCell.setCellValue("TERMS & CONDITIONS:");
        termsAndCondHeadingCell.setCellStyle(notesHeadingStyle);
// Define note labels
        String[] termsAndCond = {"* If the delivery without Vessel sign/stamp the VAT will be applicable.",
                "* Offer validity will be 30 days from the quoted date.",
                "* Offered items subject to prior sale", "* Offer based on Ex works price and the delivery/dispatch/packing charges are additional",
                "* After order confirmation cancellation is not allowed. ", "* If need to cancel the order, cancellation charges will be applicable"};

// Add notes labels with the same style
        for (String terms : termsAndCond) {
            Row termsRow = sheet.createRow(totalRowIndex++);
            Cell termsCell = termsRow.createCell(1); // Column F for note labels
            termsCell.setCellValue(terms);
            termsCell.setCellStyle(notesStyle);
        }

        int lastRow = totalRowIndex;
        Row lastRowRow = sheet.createRow(lastRow+1);
        Cell lastRowCell = lastRowRow.createCell(1);
        lastRowCell.setCellValue("If you have any questions about this price quote, please contact by what's app +971 50 8807549 (or) email \"sales@kauferkart.com\"");
        lastRowCell.setCellStyle(notesStyle);

      applyOuterBorder(sheet, rowIndex, lastRow +2, 0, 9, workbook); // Logo Section
// 🔹 Write the Excel File
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }

    // -------------------------------------------
    // 🔹 Helper Method to Apply Borders to Merged Cells
    private void applyBorders(Sheet sheet, int startRow, int endRow, int startCol, int endCol, CellStyle style) {
        for (int row = startRow; row <= endRow; row++) {
            Row sheetRow = sheet.getRow(row);
            if (sheetRow == null) sheetRow = sheet.createRow(row);
            for (int col = startCol; col <= endCol; col++) {
                Cell cell = sheetRow.getCell(col);
                if (cell == null) cell = sheetRow.createCell(col);
                cell.setCellStyle(style);
            }
        }
    }

    // -------------------------------------------
// 🔹 Function to Apply Outer Border to Merged Cells
// -------------------------------------------
    private static void applyOuterBorder(XSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, XSSFWorkbook workbook) {
        for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            for (int colIndex = firstCol; colIndex <= lastCol; colIndex++) {
                Cell cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.createCell(colIndex);
                }

                // Retrieve the existing cell style
                CellStyle existingStyle = cell.getCellStyle();
                CellStyle newStyle = workbook.createCellStyle();

                // Copy existing style properties
                if (existingStyle != null) {
                    newStyle.cloneStyleFrom(existingStyle);
                }

                // Apply Borders Only to Outer Edges
                if (rowIndex == firstRow) {
                    newStyle.setBorderTop(BorderStyle.THIN);
                }
                if (rowIndex == lastRow) {
                    newStyle.setBorderBottom(BorderStyle.THIN);
                }
                if (colIndex == firstCol) {
                    newStyle.setBorderLeft(BorderStyle.THIN);
                }
                if (colIndex == lastCol) {
                    newStyle.setBorderRight(BorderStyle.THIN);
                }

                // Set the new style with both borders and existing properties
                cell.setCellStyle(newStyle);
            }
        }
    }

}