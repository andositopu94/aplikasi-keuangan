package brajaka.demo.controller;

import brajaka.demo.dto.KegiatanDto;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.repository.KegiatanRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/kegiatan")
    public class KegiatanController {

        private final KegiatanRepository repository;

        public KegiatanController(KegiatanRepository repository) {
            this.repository = repository;
        }

        @GetMapping
        public Page<Kegiatan> getAll(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            return repository.findAll(PageRequest.of(page, size));
        }

        @PostMapping
        public ResponseEntity<Kegiatan> create(@Valid @RequestBody KegiatanDto dto) {
            if (repository.existsById(dto.getKodeKegiatan())) {
                throw new RuntimeException("Kode Kegiatan sudah digunakan");
            }

            Kegiatan kegiatan = new Kegiatan();
            kegiatan.setKodeKegiatan(dto.getKodeKegiatan());
            kegiatan.setNamaKegiatan(dto.getNamaKegiatan());
            return ResponseEntity.ok(repository.save(kegiatan));
        }

        @GetMapping("/{kode}")
        public ResponseEntity<Kegiatan> getById(@PathVariable String kode) {
            return repository.findById(kode)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{kode}")
        public ResponseEntity<Void> delete(@PathVariable String kode) {
            if (!repository.existsById(kode)) {
                return ResponseEntity.notFound().build();
            }
            repository.deleteById(kode);
            return ResponseEntity.noContent().build();
        }

        @PutMapping("/{kode}")
        public ResponseEntity<Kegiatan> update(@PathVariable String kode,
                                               @Valid @RequestBody KegiatanDto dto) {
            if (!repository.existsById(kode)) {
                return ResponseEntity.notFound().build();
            }
            if (!kode.equals(dto.getKodeKegiatan())) {
                if (repository.existsById(dto.getKodeKegiatan())) {
                    throw new RuntimeException("Kode Kegiatan sudah digunakan");
                }
            }

            Kegiatan kegiatan = new Kegiatan();
            kegiatan.setKodeKegiatan(dto.getKodeKegiatan());
            kegiatan.setNamaKegiatan(dto.getNamaKegiatan());
            return ResponseEntity.ok(repository.save(kegiatan));
        }
}
