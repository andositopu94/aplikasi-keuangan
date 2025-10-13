package brajaka.demo.repository;

import brajaka.demo.model.BukuUtama;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BukuUtamaRepository extends JpaRepository<BukuUtama, String>, JpaSpecificationExecutor<BukuUtama> {
    @Query("SELECT COALESCE(SUM(bu.nominalMasuk), 0) - COALESCE(SUM(bu.nominalKeluar), 0) " +
    "FROM BukuUtama bu WHERE UPPER(bu.jenisRekening) = 'Cash' AND bu.tanggal < :tanggal")
    BigDecimal getTotalSaldoCashSebelumnya(@Param("tanggal") LocalDateTime tanggal);

    @Query("SELECT COALESCE(SUM(bu.nominalMasuk), 0) - COALESCE(SUM(bu.nominalKeluar), 0) " +
            "FROM BukuUtama bu WHERE UPPER(bu.jenisRekening) = 'Main BCA' AND bu.tanggal < :tanggal")
    BigDecimal getTotalSaldoMainBCASebelumnya(@Param("tanggal") LocalDateTime tanggal);

    @Query("SELECT COALESCE(SUM(bu.nominalMasuk), 0) - COALESCE(SUM(bu.nominalKeluar), 0) " +
            "FROM BukuUtama bu WHERE UPPER(bu.jenisRekening) = 'BCA Dir' AND bu.tanggal < :tanggal")
    BigDecimal getTotalSaldoMainBCADirSebelumnya(@Param("tanggal") LocalDateTime tanggal);

    @Query("SELECT COALESCE(SUM(bu.nominalMasuk), 0) - COALESCE(SUM(bu.nominalKeluar), 0) " +
            "FROM BukuUtama bu WHERE UPPER(bu.jenisRekening) = 'PCU' AND bu.tanggal < :tanggal")
    BigDecimal getTotalSaldoPCUSebelumnya(@Param("tanggal") LocalDateTime tanggal);


//    @Transactional(readOnly = true)
//    Optional<BukuUtama> findTop1ByJenisRekeningIgnoreCaseAndTanggalLessThanOrderByTanggalDesc
//            (@Param("jenisRekening") String jenisRekening,
//             @Param("tanggal") LocalDateTime tanggal);

    @Query("SELECT b FROM BukuUtama b WHERE UPPER(b.jenisRekening) = UPPER(:jenisRekening) AND b.tanggal < :tanggal ORDER BY b.tanggal DESC LIMIT 1")
    Optional<BukuUtama> findTop1ByJenisRekeningAndTanggalBefore(@Param("jenisRekening") String jenisRekening,
                                                                @Param("tanggal") LocalDateTime tanggal);

    @Transactional(readOnly = true)
    Optional<BukuUtama> findFirstByJenisRekeningAndTanggalBeforeOrderByTanggalDesc(String jenisRekening, LocalDateTime tanggal);


    @Query("SELECT b FROM BukuUtama b WHERE b.akun.kodeAkun = :kodeAkun")
    List<BukuUtama>findByKodeAkun(@Param("kodeAkun") String kodeAkun);

    @Query("SELECT b FROM BukuUtama b WHERE b.kegiatan.kodeKegiatan = :kodeKegiatan")
    List<BukuUtama>findByKodeKegiatan(@Param("kodeKegiatan") String kodeKegiatan);
    List<BukuUtama>findByTanggalBetween(LocalDateTime start, LocalDateTime end);


    @Query("SELECT b.akun.kodeAkun, b.akun.namaAkun, SUM(b.nominalMasuk), SUM(b.nominalKeluar) " +
            "FROM BukuUtama b WHERE b.akun IS NOT NULL GROUP BY b.akun.kodeAkun, b.akun.namaAkun")
    List<Object[]> findTotalPerKodeAkun();

    @Query("SELECT b.kegiatan.kodeKegiatan, b.kegiatan.namaKegiatan, SUM(b.nominalMasuk), SUM(b.nominalKeluar) " +
            "FROM BukuUtama b WHERE b.kegiatan IS NOT NULL GROUP BY b.kegiatan.kodeKegiatan, b.kegiatan.namaKegiatan")
    List<Object[]> findTotalPerKodeKegiatan();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BukuUtama b WHERE b.tanggal > :dariTanggal ORDER BY b.tanggal ASC")
    List<BukuUtama>findByTanggalAfterOrderByTanggalAsc(@Param("dariTanggal") LocalDateTime dariTanggal);

    @Query("SELECT b FROM BukuUtama b WHERE UPPER(b.jenisRekening) = UPPER(:jenisRekening) AND b.tanggal BETWEEN :start AND :end")
    List<BukuUtama> findByJenisRekeningAndTanggalBetween(
            @Param("jenisRekening") String jenisRekening,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @EntityGraph(attributePaths = {"akun", "kegiatan"})
    Page<BukuUtama> findAll(Specification<BukuUtama> spec, Pageable pageable);
}
