package brajaka.demo.controller;

import brajaka.demo.config.PaginationUtil;
import brajaka.demo.dto.UangKeluarDto;
import brajaka.demo.dto.UangKeluarMapper;
import brajaka.demo.dto.UangKeluarRequest;
import brajaka.demo.model.Akun;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.model.UangKeluar;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.UangKeluarRepository;
import brajaka.demo.service.BukuUtamaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uang-keluar")
public class UangKeluarController {

    private final UangKeluarRepository uangKeluarRepository;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;
    private final BukuUtamaService bukuUtamaService;

    public UangKeluarController(UangKeluarRepository uangKeluarRepository,
                                AkunRepository akunRepository,
                                KegiatanRepository kegiatanRepository, BukuUtamaService bukuUtamaService) {
        this.uangKeluarRepository = uangKeluarRepository;
        this.akunRepository= akunRepository;
        this.kegiatanRepository= kegiatanRepository;
        this.bukuUtamaService=bukuUtamaService;
    }

    @GetMapping
    public Page<UangKeluar> getAllUangKeluar(
            @RequestParam(required = false) String kodeAkun,
            @RequestParam(required = false) LocalDate tanggal,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "tanggal, asc") String [] sort){

        Specification<UangKeluar> spec = Specification.where(null);

        if (kodeAkun != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("akun").get("kodeAkun"), kodeAkun));
        }
        if (tanggal != null){
            spec=spec.and((root, query, cb) -> cb.like(root.get("tanggal"), tanggal.toString() + "%"));
        }

        if (search !=null && !search.isEmpty()) {
            String likePattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, criteriaBuilder) -> {return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deskripsi")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("kodeTransaksi")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("rekeningTujuan")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("akun").get("namaAkun")), likePattern));
            });
        }
        Sort sortOrder = PaginationUtil.parseSort(sort);
        return uangKeluarRepository.findAll(spec, PageRequest.of(page, size, sortOrder));
    }

    @PostMapping
    public ResponseEntity<UangKeluarDto> create(@Valid @RequestBody UangKeluarRequest request, BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }
        Akun akun = akunRepository.findById(request.getKodeAkun())
                .orElseThrow(() -> new RuntimeException("Akun Tidak Ditemukan"));
        Kegiatan kegiatan = kegiatanRepository.findById(request.getKodeKegiatan())
                .orElseThrow(() -> new RuntimeException("Kegiatan Tidak Ditemukan"));

        String traceNumber = UUID.randomUUID().toString();
        String kodeTransaksi = generateKodeTransaksi();


        //validasi uniq number
        if (uangKeluarRepository.existsById(traceNumber)){
            throw new RuntimeException("Trace Number Sudah Digunakan");
        }

        UangKeluar uangKeluar = new UangKeluar();
        uangKeluar.setTraceNumber(UUID.randomUUID().toString());
        uangKeluar.setTanggal(LocalDateTime.now());
        uangKeluar.setKodeTransaksi(kodeTransaksi);
        uangKeluar.setRekeningTujuan(request.getRekeningTujuan());
        uangKeluar.setNominal(request.getNominal());
        uangKeluar.setDeskripsi(request.getDeskripsi());
        uangKeluar.setAkun(akun);
        uangKeluar.setKegiatan(kegiatan);

        UangKeluar saved = uangKeluarRepository.save(uangKeluar);
        bukuUtamaService.updateSaldoBerantai(uangKeluar.getTanggal());
        return ResponseEntity.ok(UangKeluarMapper.toDTO(saved));
    }
    private String generateKodeTransaksi(){
        String prefix = "UKEL/";
        String dateSuffix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        Long count = uangKeluarRepository.count() + 1;
                return prefix + dateSuffix + "/" + String.format("%04d", count);
    }
}