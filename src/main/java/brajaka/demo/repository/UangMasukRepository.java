package brajaka.demo.repository;

import brajaka.demo.model.BukuUtama;
import brajaka.demo.model.UangMasuk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface UangMasukRepository extends JpaRepository<UangMasuk, String>, JpaSpecificationExecutor<UangMasuk> {
    List<BukuUtama>findByTanggalBetween(LocalDateTime start, LocalDateTime end);
}
