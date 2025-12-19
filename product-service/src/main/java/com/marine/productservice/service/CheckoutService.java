package com.marine.productservice.service;

import com.marine.productservice.entity.CartEntity;
import com.marine.productservice.entity.ClientEntity;
import com.marine.productservice.model.CartItemsModel;
import com.marine.productservice.model.checkoutModel;
import com.marine.productservice.repository.CartRepository;
import com.marine.productservice.repository.ClientRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

@Service
public class CheckoutService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ResourceLoader resourceLoader;

    public String transformEmailTemplate(String recipientName) throws Exception {
        recipientName = "jjjsohini";
        // Load XSLT stylesheet
        File xsltFile = ResourceUtils.getFile("classpath:templates/enquiry-email.xsl");
        StreamSource xsltSource = new StreamSource(xsltFile);
        // Load HTML template
        Resource htmlResource = resourceLoader.getResource("classpath:templates/enquiry-email-admin.html");
        StreamSource htmlSource = new StreamSource(htmlResource.getInputStream());

        // Perform transformation
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);

        // Transform HTML using XSLT
        StringWriter writer = new StringWriter();
        transformer.transform(htmlSource, new StreamResult(writer));

        return writer.toString();
    }

    public void sendEnquiryEmailToAdmin(checkoutModel checkoutItems) throws MessagingException, IOException {
        ClientEntity clientDetails = saveClientDetails(checkoutItems);
        createExcelFile(clientDetails.getId(), checkoutItems);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(checkoutItems.getEmail());
        helper.setSubject("KAUFERKART: Enguiry From Client");
        // Add attachment
        File file = new File(createExcelFile(clientDetails.getId(), checkoutItems));
        helper.addAttachment(file.getName(), file);

        String subject = null;
        String message = null;
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", message);
        String htmlContent = templateEngine.process("enquiry-email-admin", context);

        helper.setText(htmlContent, true);

        emailSender.send(mimeMessage);
    }

    private ClientEntity saveClientDetails(checkoutModel checkoutItems) {
        List<CartEntity> cartList = new ArrayList<>();
        ClientEntity client = new ClientEntity();
        if (checkoutItems != null) {
            client.setEmail(checkoutItems.getEmail());
            client.setFullName(checkoutItems.getFullName());
            client.setCompanyName(checkoutItems.getCompanyName());
            client.setCompanyAddress(checkoutItems.getCompanyAddress());
            if (checkoutItems.getCart() != null) {
                for (CartItemsModel c : checkoutItems.getCart()) {
                    CartEntity cart = new CartEntity();
                    cart.setProductName(c.getProductName());
                    cart.setPrice(c.getPrice());
                    cart.setImage(c.getImage());
                    cart.setClientEntity(client);
                    cartList.add(cart);
                }
            }

            clientRepository.save(client);
            cartRepository.saveAll(cartList);
        }
        return client;
    }


    public String createExcelFile(Long clientId, checkoutModel checkoutItems) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("CL" + clientId);

       CellStyle fontStyle = createFontStyle(workbook);
        CellStyle whiteStyle = createWhiteBackground(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("img");
        headerRow.setHeightInPoints(100); // 100 points
        createBorderlessCell(headerRow, workbook, whiteStyle, fontStyle, false);

        Row headerRow1 = sheet.createRow(1);
        Cell kauferCell = headerRow1.createCell(0);
        kauferCell.setCellValue("KANUFERKART");
        headerRow1.createCell(10).setCellValue("COMMERCIAL INVOICE & PACKING LIST:");
        createBorderlessCell(headerRow1, workbook, whiteStyle, fontStyle, true);

        Row headerRow2 = sheet.createRow(2);
        headerRow2.createCell(0).setCellValue("Block B, Office B24-041");
        headerRow2.createCell(10).setCellValue("Date:");
        createBorderlessCell(headerRow2, workbook, whiteStyle, fontStyle, false);

        Row headerRow3 = sheet.createRow(3);
        headerRow3.createCell(0).setCellValue("Sharjah Research Technology & Innovation Park");
        headerRow3.createCell(10).setCellValue("Invoice No:");
        createBorderlessCell(headerRow3, workbook, whiteStyle, fontStyle, false);

        Row headerRow4 = sheet.createRow(4);
        headerRow4.createCell(0).setCellValue("Sharjah, United Arab Emirates");
        headerRow4.createCell(10).setCellValue("PO No: ");
        createBorderlessCell(headerRow4, workbook, whiteStyle, fontStyle, false);

        Row headerRow5 = sheet.createRow(5);
        headerRow5.createCell(0).setCellValue("+971 568976712 (what’s app only)");
        headerRow5.createCell(10).setCellValue("PO Date: ");
        createBorderlessCell(headerRow5, workbook, whiteStyle, fontStyle, false);

        Row headerRow6 = sheet.createRow(6);
        headerRow6.createCell(0).setCellValue("www.kauferkart.com");
        headerRow6.createCell(10).setCellValue("Vessel: ");
        createBorderlessCell(headerRow6, workbook, whiteStyle, fontStyle, false);

        Row headerRow7 = sheet.createRow(7);
        headerRow7.createCell(0).setCellValue("TO:");
        createBorderlessCell(headerRow7, workbook, whiteStyle, fontStyle, false);

        Integer i = 0;
        Row tableRow = sheet.createRow(9);
        tableRow.createCell(0).setCellValue("Sl No");
        tableRow.createCell(1).setCellValue("Description");
        tableRow.createCell(2).setCellValue("Part No/Dug No");
        tableRow.createCell(3).setCellValue("Qty");
        tableRow.createCell(4).setCellValue("Unit");
        tableRow.createCell(5).setCellValue("Price / Pc");
        tableRow.createCell(6).setCellValue("Total Price");
        tableRow.createCell(7).setCellValue("Remarks");
        createBorderCell(tableRow,workbook);
        if(checkoutItems.getCart() != null){
             i = 10;
            for(CartItemsModel c: checkoutItems.getCart() ) {
                Row dataRow2 = sheet.createRow(i++);
                dataRow2.createCell(0).setCellValue(c.getProductName());
                dataRow2.createCell(1).setCellValue(c.getPrice());
                dataRow2.createCell(2).setCellValue(c.getImage());
                createBorderCell(dataRow2,workbook);

            }
}
        Row totalRow = sheet.createRow(i);
        totalRow.createCell(4).setCellValue("Total Amount  (USD)          :1345");
        Row noteRow = sheet.createRow(i+2);
        noteRow.createCell(2).setCellValue("Country of Origin: UAE");
        Row packingRow = sheet.createRow(i+3);
        packingRow.createCell(2).setCellValue("15 Kg : 1 Pallet");
        Row lastRow = sheet.createRow(i+4);
        lastRow.createCell(2).setCellValue("If you have any questions about this price Invoice, please contact by what's app +971 568976712 (or) email \"sales@kauferkart.com\"");
        Row thankYouRow = sheet.createRow(i+5);
        thankYouRow.createCell(5).setCellValue("Thank you for your business");

        //        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
//            Cell cell = headerRow.getCell(i);
//            if (cell == null) {
//                cell = headerRow.createCell(i);
//            }
//      //      cell.setCellStyle(yellowStyle);
//        }
//        for (int i = 0; i < headerRow2.getLastCellNum(); i++) {
//            Cell cell = headerRow2.getCell(i);
//            if (cell == null) {
//                cell = headerRow2.createCell(i);
//            }
//     //       cell.setCellStyle(yellowStyle);
//        }
        // Generate file name based on client ID
        // Specify a valid directory path where you want to save the Excel file
        String directoryPath = "/Users/joshinianklin/Desktop/excel/";
        String fileName = "CL-" + clientId + "_enquiry.xlsx";
        String filePath = directoryPath + fileName;

        // Write the workbook to the file path
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // Close the workbook
        workbook.close();

        return filePath;
    }


    private void createBorderCell(Row tableRow, Workbook workbook) {
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);
        for (Cell cell : tableRow) {
            cell.setCellStyle(borderStyle);
        }
    }


    private CellStyle createFontStyle(Workbook workbook) {
        CellStyle fontStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        fontStyle.setFont(font);
        return fontStyle;
    }

    private CellStyle createWhiteBackground(Workbook workbook) {
        CellStyle whiteStyle = workbook.createCellStyle();
        whiteStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        whiteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return whiteStyle;
    }

    private void createBorderlessCell(Row headerRow, Workbook workbook, CellStyle whiteStyle, CellStyle fontStyle, boolean fontFlag) {

        CellStyle borderlessCellStyle = workbook.createCellStyle();
        // Loop through each cell in the row and apply the borderless cell style
        for (int i = 0; i <= 10; i++) {
            CellStyle cellStyle = workbook.createCellStyle();
            Cell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.createCell(i);
            }
            cellStyle.cloneStyleFrom(borderlessCellStyle);
            cell.setCellStyle(whiteStyle);
            if(fontFlag == true){
                cell.setCellStyle(fontStyle);
            }
        }
    }
}
