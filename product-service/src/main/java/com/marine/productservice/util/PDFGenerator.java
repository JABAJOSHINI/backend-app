package com.marine.productservice.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.marine.productservice.DTO.CartDto;
import com.marine.productservice.model.CartModel;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.model.RFQModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PDFGenerator {
    public ByteArrayOutputStream generatePdfsample(EnquiryModel enquiry, List<CartDto> productList, String orderNumber) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


            try {
                String logoPath = "https://marine-store.s3.eu-north-1.amazonaws.com/Home-Page/company-logo.png"; // Replace with your logo URL

                // PdfWriter with ByteArrayOutputStream
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                // Create a Table with 2 Columns for Header
                Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth();
                headerTable.setMarginBottom(20);

                // Left Side: Logo
                Image logo = new Image(ImageDataFactory.create(logoPath));
                logo.setMaxHeight(20).setAutoScale(true); // Adjust size
                Cell logoCell = new Cell().add(logo)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.LEFT); // Logo aligned to the left
                headerTable.addCell(logoCell);

                // Right Side: Dynamic Content
                String dynamicContent = "Ref Number:" +orderNumber + "\nDate:" + java.time.LocalDate.now();
                Paragraph rightContent = new Paragraph(dynamicContent)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(8);
                Cell dynamicCell = new Cell().add(rightContent)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                headerTable.addCell(dynamicCell);

                // Add Header Table to Document
                document.add(headerTable);
                // Add Company Details Below the Header Table
                PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
                Paragraph companyDetails = new Paragraph()
                        .add(new Text("KAUFERKART\n").setFont(boldFont))  // Setting 'V' as bold
                        .add("Block B, Office B24-041,\n")
                        .add("Sharjah Research Technology & Innovation Park,\n")
                        .add("Sharjah, United Arab Emirates\n")
                        .add("www.kauferkart.com\n")
                        .add("Phone: +971 568976712 (what’s app only)")
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(8);
                Cell detailsCell = new Cell().add(companyDetails)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                detailsTable.addCell(detailsCell);
                document.add(detailsTable);
/////////////////////
                // Create Table with 2 Columns (Left: Company Details, Right: Client Details)
                Table vesselTable = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth();
                vesselTable.setMarginBottom(20);

// Left Side: Company Details
                //PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                Table vesselstable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
                Paragraph vesselContent = new Paragraph()
                        .add(new Text("Equipment: ").setFont(boldFont)).add(enquiry.getEquipment() + "\n")
                        .add(new Text("Vessel: ").setFont(boldFont)).add(enquiry.getVessel() + "\n")
                        .add(new Text("Model: ").setFont(boldFont)).add(enquiry.getModel() + "\n")
                        .add(new Text("Make: ").setFont(boldFont)).add(enquiry.getMake() + "\n")
                        .add(new Text("IMO: ").setFont(boldFont)).add(enquiry.getImo() + "\n")
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(8);

                Cell vesselCell = new Cell().add(vesselContent)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                vesselstable.addCell(vesselCell);

// Add Left Cell (Company Details)
                Cell leftCell = new Cell().add(vesselstable)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                vesselTable.addCell(leftCell);

// Right Side: Client Details
                String clientContent = "Ref Number: " + orderNumber + "\nDate: " + java.time.LocalDate.now();
                Paragraph client = new Paragraph(clientContent)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(8);

                Cell clientCell = new Cell().add(client)
                        .setBorder(null)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setTextAlignment(TextAlignment.RIGHT); // Align text to right
                vesselTable.addCell(clientCell);

// Add Table to Document
                document.add(vesselTable);

//                Table vesselTable = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth();
//                vesselTable.setMarginBottom(20);
//
//                // Add Empty Left Cell to Push Content to Right
//                Cell emptyCell = new Cell().setBorder(null);
//
//                vesselTable.addCell(emptyCell);
//
//                // Right Side: Dynamic Content
//                String clientContent = "Full Name: " + orderNumber + "\nCompany Name: " + enquiry.getCompanyName()+
//                        "\nEmail: " + enquiry.getEmail() + "\nPhone no: " + enquiry.getPhone()+ "\nmessage: " + java.time.LocalDate.now();
//                Paragraph client = new Paragraph(clientContent)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(8);
//
//                Cell clientCell = new Cell().add(client)
//                        .setBorder(null)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setTextAlignment(TextAlignment.RIGHT); // Align cell contents
//
//                vesselTable.addCell(clientCell);
//
//                // Add Table to Document
//                document.add(vesselTable);

//                // Add Company Details Below the Header Table
//                PdfFont boldsFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//                Table vesselstable = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
//                Paragraph vesselContent = new Paragraph()
//                        .add(new Text("Equipment: ").setFont(boldFont))
//                        .add(enquiry.getEquipment() + "\n")  // Correct usage of add()
//
//                        .add(new Text("Vessel: ").setFont(boldFont))
//                        .add(enquiry.getVessel() + "\n")  // Corrected field
//
//                        .add(new Text("Model: ").setFont(boldFont))
//                        .add(enquiry.getModel() + "\n")  // Corrected field
//
//                        .add(new Text("Make: ").setFont(boldFont))
//                        .add(enquiry.getMake() + "\n")  // Corrected field
//
//                        .add(new Text("IMO: ").setFont(boldFont))
//                        .add(enquiry.getImo() + "\n")  // Corrected field
//
//                        .setTextAlignment(TextAlignment.LEFT)
//                        .setFontSize(8);
//                Cell vesselCell = new Cell().add(vesselContent)
//                        .setBorder(null)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
//                vesselstable.addCell(vesselCell);
//                document.add(vesselstable);


                // Add Vessel and Equipment Details Below Company Details
//                Paragraph vesselContent = new Paragraph()
//                        .add(new Text("Equipment: ").setFont(boldFont))
//                        .add(enquiry.getEquipment() + "\n")  // Correct usage of add()
//
//                        .add(new Text("Vessel: ").setFont(boldFont))
//                        .add(enquiry.getVessel() + "\n")  // Corrected field
//
//                        .add(new Text("Model: ").setFont(boldFont))
//                        .add(enquiry.getModel() + "\n")  // Corrected field
//
//                        .add(new Text("Make: ").setFont(boldFont))
//                        .add(enquiry.getMake() + "\n")  // Corrected field
//
//                        .add(new Text("IMO: ").setFont(boldFont))
//                        .add(enquiry.getImo() + "\n")  // Corrected field
//
//                        .setTextAlignment(TextAlignment.LEFT)
//                        .setFontSize(8);
//
//// Add Vessel Details Below Company Details
//                Cell vesselDetailsCell = new Cell().add(vesselContent)
//                        .setBorder(null)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
//                detailsTable.addCell(vesselDetailsCell);


// Define column widths for the new table
                Table mainTable = new Table(UnitValue.createPercentArray(new float[]{1,3, 2, 1, 1, 1})).useAllAvailableWidth();
                mainTable.setMarginTop(20);

// Add header row
                Cell slNo = new Cell().add(new Paragraph("S.No")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                Cell descriptionHeader = new Cell().add(new Paragraph("Description")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                Cell partNumHeader = new Cell().add(new Paragraph("Part Number")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                Cell makeHeader = new Cell().add(new Paragraph("Make")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                Cell modelHeader = new Cell().add(new Paragraph("Model")).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                Cell quantityHeader = new Cell().add(new Paragraph("Qty"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER);
                mainTable.addCell(slNo);
                mainTable.addCell(descriptionHeader);
                mainTable.addCell(partNumHeader);
                mainTable.addCell(makeHeader);
                mainTable.addCell(modelHeader);
                mainTable.addCell(quantityHeader);

// Add sample data rows
                for (CartDto dto : productList) {
                    int slno = 0;
                    String sno = (dto.getProducts() != null && dto.getProducts().getDescription() != null)
                            ? dto.getProducts().getDescription()
                            : "N/A";
                    String description = (dto.getProducts() != null && dto.getProducts().getDescription() != null)
                            ? dto.getProducts().getDescription()
                            : "N/A";
                    String partNumber = (dto.getProducts() != null && dto.getProducts().getPartNumber() != null)
                            ? dto.getProducts().getPartNumber()
                            : "N/A";
                    String make = (dto.getProducts() != null && dto.getProducts().getMake() != null)
                            ? dto.getProducts().getMake()
                            : "N/A";
                    String model = (dto.getProducts() != null && dto.getProducts().getModel() != null)
                            ? dto.getProducts().getModel()
                            : "N/A";
                    String quantity = (dto.getQuantity() != null)
                            ? String.valueOf(dto.getQuantity())
                            : "N/A";
                    Cell slnoCell = new Cell().add(new Paragraph(String.valueOf(slno++))).setTextAlignment(TextAlignment.CENTER).setFontSize(8);
                    Cell descriptionCell = new Cell().add(new Paragraph(description)).setFontSize(8);
                    Cell partNumCell = new Cell().add(new Paragraph(partNumber)).setTextAlignment(TextAlignment.CENTER).setFontSize(8);
                    Cell makeCell = new Cell().add(new Paragraph(make)).setTextAlignment(TextAlignment.CENTER).setFontSize(8);
                    Cell modelCell = new Cell().add(new Paragraph(model)).setTextAlignment(TextAlignment.CENTER).setFontSize(8);
                    Cell quantityCell = new Cell().add(new Paragraph(quantity)).setTextAlignment(TextAlignment.CENTER);

                    mainTable.addCell(slnoCell);
                    mainTable.addCell(descriptionCell);
                    mainTable.addCell(partNumCell);
                    mainTable.addCell(makeCell);
                    mainTable.addCell(modelCell);
                    mainTable.addCell(quantityCell);
                }


// Add the table to the document
                document.add(mainTable);
                // Add a note below the table
                Paragraph note = new Paragraph("Note: Your enquiry has been received. The admin will contact you through mail, or you can contact them via the WhatsApp number provided. Thank you.")
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10)
                        .setMarginTop(20);
                document.add(note);

                // Close the document
                document.close();
                System.out.println("PDF generated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return outputStream;

        }

    public ByteArrayOutputStream generatePdf(EnquiryModel enquiry, String orderNumber) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // === 1. Company Header with Logo ===
        ImageData imageData = ImageDataFactory.create("https://c.pxhere.com/images/56/67/c5004fbbaf1cb138b51570c6e43b-1597533.jpg!d");
        Image logo = new Image(imageData).scaleToFit(100, 50);

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 6}))
                .useAllAvailableWidth();

        headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));

        headerTable.addCell(new Cell()
                .add(new Paragraph("KAUFERKART").setBold().setFontSize(14))
                .add(new Paragraph("Block B, Office B24-041\n" +
                        "Sharjah Research Technology & Innovation Park\n" +
                        "Sharjah, United Arab Emirates"))
                .add(new Paragraph("Phone: +971 568976712 (WhatsApp only)"))
                .add(new Paragraph("Email: www.kauferkart.com"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));

        document.add(headerTable);

        // === 2. RFQ Title ===
        Paragraph rfqTitle = new Paragraph()
                .add(new Text("Request for Quotation - ").setFontSize(14).setBold().setFontColor(ColorConstants.RED))
                .add(new Text(orderNumber).setFontSize(16).setBold().setFontColor(ColorConstants.RED))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setMarginBottom(20);

        document.add(rfqTitle);


        // === 3. Client & RFQ Info (Side by Side) ===
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth();

        // --- Client Details ---
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

        String companyName = (enquiry.getCompanyName() != null) ? enquiry.getCompanyName() : "N/A";
        String email = (enquiry.getEmail() != null) ? enquiry.getEmail() : "N/A";
        String phone = (enquiry.getPhone() != null) ? enquiry.getPhone() : "N/A";
        String message = (enquiry.getMessage() != null) ? enquiry.getMessage() : "N/A";
        String address = (enquiry.getCompanyAddress() != null) ? enquiry.getCompanyAddress() : "N/A";
        if(!message.equals("N/A")){
            infoTable.addCell(getInfoCell("CLIENT DETAILS",
                    "Company: " + companyName,
                    "Name: " + fullName,
                    "Email: " + email,
                    "Phone: " + phone + "," +enquiry.getCountry().getName(),
                    "Message: " + message,
                    "Date Issued: " + LocalDate.now()));
        }else {
            infoTable.addCell(getInfoCell("CLIENT DETAILS",
                    "Company: " + companyName,
                    "Name: " + fullName,
                    "Email: " + email,
                    "Phone: " + phone + "," +enquiry.getCountry().getName(),
                    "Company Address: " + address,
                    "Date Issued: " + LocalDate.now()));
        }

        // --- RFQ Details ---
        String vesselName = (enquiry.getVessel() != null) ? enquiry.getVessel() : "N/A";
        String equipment = (enquiry.getEquipment() != null) ? enquiry.getEquipment() : "N/A";
        String make = (enquiry.getMake() != null) ? enquiry.getMake() : "N/A";
        String model = (enquiry.getModel() != null) ? enquiry.getModel() : "N/A";
        String imo = (enquiry.getImo() != null) ? enquiry.getImo() : "N/A";
        if(!vesselName.equals("N/A") || !equipment.equals("N/A") || !make.equals("N/A") || !model.equals("N/A") || !imo.equals("N/A") ) {
            infoTable.addCell(getInfoCell("RFQ DETAILS",
                    "Equipment: " + equipment,
                    "Vessel: " + vesselName,
                    "Make: " + make,
                    "Model: " + model,
                    "IMO: " + imo));
        }
        document.add(infoTable.setMarginBottom(20));

        // === Product Table ===
        float[] columnWidths = {3, 3, 3, 3, 3, 3}; // all equal width like your example
        Table mainTable = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setMarginTop(20);

// === Header Row ===
        String[] headers = {"S.No", "Description", "Part Number", "Make", "Model", "Qty"};
        for (String h : headers) {
            mainTable.addCell(
                    new Cell()
                            .add(new Paragraph(h)
                                    .setBold()
                                    .setFontSize(11)
                                    .setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(new DeviceRgb(9, 58, 125)) // Navy Blue #093a7d
                            .setTextAlignment(TextAlignment.CENTER)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setPadding(8)
                            .setBorder(Border.NO_BORDER) // clean look
            );
        }

// === Data Rows ===
        int slno = 1;
        boolean alternate = false; // for zebra striping

        for (RFQModel dto : enquiry.getRfq()) {
            String description = dto.getDescription() != null ? dto.getDescription() : "N/A";
            String partNumber = dto.getPartNumber() != null ? dto.getPartNumber() : "N/A";
            String rfqMake = dto.getMake() != null ? dto.getMake() : "N/A";
            String rfqModel = dto.getModel() != null ? dto.getModel() : "N/A";
            String quantity = dto.getQuantity() != null ? String.valueOf(dto.getQuantity()) : "N/A";

            // Choose row background
            DeviceRgb bgColor = alternate ? new DeviceRgb(245, 245, 245) : (DeviceRgb) ColorConstants.WHITE;
            alternate = !alternate;

            // Add cells
            mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(slno++)).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));

            mainTable.addCell(new Cell().add(new Paragraph(description).setFontSize(10))
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));

            mainTable.addCell(new Cell().add(new Paragraph(partNumber).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));

            mainTable.addCell(new Cell().add(new Paragraph(rfqMake).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));

            mainTable.addCell(new Cell().add(new Paragraph(rfqModel).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));

            mainTable.addCell(new Cell().add(new Paragraph(quantity).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(bgColor)
                    .setPadding(6)
                    .setBorder(Border.NO_BORDER));
        }

// Then check Cart items
        if (enquiry.getCart() != null && !enquiry.getCart().isEmpty()) {
            for (CartModel cart : enquiry.getCart()) {
                String cart_description = (cart != null && cart.getName() != null) ? cart.getName() : "N/A";
                String cart_partNumber = "N/A";
                String cart_make = (cart != null && cart.getMake() != null) ? cart.getMake() : "N/A";
                String cart_model = "N/A";
                String cart_quantity = (cart != null && cart.getQuantity() != null) ? String.valueOf(cart.getQuantity()) : "N/A";

                // Alternate row background (zebra effect)
                DeviceRgb bgColor = alternate ? new DeviceRgb(245, 245, 245) : (DeviceRgb) ColorConstants.WHITE;
                alternate = !alternate;

                mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(slno++)).setFontSize(10))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));

                mainTable.addCell(new Cell().add(new Paragraph(cart_description).setFontSize(10))
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));

                mainTable.addCell(new Cell().add(new Paragraph(cart_partNumber).setFontSize(10))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));

                mainTable.addCell(new Cell().add(new Paragraph(cart_make).setFontSize(10))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));

                mainTable.addCell(new Cell().add(new Paragraph(cart_model).setFontSize(10))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));

                mainTable.addCell(new Cell().add(new Paragraph(cart_quantity).setFontSize(10))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER));
            }
        }

// Finally add to document
        document.add(mainTable);
        document.close();
        return outputStream;
    }

    // 🔹 Helper for Info Section
    // === Helper Method for Info Cell ===
    private static Cell getInfoCell(String title, String... lines) {
        Paragraph paragraph = new Paragraph()
                .add(new Text(title + "\n").setBold().setFontSize(11)); // 🔹 Title Bold

        for (String line : lines) {
            paragraph.add(new Text(line + "\n").setFontSize(10)); // 🔹 Normal (not bold)
        }

        return new Cell()
                .add(paragraph)
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
    }


}


