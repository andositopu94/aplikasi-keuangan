package brajaka.demo.controller;

import brajaka.demo.dto.BukuUtamaMapper;
import brajaka.demo.dto.LaporanKeuanganDto;
import brajaka.demo.model.BukuUtama;
import brajaka.demo.repository.BukuUtamaRepository;
import brajaka.demo.repository.UangKeluarRepository;
import brajaka.demo.repository.UangMasukRepository;
import brajaka.demo.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/laporan")
public class LaporanController {

    private final UangMasukRepository uangMasukRepository;
    private final UangKeluarRepository uangKeluarRepository;
    private final BukuUtamaRepository bukuUtamaRepository;
    private final ExportService exportService;
    private static final Logger log = LoggerFactory.getLogger(LaporanController.class);

    public LaporanController(UangMasukRepository uangMasukRepository, UangKeluarRepository uangKeluarRepository, BukuUtamaRepository bukuUtamaRepository, ExportService exportService) {
        this.uangMasukRepository = uangMasukRepository;
        this.uangKeluarRepository = uangKeluarRepository;
        this.bukuUtamaRepository = bukuUtamaRepository;
        this.exportService = exportService;
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response, @RequestParam(required = false) LocalDate tanggalAwal,
                              @RequestParam(required = false) LocalDate tanggalAkhir) throws IOException{
        LocalDateTime start = (tanggalAwal != null ? tanggalAwal.atStartOfDay(): LocalDateTime.MIN);
        LocalDateTime end = (tanggalAkhir != null ? tanggalAkhir.atTime(LocalTime.MAX) : LocalDateTime.now());

        List<BukuUtama> bukuUtamaList = bukuUtamaRepository.findByTanggalBetween(start, end);
        List<LaporanKeuanganDto> dtoList = mapToDto(bukuUtamaList);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Keuangan.xlsx");

        response.getOutputStream().write(exportService.exportToExcel(dtoList));
        response.getOutputStream().flush();
    }
    @GetMapping("/pdf")
    public void exportToPdf(HttpServletResponse response, @RequestParam(required = false) LocalDate tanggalAwal,
                            @RequestParam(required = false) LocalDate tanggalAkhir){

        try {
            LocalDateTime start = (tanggalAwal != null ? tanggalAwal.atStartOfDay() : LocalDateTime.MIN);
            LocalDateTime end = (tanggalAkhir != null ? tanggalAkhir.atTime(LocalTime.MAX) : LocalDateTime.now());

            List<BukuUtama> bukuUtamaList = bukuUtamaRepository.findByTanggalBetween(start, end);
            List<LaporanKeuanganDto> dtoList = mapToDto(bukuUtamaList);

            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Keuangan.pdf");

            response.getOutputStream().write(exportService.exportToPDF(dtoList));
            response.getOutputStream().flush();

        }catch (IOException e) {
            log.error("Error in PDF export", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                response.getWriter().write("Error generating PDF: " + e.getMessage());
            } catch (IOException ex) {
                log.error("Failed to write error response", ex);
            }
        }
    }

    private List<LaporanKeuanganDto> mapToDto(List<BukuUtama>list){
        return list.stream().map(b -> {
            LaporanKeuanganDto dto = new LaporanKeuanganDto();
            dto.setTraceNumber(b.getTraceNumber());
            dto.setTanggal(b.getTanggal());
            dto.setKodeTransaksi(b.getKodeTransaksi());
            dto.setDeskripsi(b.getDeskripsi());
            dto.setNominalMasuk(b.getNominalMasuk());
            dto.setNominalKeluar(b.getNominalKeluar());
            dto.setJenisRekening(b.getJenisRekening());
//            dto.setKodeAkun(b.getAkun().getKodeAkun());
//            dto.setNamaAkun(b.getAkun().getNamaAkun());
//            return dto;
//        }).toList();
            if (b.getAkun() != null) {
                dto.setKodeAkun(b.getAkun().getKodeAkun());
                dto.setNamaAkun(b.getAkun().getNamaAkun());
            } else {
                dto.setKodeAkun("N/A");
                dto.setNamaAkun("N/A");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/export/excel/histori-saldo")
    public void exportHistoriSaldoToExcel(@RequestParam LocalDate tanggalAwal,
                                          @RequestParam LocalDate tanggalAkhir,
                                          HttpServletResponse response) throws IOException {
        List<BukuUtama> list = bukuUtamaRepository.findByTanggalBetween(tanggalAwal.atStartOfDay(), tanggalAkhir.atTime(LocalTime.MAX));
        if (list.isEmpty()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.getWriter().write("Tidak ada data untuk periode yang dipilih");
            return;
        }
        List<LaporanKeuanganDto> dtoList = list.stream().map(BukuUtamaMapper::toLaporanKeuanganDto).toList();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Histori_Saldo.xlsx");

        response.getOutputStream().write(exportService.exportToExcel(dtoList));
        response.getOutputStream().flush();
    }
    @GetMapping("/export/pdf/histori-saldo")
    public void exportHistoriSaldoToPDF(
            @RequestParam LocalDate tanggalAwal,
            @RequestParam LocalDate tanggalAkhir,
            HttpServletResponse response) {

        try {
            List<BukuUtama> list = bukuUtamaRepository.findByTanggalBetween(
                    tanggalAwal.atStartOfDay(),
                    tanggalAkhir.atTime(LocalTime.MAX)
            );

            if (list.isEmpty()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                response.getWriter().write("Tidak ada data untuk periode yang dipilih");
                return;
            }

            List<LaporanKeuanganDto> dtoList = list.stream()
                    .map(BukuUtamaMapper::toLaporanKeuanganDto)
                    .collect(Collectors.toList());

            byte[] pdfBytes = exportService.exportToPDF(dtoList);

            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=Laporan_Histori_Saldo_" + LocalDate.now() + ".pdf");
            response.setContentLength(pdfBytes.length);

            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                response.getWriter().write("Error generating PDF: " + e.getMessage());
            } catch (IOException ex) {
                log.error("Failed to write error response", ex);
            }
            log.error("Failed to export PDF", e);
        }
    }
}
