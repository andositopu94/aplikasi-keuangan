package brajaka.demo.controller;

import brajaka.demo.dto.AkunDto;
import brajaka.demo.model.Akun;
import brajaka.demo.repository.AkunRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/akun")
public class AkunController {

        private final AkunRepository repository;

        public AkunController(AkunRepository repository) {
            this.repository = repository;
        }

        @GetMapping
        public Page<Akun> getAllAkun(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
            return repository.findAll(PageRequest.of(page, size));
        }

        @PostMapping
        public ResponseEntity<?> createAkun(@RequestBody AkunDto dto) {
            if (repository.existsById(dto.getKodeAkun())) {
                return ResponseEntity.badRequest().body(Map.of("Kode Akun", "Sudah digunakan"));
            }

            Akun akun = new Akun();
            akun.setKodeAkun(dto.getKodeAkun());
            akun.setNamaAkun(dto.getNamaAkun());
            return ResponseEntity.ok(repository.save(akun));
        }

        @GetMapping("/{kode}")
        public ResponseEntity<Akun> getAkunByKode(@PathVariable String kode) {
            return repository.findById(kode)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("Akun tidak ditemukan"));
        }

        @DeleteMapping("/{kode}")
        public ResponseEntity<Void> deleteAkun(@PathVariable String kode) {
            if (!repository.existsById(kode)) {
                return ResponseEntity.notFound().build();
            }
            repository.deleteById(kode);
            return ResponseEntity.noContent().build();
        }

        @PutMapping("/{kode}")
        public ResponseEntity<?>updateAkun(@PathVariable String kode,
                                              @Valid @RequestBody AkunDto dto)
        {
            if (!repository.existsById(kode)) {
                return ResponseEntity.notFound().build();
            }
            if (!kode.equals(dto.getKodeAkun())) {
                if (repository.existsById(dto.getKodeAkun()))
                    return ResponseEntity.badRequest().body(Map.of("Kode Akun", "Sudah digunakan"));
            }
            Akun akun = new Akun();
            akun.setKodeAkun(dto.getKodeAkun());
            akun.setNamaAkun(dto.getNamaAkun());

            if (!kode.equals(dto.getKodeAkun())) {repository.deleteById(kode);}

            return ResponseEntity.ok(repository.save(akun));
        }
    }

