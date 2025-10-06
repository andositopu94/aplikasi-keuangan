package brajaka.demo.controller;

import brajaka.demo.config.PaginationUtil;
import brajaka.demo.dto.LaporanLapanganMapper;
import brajaka.demo.dto.LaporanLapanganRequest;
import brajaka.demo.dto.LaporanLapanganResponse;
import brajaka.demo.model.LaporanLapangan;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.LaporanLapanganRepository;
import brajaka.demo.service.FileStrorageService;
import brajaka.demo.service.LaporanLapanganService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/laporan-lapangan")
public class LaporanLapanganController {

    private final LaporanLapanganRepository laporanLapanganRepository;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;
    private final FileStrorageService fileStrorageService;
    private final LaporanLapanganService laporanLapanganService;

    public LaporanLapanganController(LaporanLapanganRepository laporanLapanganRepository,
                                     AkunRepository akunRepository,
                                     KegiatanRepository kegiatanRepository,
                                     FileStrorageService fileStrorageService,
                                     LaporanLapanganService laporanLapanganService){
        this.laporanLapanganRepository = laporanLapanganRepository;
        this.akunRepository = akunRepository;
        this.kegiatanRepository = kegiatanRepository;
        this.fileStrorageService = fileStrorageService;
        this.laporanLapanganService = laporanLapanganService;
    }

    @GetMapping
    public Page<LaporanLapanganResponse> getALlLaporan(
            @RequestParam(required = false) String kodeAkun,
            @RequestParam(required = false) String kodeKegiatan,
            @RequestParam(required = false) LocalDate tanggal,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "tanggal, asc") String[] sort){

        Specification<LaporanLapangan> spec = Specification.where(null);

        if (kodeAkun !=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("akun").get("kodeAkun"), kodeAkun));
        }
        if (kodeKegiatan != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("kegiatan").get("kodeKegiatan"), kodeKegiatan));
        }
        if (tanggal != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("tanggal"), tanggal.toString() + "%"));
        }
        if (search != null && !search.isEmpty()){
            String likePattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("kodeKegiatan")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("kodeLapangan")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("akun").get("namaAkun")), likePattern));
            });
        }
        Sort sortOrder = PaginationUtil.parseSort(sort);
        return laporanLapanganRepository.findAll(spec, PageRequest.of(page, size, sortOrder))
                .map(LaporanLapanganMapper::toResponse);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<LaporanLapanganResponse> uploadLaporan(
            @RequestPart("request") @Valid LaporanLapanganRequest request,
            @RequestParam("bukti")MultipartFile bukti) {

            LaporanLapangan saved = laporanLapanganService.createLaporan(request, bukti);
            return ResponseEntity.ok(LaporanLapanganMapper.toResponse(saved));

    }
    @PutMapping("/{id}")
    public ResponseEntity<LaporanLapanganResponse>updateLaporan(@PathVariable Long id,
                                                                @RequestPart("request") @Valid LaporanLapanganRequest request,
                                                                @RequestParam(value = "bukti", required = false) MultipartFile bukti){
        LaporanLapangan updated = laporanLapanganService.updateLaporan(id, request, bukti);
        return ResponseEntity.ok(LaporanLapanganMapper.toResponse(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteLaporan(@PathVariable Long id){
        laporanLapanganRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

@GetMapping("/files/{filename:.+}")
public ResponseEntity<byte[]> getFile(@PathVariable String filename) throws IOException {
    Path filePath = Paths.get("uploads").resolve(filename).normalize();

    if (!Files.exists(filePath)) {
        return ResponseEntity.notFound().build();
    }

    byte[] fileBytes = Files.readAllBytes(filePath);

    // Deteksi MIME type otomatis
    String mimeType = Files.probeContentType(filePath);
    if (mimeType == null) {
        mimeType = "application/octet-stream"; // fallback
    }

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mimeType))
            .body(fileBytes);
}

}
