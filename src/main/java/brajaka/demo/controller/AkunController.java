package brajaka.demo.controller;

import brajaka.demo.model.Akun;
import brajaka.demo.repository.AkunRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        public Akun createAkun(@RequestBody Akun akun) {
            return repository.save(akun);
        }

        @GetMapping("/{kode}")
        public ResponseEntity<Akun> getAkunByKode(@PathVariable String kode) {
            return repository.findById(kode)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("Akun tidak ditemukan"));
        }

        @DeleteMapping("/{kode}")
        public void deleteAkun(@PathVariable String kode) {
            repository.deleteById(kode);
        }
    }

