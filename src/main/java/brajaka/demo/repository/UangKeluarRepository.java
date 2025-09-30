package brajaka.demo.repository;

import brajaka.demo.model.BukuUtama;
import brajaka.demo.model.UangKeluar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface UangKeluarRepository extends JpaRepository<UangKeluar,String>, JpaSpecificationExecutor<UangKeluar> {
    List<BukuUtama> findByTanggalBetween(LocalDateTime start, LocalDateTime end);
}
