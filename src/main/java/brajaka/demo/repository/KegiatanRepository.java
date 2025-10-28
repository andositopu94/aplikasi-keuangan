package brajaka.demo.repository;

import brajaka.demo.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KegiatanRepository extends JpaRepository<Kegiatan, String> {
    Optional<Kegiatan>findByKodeKegiatan(String kodeKegiatan);
}
