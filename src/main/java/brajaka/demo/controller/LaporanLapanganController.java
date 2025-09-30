package brajaka.demo.controller;

import brajaka.demo.config.PaginationUtil;
import brajaka.demo.dto.LaporanLapanganRequest;
import brajaka.demo.model.LaporanLapangan;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.LaporanLapanganRepository;
import brajaka.demo.service.FileStrorageService;
import brajaka.demo.service.LaporanLapanganService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Page<LaporanLapangan> getALlLaporan(
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
        return laporanLapanganRepository.findAll(spec, PageRequest.of(page, size, sortOrder));
    }

    @PostMapping("/upload")
    public ResponseEntity<LaporanLapangan> uploadLaporan(
            @RequestPart("request") @Valid LaporanLapanganRequest request,
            @RequestParam("bukti")MultipartFile bukti) {

            LaporanLapangan saved = laporanLapanganService.createLaporan(request, bukti);
            return ResponseEntity.ok(saved);
    }
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
        Resource resource = fileStrorageService.loadFileAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
