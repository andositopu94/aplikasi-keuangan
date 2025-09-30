package brajaka.demo.controller;

import brajaka.demo.model.Kegiatan;
import brajaka.demo.repository.KegiatanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        public Kegiatan create(@RequestBody Kegiatan kegiatan) {
            return repository.save(kegiatan);
        }

        @GetMapping("/{kode}")
        public Kegiatan getById(@PathVariable String kode) {
            return repository.findById(kode)
                    .orElseThrow(() -> new RuntimeException("Kegiatan tidak ditemukan"));
        }

        @DeleteMapping("/{kode}")
        public void delete(@PathVariable String kode) {
            repository.deleteById(kode);
        }
}
