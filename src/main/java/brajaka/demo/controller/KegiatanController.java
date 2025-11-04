package brajaka.demo.controller;

import brajaka.demo.dto.KegiatanDto;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.service.KegiatanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/kegiatan")
public class KegiatanController {

    @Autowired
    private KegiatanRepository repository;

    @Autowired
    private KegiatanService kegiatanService;


    @GetMapping
    public Page<KegiatanDto> getAllKegiatan(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) { // Ambil banyak untuk dropdown
        Pageable pageable = PageRequest.of(page, size);


        return repository.findAll(pageable)
                .map(kegiatan -> new KegiatanDto(kegiatan.getKodeKegiatan(), kegiatan.getNamaKegiatan()));

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISI')")
    public ResponseEntity<KegiatanDto> createKegiatan(@RequestBody @Valid KegiatanDto dto) { // Terima dan kembalikan DTO
        // Konversi DTO ke Entity untuk disimpan
        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setKodeKegiatan(dto.getKodeKegiatan());
        kegiatan.setNamaKegiatan(dto.getNamaKegiatan());
        Kegiatan savedKegiatan = repository.save(kegiatan);

        KegiatanDto savedDto = new KegiatanDto(savedKegiatan.getKodeKegiatan(), savedKegiatan.getNamaKegiatan());
        return ResponseEntity.status(201).body(savedDto);
    }

    @GetMapping("/{kode}")
    public ResponseEntity<KegiatanDto> getKegiatanByKode(@PathVariable String kode) {
        return repository.findById(kode)
                .map(kegiatan -> new KegiatanDto(kegiatan.getKodeKegiatan(), kegiatan.getNamaKegiatan()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{kode}")
    @PreAuthorize("hasRole('SUPERVISI')")
    public ResponseEntity<?> deleteKegiatan(@PathVariable String kode) {
        try {
            kegiatanService.deleteKegiatanIfUnused(kode);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kegiatan tidak dapat dihapus karena masih direferensi di entitas lain.");
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("error", "Terjadi kesalahan internal"));
        }
    }
    @PutMapping("/{kode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISI')")
    public ResponseEntity<KegiatanDto> updateKegiatan(@PathVariable String kode, @RequestBody @Valid KegiatanDto dto) {
        if (!repository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }


        Kegiatan existingKegiatan = repository.findById(kode).orElseThrow(() -> new RuntimeException("Kegiatan tidak ditemukan"));


        existingKegiatan.setNamaKegiatan(dto.getNamaKegiatan());


        Kegiatan updatedKegiatan = repository.save(existingKegiatan);


        KegiatanDto updatedDto = new KegiatanDto(updatedKegiatan.getKodeKegiatan(), updatedKegiatan.getNamaKegiatan());

        return ResponseEntity.ok(updatedDto);
    }
}