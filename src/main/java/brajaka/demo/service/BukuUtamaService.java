package brajaka.demo.service;

import brajaka.demo.dto.BukuUtamaDto;
import brajaka.demo.dto.BukuUtamaMapper;
import brajaka.demo.dto.HistoriSaldoDto;
import brajaka.demo.dto.LaporanGroupDto;
import brajaka.demo.model.*;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.BukuUtamaRepository;
import brajaka.demo.repository.KegiatanRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
@Transactional
@Service
public class BukuUtamaService {
    private final BukuUtamaRepository bukuUtamaRepository;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;
    private static final Logger log = LoggerFactory.getLogger(BukuUtamaService.class);

    public BukuUtamaService(BukuUtamaRepository bukuUtamaRepository, AkunRepository akunRepository, KegiatanRepository kegiatanRepository) {
        this.bukuUtamaRepository = bukuUtamaRepository;
        this.akunRepository = akunRepository;
        this.kegiatanRepository = kegiatanRepository;

    }
    @Transactional
    public BukuUtamaDto createBukuUtama(BukuUtamaDto dto){

        BukuUtama entity = new BukuUtama();
        entity.setTraceNumber(UUID.randomUUID().toString());
        entity.setTanggal(dto.getTanggal());
        entity.setKodeTransaksi(dto.getKodeTransaksi());
        entity.setJenisRekening(dto.getJenisRekening());
        entity.setNominalMasuk(dto.getNominalMasuk() != null ? dto.getNominalMasuk() : BigDecimal.ZERO);
        entity.setNominalKeluar(dto.getNominalKeluar() != null ? dto.getNominalKeluar() : BigDecimal.ZERO);
        entity.setSumberRekening(dto.getSumberRekening());
        entity.setRekeningTujuan(dto.getRekeningTujuan());
        entity.setDeskripsi(dto.getDeskripsi());

        if (dto.getKodeAkun() != null && !dto.getKodeAkun().isEmpty()) {
            Akun akun = akunRepository.findById(dto.getKodeAkun())
                    .orElseThrow(() -> new RuntimeException("Akun Tidak Ditemukan!"));
            entity.setAkun(akun);
        }
        if (dto.getKodeKegiatan() != null && !dto.getKodeKegiatan().isEmpty()) {
            Kegiatan kegiatan = kegiatanRepository.findById(dto.getKodeKegiatan())
                    .orElseThrow(() -> new RuntimeException("Kegiatan Tidak Ditemukan!"));
            entity.setKegiatan(kegiatan);
        }
        BukuUtama saved= bukuUtamaRepository.save(entity);
//        updateSaldoAsync(saved.getTanggal());
        updateSaldoBerantai(saved.getTanggal());
        return BukuUtamaMapper.toDTO(saved);
    }

    @Async
    @Scheduled(fixedDelay = 5000)
    private void updateSaldoAsync(LocalDateTime tanggal) {
        try {
            updateSaldoBerantai(tanggal);
        }catch (Exception e){
            log.error("gagal update saldo async", e);
        }
    }

    public BukuUtamaDto updateBukuUtama(String id, BukuUtamaDto dto) {
        BukuUtama existing = bukuUtamaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku Utama tidak ditemukan"));

        existing.setTanggal(dto.getTanggal());
        existing.setKodeTransaksi(dto.getKodeTransaksi());
        existing.setNominalMasuk(dto.getNominalMasuk());
        existing.setNominalKeluar(dto.getNominalKeluar());
        existing.setSumberRekening(dto.getSumberRekening());
        existing.setRekeningTujuan(dto.getRekeningTujuan());
        existing.setJenisRekening(dto.getJenisRekening());
        existing.setDeskripsi(dto.getDeskripsi());
        existing.setSaldoMainBCA(dto.getSaldoMainBCA());
        existing.setSaldoPCU(dto.getSaldoPCU());
        existing.setSaldoBCADir(dto.getSaldoBCADir());
        existing.setSaldoCash(dto.getSaldoCash());

        if (dto.getKodeAkun() != null) {
            Akun akun =akunRepository.findById(dto.getKodeAkun())
                    .orElseThrow(() -> new RuntimeException("Akun Tidak Ditemukan"));
            existing.setAkun(akun);
        }
        if (dto.getKodeKegiatan() != null) {
            Kegiatan kegiatan = kegiatanRepository.findById(dto.getKodeKegiatan())
                    .orElseThrow(() -> new RuntimeException("Kegiatan Tidak Ditemukan"));
            existing.setKegiatan(kegiatan);
        }
        BukuUtama updated = bukuUtamaRepository.save(existing);
        updateSaldoBerantai(updated.getTanggal());
        return BukuUtamaMapper.toDTO(updated);
    }
    public void deleteBukuUtama(String id) {
        if (!bukuUtamaRepository.existsById(id)) {
            throw new RuntimeException("Data tidak ditemukan");
        }
        bukuUtamaRepository.deleteById(id);
    }

    public void createFromUangMasuk(UangMasuk uangMasuk) {
        if (bukuUtamaRepository.existsById(uangMasuk.getTraceNumber())) {
            throw new RuntimeException("Trace Number sudah terpakai");
        }
        BigDecimal previousSaldoCash = bukuUtamaRepository.getTotalSaldoCashSebelumnya(uangMasuk.getTanggal());

        BukuUtama bukuUtama = new BukuUtama();
        bukuUtama.setTraceNumber(uangMasuk.getTraceNumber());
        bukuUtama.setTanggal(uangMasuk.getTanggal());
        bukuUtama.setKodeTransaksi(uangMasuk.getKodeTransaksi());
        bukuUtama.setNominalMasuk(uangMasuk.getNominal());
        bukuUtama.setNominalKeluar(BigDecimal.ZERO);
        bukuUtama.setSumberRekening(uangMasuk.getSumberRekening());
        bukuUtama.setRekeningTujuan(null);
        bukuUtama.setDeskripsi(bukuUtama.getDeskripsi());
        bukuUtama.setSaldoMainBCA(bukuUtama.getSaldoMainBCA());
        bukuUtama.setSaldoBCADir(bukuUtama.getSaldoBCADir());
        bukuUtama.setSaldoPCU(bukuUtama.getSaldoPCU());
        bukuUtama.setSaldoCash(bukuUtama.getSaldoCash());
        bukuUtama.setAkun(uangMasuk.getAkun());
        bukuUtama.setKegiatan(uangMasuk.getKegiatan());

        if ("1-101".equals(uangMasuk.getAkun().getKodeAkun())) {
            bukuUtama.setJenisRekening("Cash");
            bukuUtama.setSaldoCash(previousSaldoCash.add(uangMasuk.getNominal()));
        } else if ("1-102".equals(uangMasuk.getAkun().getKodeAkun())) {
            bukuUtama.setJenisRekening("Main BCA");
            BigDecimal previousSaldoMainBCA = bukuUtamaRepository.getTotalSaldoMainBCASebelumnya(uangMasuk.getTanggal());
            bukuUtama.setSaldoMainBCA(previousSaldoMainBCA.add(uangMasuk.getNominal()));
        } else if ("1-103".equals(uangMasuk.getAkun().getKodeAkun())) {
            bukuUtama.setSaldoBCADir(hitungSaldoBCADir(uangMasuk.getNominal(), BigDecimal.ZERO));
        } else if ("1-104".equals(uangMasuk.getAkun().getKodeAkun())) {
            bukuUtama.setSaldoPCU(hitungSaldoPCU(uangMasuk.getNominal(), BigDecimal.ZERO));
        }

        bukuUtamaRepository.save(bukuUtama);
    }

    public void createFromUangKeluar(UangKeluar uangKeluar) {
        if (bukuUtamaRepository.existsById(uangKeluar.getTraceNumber())) {
            throw new RuntimeException("Trace Number sudah terpakai");
        }

        // Ambil saldo terakhir sebelum tanggal transaksi
        BigDecimal previousSaldoCash = bukuUtamaRepository.getTotalSaldoCashSebelumnya(uangKeluar.getTanggal());

        BukuUtama bukuUtama = new BukuUtama();
        bukuUtama.setTraceNumber(uangKeluar.getTraceNumber());
        bukuUtama.setTanggal(uangKeluar.getTanggal());
        bukuUtama.setKodeTransaksi(uangKeluar.getKodeTransaksi());
        bukuUtama.setNominalKeluar(uangKeluar.getNominal());
        bukuUtama.setNominalMasuk(BigDecimal.ZERO);
        bukuUtama.setSumberRekening(null);
        bukuUtama.setRekeningTujuan(uangKeluar.getRekeningTujuan());
        bukuUtama.setDeskripsi(uangKeluar.getDeskripsi());
        bukuUtama.setAkun(uangKeluar.getAkun());
        bukuUtama.setKegiatan(uangKeluar.getKegiatan());

        if ("1-101".equals(uangKeluar.getAkun().getKodeAkun())) {
            bukuUtama.setJenisRekening("");
            bukuUtama.setSaldoCash(previousSaldoCash.subtract(uangKeluar.getNominal()));
        } else if ("1-102".equals(uangKeluar.getAkun().getKodeAkun())) {
            bukuUtama.setJenisRekening("");
            BigDecimal previousSaldoMainBCA = bukuUtamaRepository.getTotalSaldoMainBCASebelumnya(uangKeluar.getTanggal());
            bukuUtama.setSaldoMainBCA(previousSaldoMainBCA.subtract(uangKeluar.getNominal()));
        }

        bukuUtamaRepository.save(bukuUtama);
    }

    public void updateFromUangMasuk(UangMasuk uangMasuk) {
        if (bukuUtamaRepository.existsById(uangMasuk.getTraceNumber())) {
            BukuUtama existing = bukuUtamaRepository.findById(uangMasuk.getTraceNumber())
                    .orElseThrow(() -> new RuntimeException("Data Buku Utama Tidak di Temukan"));

            existing.setTanggal(uangMasuk.getTanggal());
            existing.setKodeTransaksi(uangMasuk.getKodeTransaksi());
            existing.setNominalMasuk(uangMasuk.getNominal());
            existing.setSumberRekening(uangMasuk.getSumberRekening());
            existing.setDeskripsi(uangMasuk.getDeskripsi());
            existing.setAkun(uangMasuk.getAkun());
            existing.setKegiatan(uangMasuk.getKegiatan());

            bukuUtamaRepository.save(existing);
        } else {
            createFromUangMasuk(uangMasuk);  //jika belum ada buat baru
        }
    }

    //jika ada saldo yang diedit pd tanggal sebelumnya
    @Transactional(rollbackFor = Exception.class)
    public void updateSaldoBerantai(LocalDateTime dariTanggal){
        List<BukuUtama> daftar = bukuUtamaRepository.findByTanggalAfterOrderByTanggalAsc(dariTanggal);
        if (daftar.isEmpty()) return;


        //inisialisasi saldo
        BigDecimal saldoCash = getSaldoTerakhirSampaiTanggal(dariTanggal,"Cash");
        BigDecimal saldoMainBCA = getSaldoTerakhirSampaiTanggal(dariTanggal, "Main BCA");
        BigDecimal saldoBCADir = getSaldoTerakhirSampaiTanggal(dariTanggal, "BCA Dir");
        BigDecimal saldoPCU = getSaldoTerakhirSampaiTanggal(dariTanggal, "PCU");

        for (BukuUtama buku : daftar) {

            String jenis = buku.getJenisRekening();
            BigDecimal masuk = buku.getNominalMasuk() != null ? buku.getNominalMasuk():BigDecimal.ZERO;
            BigDecimal keluar = buku.getNominalKeluar() != null ? buku.getNominalKeluar() : BigDecimal.ZERO;

            if ("Cash".equalsIgnoreCase(jenis)) {
                saldoCash = saldoCash.add(masuk).subtract(keluar);
                buku.setSaldoCash(saldoCash);
            } else if ("Main BCA".equalsIgnoreCase(jenis)) {
                saldoMainBCA = saldoMainBCA.add(masuk).subtract(keluar);
                buku.setSaldoMainBCA(saldoMainBCA);
            } else if ("BCA Dir".equalsIgnoreCase(jenis)) {
                saldoBCADir = saldoBCADir.add(masuk).subtract(keluar);
                buku.setSaldoBCADir(saldoBCADir);
            } else if ("PCU".equalsIgnoreCase(jenis)) {
                saldoPCU = saldoPCU.add(masuk).subtract(keluar);
                buku.setSaldoPCU(saldoPCU);
            }
            bukuUtamaRepository.save(buku);
        }
    }

    public BigDecimal getSaldoTerakhirSampaiTanggal(LocalDateTime tanggal, String jenisRekening) {
        return bukuUtamaRepository.findTop1ByJenisRekeningIgnoreCaseAndTanggalLessThanOrderByTanggalDesc(jenisRekening, tanggal)
                .stream()
                .findFirst()
                .map(buku -> {
                    switch (jenisRekening) {
                        case "Cash": return buku.getSaldoCash();
                        case "Main BCA": return buku.getSaldoMainBCA();
                        case "BCA Dir": return buku.getSaldoBCADir();
                        case "PCU": return buku.getSaldoPCU();
                        default:return BigDecimal.ZERO;
                    }
                })
                .orElse(BigDecimal.ZERO);
    }

    public List<LaporanGroupDto> getLaporanGroupByAkun(){
        return bukuUtamaRepository.findTotalPerKodeAkun().stream().map(obj -> {
                    String kode = (String) obj[0];
                    String nama = (String) obj[1];
                    BigDecimal totalMasuk = (BigDecimal) obj[2];
                    BigDecimal totalKeluar = (BigDecimal) obj[3];

                    return new LaporanGroupDto(kode, nama, totalMasuk, totalKeluar);
                })
                .toList();

//                    LaporanGroupDto dto = new LaporanGroupDto(kode, nama, totalMasuk, totalKeluar);
//                    return dto;
//        }).toList();
    }
    public List<LaporanGroupDto> getLaporanGroupByKegiatan() {
        return bukuUtamaRepository.findTotalPerKodeKegiatan().stream().map(obj -> {
            String kode = (String) obj[0];
            String nama = (String) obj[1];
            BigDecimal totalMasuk = (BigDecimal) obj[2];
            BigDecimal totalKeluar = (BigDecimal) obj[3];

            return new LaporanGroupDto(kode, nama, totalMasuk, totalKeluar);
        }).toList();
    }

    public List<HistoriSaldoDto>getHistoriSaldoCash(LocalDate start, LocalDate end){
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return bukuUtamaRepository.findByJenisRekeningAndTanggalBetween("Cash", startDate, endDate)
                .stream()
                .map(buku -> new HistoriSaldoDto(buku.getTanggal().toLocalDate(),
                        buku.getSaldoCash(), null, null, null)).collect(Collectors.toList());

    }

    public List<HistoriSaldoDto>getHistoriSaldoMainBCA(LocalDate start, LocalDate end){
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return bukuUtamaRepository.findByJenisRekeningAndTanggalBetween("Main BCA", startDate, endDate)
                .stream().map(buku -> new HistoriSaldoDto(buku.getTanggal().toLocalDate(),
                        null,
                        buku.getSaldoMainBCA(),
                        null,
                        null))
                .collect(Collectors.toList());
    }

    public List<HistoriSaldoDto>getHistoriSaldoBCADir(LocalDate start, LocalDate end){
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return bukuUtamaRepository.findByJenisRekeningAndTanggalBetween("BCA Dir", startDate, endDate)
                .stream().map(buku -> new HistoriSaldoDto(buku.getTanggal().toLocalDate(),
                        null,
                        null,
                        buku.getSaldoBCADir(),
                        null))
                .collect(Collectors.toList());
    }

    public List<HistoriSaldoDto>getHistoriSaldoPCU(LocalDate start, LocalDate end){
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return bukuUtamaRepository.findByJenisRekeningAndTanggalBetween("PCU", startDate, endDate)
                .stream().map(buku -> new HistoriSaldoDto(buku.getTanggal().toLocalDate(),
                        null,
                        null,
                        null,
                        buku.getSaldoPCU()))
                .collect(Collectors.toList());
    }
    private Function<BukuUtama, BigDecimal> getSaldoFunction(String jenisRekening){
        return switch (jenisRekening){
            case "Cash" -> BukuUtama::getSaldoCash;
            case "Main BCA" -> BukuUtama::getSaldoMainBCA;
            case "BCA Dir" -> BukuUtama::getSaldoBCADir;
            case "PCU" -> BukuUtama::getSaldoPCU;
            default -> b -> BigDecimal.ZERO;
        };
    }

    private BigDecimal hitungSaldoCash(BigDecimal masuk, BigDecimal keluar) {
        return masuk.subtract(keluar);
    }

    private BigDecimal hitungSaldoMainBCA(BigDecimal masuk, BigDecimal keluar) {
        return masuk.subtract(keluar);
    }

    private BigDecimal hitungSaldoBCADir(BigDecimal masuk, BigDecimal keluar) {
        return masuk.subtract(keluar);
    }

    private BigDecimal hitungSaldoPCU(BigDecimal masuk, BigDecimal keluar) {
        return masuk.subtract(keluar);
    }
}