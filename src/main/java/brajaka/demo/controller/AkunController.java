package brajaka.demo.controller;

import brajaka.demo.dto.AkunDto;
import brajaka.demo.model.Akun;
import brajaka.demo.repository.AkunRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/akun")
public class AkunController {

    @Autowired
    private AkunRepository repository;


    @GetMapping
    public Page<AkunDto> getAllAkun(
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);


        return repository.findAll(pageable)
                .map(akun -> new AkunDto(akun.getKodeAkun(), akun.getNamaAkun()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISI')")
    public ResponseEntity<AkunDto> createAkun(@RequestBody @Valid AkunDto dto) {

        Akun akun = new Akun();
        akun.setKodeAkun(dto.getKodeAkun());
        akun.setNamaAkun(dto.getNamaAkun());
        Akun savedAkun = repository.save(akun);

        AkunDto savedDto = new AkunDto(savedAkun.getKodeAkun(), savedAkun.getNamaAkun());
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/{kode}")
    public ResponseEntity<AkunDto> getAkunByKode(@PathVariable String kode) {
        return repository.findById(kode)
                .map(akun -> new AkunDto(akun.getKodeAkun(), akun.getNamaAkun()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{kode}")
    @PreAuthorize("hasRole('SUPERVISI')")
    public ResponseEntity<Void> deleteAkun(@PathVariable String kode) {
        if (!repository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(kode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{kode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISI')")
    public ResponseEntity<AkunDto> updateAkun(@PathVariable String kode, @RequestBody @Valid AkunDto dto) {
        if (!repository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }


        Akun existingAkun = repository.findById(kode).orElseThrow(() -> new RuntimeException("Akun tidak ditemukan"));


        existingAkun.setNamaAkun(dto.getNamaAkun());


        Akun updatedAkun = repository.save(existingAkun);


        AkunDto updatedDto = new AkunDto(updatedAkun.getKodeAkun(), updatedAkun.getNamaAkun());

        return ResponseEntity.ok(updatedDto);
    }
}