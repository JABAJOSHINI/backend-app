package com.marine.productservice.util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

public class CellStyler {
    public CellStyle createWarningColor(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Courier New");
        font.setBold(true);
        font.setUnderline(Font.U_SINGLE);
        font.setColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}