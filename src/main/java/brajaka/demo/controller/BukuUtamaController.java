package brajaka.demo.controller;

import brajaka.demo.config.PaginationUtil;
import brajaka.demo.dto.BukuUtamaDto;
import brajaka.demo.dto.BukuUtamaMapper;
import brajaka.demo.dto.HistoriSaldoDto;
import brajaka.demo.dto.LaporanKeuanganDto;
import brajaka.demo.model.Akun;
import brajaka.demo.model.BukuUtama;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.BukuUtamaRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.service.BukuUtamaService;
import brajaka.demo.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buku-utama")
public class BukuUtamaController {
    private final BukuUtamaRepository bukuUtamaRepository;
    private final BukuUtamaService bukuUtamaService;
    private final ExportService exportService;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;

    public BukuUtamaController(BukuUtamaRepository bukuUtamaRepository, BukuUtamaService bukuUtamaService, ExportService exportService,
                               AkunRepository akunRepository,
                               KegiatanRepository kegiatanRepository){
        this.bukuUtamaRepository = bukuUtamaRepository;
        this.bukuUtamaService= bukuUtamaService;
        this.exportService = exportService;
        this.akunRepository = akunRepository;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    public Page<BukuUtamaDto>getAll(@RequestParam(required = false) String kodeAkun,
                                    @RequestParam(required = false) String jenisRekening,
                                    @RequestParam(required = false) String tanggal,
                                    @RequestParam(required = false) String search,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "tanggal, asc") String[] sort){
        Specification<BukuUtama> spec = Specification.where(null);

        if (kodeAkun != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("akun").get("kodeAkun"), kodeAkun));
        }

        if (jenisRekening != null && !jenisRekening.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("jenisRekening")), jenisRekening.trim().toUpperCase()));
        }

        if (tanggal != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("tanggal").as(String.class), "%" + tanggal + "%"));
        }
        if (search != null && !search.isEmpty()){
            String likePattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                return cb.or(
                        cb.like(cb.lower(root.get("deskripsi")), likePattern),
                        cb.like(cb.lower(root.get("kodeTransaksi")), likePattern),
                        cb.like(cb.lower(root.get("rekeningTujuan")), likePattern),
                        cb.like(cb.lower(root.get("akun").get("namaAkun")),likePattern)
                );
            });
        }
        Sort setOrder = PaginationUtil.parseSort(sort);
        return bukuUtamaRepository.findAll(spec, PageRequest.of(page, size, setOrder))
                .map(BukuUtamaMapper::toDTO);
    }

    //CRUD
    @GetMapping("/{id}")
    public ResponseEntity<BukuUtamaDto> getBukuUtamaById(@PathVariable String id){
        return bukuUtamaRepository.findById(id)
                .map(BukuUtamaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> createBukuUtama(@RequestBody @Valid BukuUtamaDto bukuUtamaDto, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try{
        if (bukuUtamaDto.getTanggal().toLocalDate().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Map.of("Tanggal", "Tanggal tidak boleh lebih dari hari ini"));
        }

        Akun akun = akunRepository.findById(bukuUtamaDto.getKodeAkun()).orElseThrow(() ->
                new RuntimeException("Akun tidak ditemukan!"));
        Kegiatan kegiatan = kegiatanRepository.findById(bukuUtamaDto.getKodeKegiatan())
                .orElseThrow(() -> new RuntimeException("Kode kegiatan tidak ditemukan!"));

        BukuUtama entity= BukuUtamaMapper.toEntity(bukuUtamaDto, akunRepository, kegiatanRepository);
        entity.setAkun(akun);
        entity.setKegiatan(kegiatan);


        BukuUtama saved= bukuUtamaRepository.save(entity);
        bukuUtamaService.updateSaldoBerantai(saved.getTanggal());

        return ResponseEntity.status(201).body(BukuUtamaMapper.toDTO(saved));
        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBukuUtama(@PathVariable String id){
        BukuUtama existing= bukuUtamaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data Tidak Ditemukan"));
        if (!bukuUtamaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        LocalDateTime tanggalTransaksi = existing.getTanggal();
        bukuUtamaRepository.deleteById(id);
        bukuUtamaService.updateSaldoBerantai(tanggalTransaksi);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/saldo")
    public Map<String, BigDecimal> getSaldoTerakir(){
        LocalDateTime now = LocalDateTime.now();
        Map<String, BigDecimal> saldoMap = new HashMap<>();

        saldoMap.put("Cash", bukuUtamaService.getSaldoTerakhirSampaiTanggal(now, "Cash"));
        saldoMap.put("Main BCA", bukuUtamaService.getSaldoTerakhirSampaiTanggal(now, "Main BCA"));
        saldoMap.put("BCA Dir", bukuUtamaService.getSaldoTerakhirSampaiTanggal(now, "BCA Dir"));
        saldoMap.put("PCU", bukuUtamaService.getSaldoTerakhirSampaiTanggal(now,"PCU"));

        return saldoMap;
    }

    @Cacheable(value = "historiAll", key = "#tanggalAwal.toString() + '_' + #tanggalAkhir.toString()")
    @GetMapping("/histori/all")
    public Map<String, List<HistoriSaldoDto>> getAllHistori (@RequestParam LocalDate tanggalAwal,
                                                          @RequestParam LocalDate tanggalAkhir) {
        Map<String, List<HistoriSaldoDto>> result = new HashMap<>();
        result.put("cash", bukuUtamaService.getHistoriSaldoCash(tanggalAwal, tanggalAkhir));
        result.put("mainBca", bukuUtamaService.getHistoriSaldoMainBCA(tanggalAwal, tanggalAkhir));
        result.put("bcaDir", bukuUtamaService.getHistoriSaldoBCADir(tanggalAwal, tanggalAkhir));
        result.put("pcu", bukuUtamaService.getHistoriSaldoPCU(tanggalAwal, tanggalAkhir));
        return result;
    }

    @GetMapping("/export/excel")
    public void exportExcel(@RequestParam LocalDate tanggalAwal,
                            @RequestParam LocalDate tanggalAkhir,
                            HttpServletResponse response) throws IOException{
        List<BukuUtama> list = bukuUtamaRepository.findByTanggalBetween(tanggalAwal.atStartOfDay(), tanggalAkhir.atTime(LocalTime.MAX));
        List<LaporanKeuanganDto> dtoList = list.stream().map(BukuUtamaMapper::toLaporanKeuanganDto)
                .toList();

        response.setContentType("/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Keuangan_" + LocalDate.now() + ".xlsx");

        response.getOutputStream().write(exportService.exportToExcel(dtoList));
        response.getOutputStream().flush();
    }
    @GetMapping("/export/pdf")
    public void exportPdf(@RequestParam LocalDate tanggalAwal,
                          @RequestParam LocalDate tanggalAkhir,
                          HttpServletResponse response) throws Exception {

        List<BukuUtama> list = bukuUtamaRepository.findByTanggalBetween(
                tanggalAwal.atStartOfDay(),
                tanggalAkhir.atTime(LocalTime.MAX)
        );

        List<LaporanKeuanganDto> dtoList = list.stream()
                .map(BukuUtamaMapper::toLaporanKeuanganDto)
                .toList();

        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Laporan_Keuangan.pdf");

        response.getOutputStream().write(exportService.exportToPDF(dtoList));
        response.getOutputStream().flush();
    }

    @PutMapping("/update-saldo")
    public ResponseEntity<String> updateSaldo(@RequestParam String tanggal){
        LocalDateTime parseTanggal = LocalDateTime.parse(tanggal);
        bukuUtamaService.updateSaldoBerantai(parseTanggal);
        return ResponseEntity.ok("Saldo Berhasil diperbaharui");
    }

    @PutMapping("/{traceNumber}")
    public ResponseEntity<BukuUtamaDto> updateBukuUtama(
            @PathVariable String traceNumber,
            @RequestBody @Valid BukuUtamaDto dto) {

        return bukuUtamaRepository.findById(traceNumber)
                .map(existing -> {
                    // 1. update scalar fields
                    existing.setTanggal(dto.getTanggal());
                    existing.setKodeTransaksi(dto.getKodeTransaksi());
                    existing.setJenisRekening(dto.getJenisRekening());
                    existing.setNominalMasuk(dto.getNominalMasuk());
                    existing.setNominalKeluar(dto.getNominalKeluar());
                    existing.setSumberRekening(dto.getSumberRekening());
                    existing.setRekeningTujuan(dto.getRekeningTujuan());
                    existing.setDeskripsi(dto.getDeskripsi());

                    // 2. update relations
                    Akun akun = akunRepository.findById(dto.getKodeAkun())
                            .orElseThrow(() -> new RuntimeException("Akun tidak ditemukan"));
                    existing.setAkun(akun);

                    Kegiatan kegiatan = kegiatanRepository.findById(dto.getKodeKegiatan())
                            .orElseThrow(() -> new RuntimeException("Kegiatan tidak ditemukan"));
                    existing.setKegiatan(kegiatan);

                    BukuUtama saved = bukuUtamaRepository.save(existing);
                    bukuUtamaService.updateSaldoBerantai(saved.getTanggal()); // re-calc saldo

                    return ResponseEntity.ok(BukuUtamaMapper.toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}