package brajaka.demo.repository;

import brajaka.demo.model.LaporanLapangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LaporanLapanganRepository extends JpaRepository<LaporanLapangan,Long>,
        JpaSpecificationExecutor<LaporanLapangan> {
}
