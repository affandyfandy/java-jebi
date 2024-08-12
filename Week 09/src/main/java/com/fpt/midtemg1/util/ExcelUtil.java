package com.fpt.midtemg1.util;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.dto.InvoiceDTO;
import com.fpt.midtemg1.dto.InvoiceProductDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<Product> parseProductFile(InputStream is) throws Exception {
        List<Product> products = new ArrayList<>();
        if (is.available() == 0) {
            return products; // Return empty list if file is empty
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                return products; // Return empty list if no data rows
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                Product product = new Product();
                product.setName(getCellValueAsString(row.getCell(0)));
                product.setPrice(getCellValueAsBigDecimal(row.getCell(1)));
                product.setStatus(getCellValueAsStatus(row.getCell(2)));
                product.setCreatedTime(getCellValueAsTimestamp(row.getCell(3)));
                product.setUpdatedTime(getCellValueAsTimestamp(row.getCell(4)));
                products.add(product);
            }
        }

        return products;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private static BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                try {
                    yield new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    yield BigDecimal.ZERO;
                }
            }
            default -> BigDecimal.ZERO;
        };
    }

    private static Status getCellValueAsStatus(Cell cell) {
        String value = getCellValueAsString(cell);
        try {
            return Status.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Status.ACTIVE;
        }
    }

    private static Timestamp getCellValueAsTimestamp(Cell cell) {
        String value = getCellValueAsString(cell);
        try {
            return new Timestamp(DATE_FORMAT.parse(value).getTime());
        } catch (ParseException e) {
            return new Timestamp(System.currentTimeMillis());
        }
    }

    public static byte[] exportInvoicesToExcel(List<InvoiceDTO> invoices) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Invoices");

            int rowNum = 0;
            createHeaderRow(sheet.createRow(rowNum++));

            for (InvoiceDTO invoice : invoices) {
                if (invoice.getInvoiceProducts() != null && !invoice.getInvoiceProducts().isEmpty()) {
                    for (InvoiceProductDTO invoiceProduct : invoice.getInvoiceProducts()) {
                        createInvoiceProductRow(sheet.createRow(rowNum++), invoiceProduct, invoice);
                    }
                } else {
                    createInvoiceRow(sheet.createRow(rowNum++), invoice);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static void createHeaderRow(Row row) {
        String[] headers = {
                "Invoice ID", "Customer ID", "Customer Name", "Invoice Amount",
                "Invoice Date", "Updated Date", "Product ID", "Product Name",
                "Product Price", "Quantity", "Amount"
        };

        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
    }

    private static void createInvoiceRow(Row row, InvoiceDTO invoice) {
        row.createCell(0).setCellValue(invoice.getId());
        row.createCell(1).setCellValue(invoice.getCustomer().getId());
        row.createCell(2).setCellValue(invoice.getCustomer().getName());
        row.createCell(3).setCellValue(invoice.getInvoiceAmount().doubleValue());
        row.createCell(4).setCellValue(invoice.getInvoiceDate().toString());
        row.createCell(5).setCellValue(invoice.getUpdatedTime().toString());
    }

    private static void createInvoiceProductRow(Row row, InvoiceProductDTO invoiceProduct, InvoiceDTO invoice) {
        createInvoiceRow(row, invoice);
        row.createCell(6).setCellValue(invoiceProduct.getProduct().getId());
        row.createCell(7).setCellValue(invoiceProduct.getProduct().getName());
        row.createCell(8).setCellValue(invoiceProduct.getProduct().getPrice().doubleValue());
        row.createCell(9).setCellValue(invoiceProduct.getQuantity());
        row.createCell(10).setCellValue(invoiceProduct.getAmount().doubleValue());
    }
}
