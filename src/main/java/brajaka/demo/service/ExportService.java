package brajaka.demo.service;

import brajaka.demo.dto.LaporanKeuanganDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    // ✅ Export ke Excel
    public byte[] exportToExcel(List<LaporanKeuanganDto> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Laporan Keuangan");

//        int rowIdx = 0;
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Trace Number", "Tanggal", "Kode Transaksi", "Deskripsi",
                "Nominal Masuk", "Nominal Keluar", "Jenis Rekening", "Kode Akun", "Nama Akun"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

//        for (LaporanKeuanganDto item : data) {
        if (data !=null) {
            for (int i = 0; i < data.size(); i++) {
                LaporanKeuanganDto item = data.get(i);
                Row row = sheet.createRow(i + 1);
//            Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getTraceNumber());
                row.createCell(1).setCellValue(item.getTanggal().toString());
                row.createCell(2).setCellValue(item.getKodeTransaksi());
                row.createCell(3).setCellValue(item.getDeskripsi());
                row.createCell(4).setCellValue(item.getNominalMasuk().doubleValue());
                row.createCell(5).setCellValue(item.getNominalKeluar().doubleValue());
                row.createCell(6).setCellValue(item.getJenisRekening());
                row.createCell(7).setCellValue(item.getKodeAkun());
                row.createCell(8).setCellValue(item.getNamaAkun());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    public byte[] exportToPDF(List<LaporanKeuanganDto> data) {
        try {
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                PdfWriter.getInstance(document, outputStream);
                document.open();

                // Add title
                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("Laporan Histori Saldo", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" ")); // Spacer

                if (data == null || data.isEmpty()) {
                    document.add(new Paragraph("Tidak ada data untuk ditampilkan"));
                } else {
                    // Create table
                    PdfPTable table = new PdfPTable(9);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);

                    // Table headers
                    String[] headers = {"Trace Number", "Tanggal", "Kode Transaksi", "Deskripsi",
                            "Nominal Masuk", "Nominal Keluar", "Jenis Rekening", "Kode Akun", "Nama Akun"};

                    for (String header : headers) {
                        PdfPCell cell = new PdfPCell(new Phrase(header));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    }

                    // Table data
                    for (LaporanKeuanganDto item : data) {
                        table.addCell(item.getTraceNumber() != null ? item.getTraceNumber() : "");
                        table.addCell(item.getTanggal() != null ? item.getTanggal().toString() : "");
                        table.addCell(item.getKodeTransaksi() != null ? item.getKodeTransaksi() : "");
                        table.addCell(item.getDeskripsi() != null ? item.getDeskripsi() : "");
                        table.addCell(item.getNominalMasuk() != null ? item.getNominalMasuk().toString() : "0");
                        table.addCell(item.getNominalKeluar() != null ? item.getNominalKeluar().toString() : "0");
                        table.addCell(item.getJenisRekening() != null ? item.getJenisRekening() : "");
                        table.addCell(item.getKodeAkun() != null ? item.getKodeAkun() : "");
                        table.addCell(item.getNamaAkun() != null ? item.getNamaAkun() : "");
                    }

                    document.add(table);
                }

            } finally {
                document.close();
            }

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }
}