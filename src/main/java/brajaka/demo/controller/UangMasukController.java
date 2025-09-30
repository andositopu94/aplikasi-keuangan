package brajaka.demo.controller;

import brajaka.demo.config.PaginationUtil;
import brajaka.demo.dto.UangMasukDto;
import brajaka.demo.dto.UangMasukMapper;
import brajaka.demo.model.UangMasuk;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.UangMasukRepository;
import brajaka.demo.service.BukuUtamaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/uang-masuk")
public class UangMasukController {
    private final UangMasukRepository repository;
    private final BukuUtamaService bukuUtamaService;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;


    public UangMasukController(UangMasukRepository repository, BukuUtamaService bukuUtamaService,
                               AkunRepository akunRepository, KegiatanRepository kegiatanRepository) {
        this.repository = repository;
        this.bukuUtamaService= bukuUtamaService;
        this.akunRepository = akunRepository;
        this.kegiatanRepository = kegiatanRepository;
    }

    @GetMapping
    public Page<UangMasukDto> getAll(@RequestParam(required = false) String kodeAkun,
                                     @RequestParam(required = false) LocalDate tanggal,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "tanggal, asc") String [] sort) {
        Specification<UangMasuk> spec = Specification.where(null);

        if (kodeAkun != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("akun").get("kodeAkun"), kodeAkun));
        }
        if (tanggal != null){
            String start = tanggal.toString() + "00:00:00";
            String end = tanggal.toString() + "23:59:59";
            spec = spec.and((root, query, cb) -> cb.between(root.get("tanggal"), start, end));
    }


        Sort sortOrder = PaginationUtil.parseSort(sort);
        Page<UangMasuk> uangMasukPage = repository.findAll(spec, PageRequest.of(page,size,sortOrder));

        return uangMasukPage.map(UangMasukMapper::toDto);
    }


    @PostMapping
    public UangMasuk create(@RequestBody UangMasuk uangMasuk) {
        String traceNumber = UUID.randomUUID().toString();
        uangMasuk.setTraceNumber(traceNumber);
        UangMasuk saved = repository.save(uangMasuk);

        bukuUtamaService.createFromUangMasuk(saved);
        return saved;
    }

    @GetMapping("/{trace}")
    public ResponseEntity<UangMasuk> getById(@PathVariable String trace) {
        return repository.findById(trace)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Data tidak ditemukan"));
    }

    @PutMapping("/{trace}")
    public UangMasuk update(@PathVariable String trace, @RequestBody UangMasuk updated) {
        UangMasuk existing = repository.findById(trace)
                .orElseThrow(() -> new RuntimeException("Data tidak ditemukan"));

        // Update field
        existing.setSumberRekening(updated.getSumberRekening());
        existing.setNominal(updated.getNominal());
        existing.setDeskripsi(updated.getDeskripsi());

        UangMasuk saved = repository.save(existing);

        // Update di BukuUtama
        bukuUtamaService.updateFromUangMasuk(saved);
        bukuUtamaService.updateSaldoBerantai(existing.getTanggal());

        return saved;
    }

    @DeleteMapping("/{trace}")
    public void delete(@PathVariable String trace) {
        repository.deleteById(trace);
    }

    private String generateKodeTransaksi(String prefix) {
        String dateSuffix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        Long count = repository.count() + 1;
        return prefix + dateSuffix + "/" + String.format("%04d", count);
    }
}
