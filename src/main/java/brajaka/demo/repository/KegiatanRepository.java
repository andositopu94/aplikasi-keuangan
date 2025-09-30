package brajaka.demo.repository;

import brajaka.demo.model.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KegiatanRepository extends JpaRepository<Kegiatan, String> {
}
