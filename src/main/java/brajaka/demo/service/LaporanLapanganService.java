package brajaka.demo.service;

import brajaka.demo.dto.LaporanLapanganRequest;
import brajaka.demo.model.Akun;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.model.LaporanLapangan;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.LaporanLapanganRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LaporanLapanganService {

    private final LaporanLapanganRepository repository;
    private final AkunRepository akunRepository;
    private final KegiatanRepository kegiatanRepository;
    private final FileStrorageService fileStrorageService;

    public LaporanLapanganService(LaporanLapanganRepository repository,
                                  AkunRepository akunRepository,
                                  KegiatanRepository kegiatanRepository,
                                  FileStrorageService fileStrorageService){
        this.repository = repository;
        this.akunRepository = akunRepository;
        this.kegiatanRepository = kegiatanRepository;
        this.fileStrorageService = fileStrorageService;
    }
    public LaporanLapangan createLaporan (LaporanLapanganRequest request, MultipartFile bukti) {
        String buktiPath = fileStrorageService.storeFile(bukti);
        Akun akun = akunRepository.findById(request.getKodeAkun())
                .orElseThrow(() -> new RuntimeException("Akun tidak ditemukan"));
        Kegiatan kegiatan = kegiatanRepository.findById(request.getKodeKegiatan())
                .orElseThrow(() -> new RuntimeException("Kegiatan tidak ditemukan"));

        LaporanLapangan laporan = new LaporanLapangan();
        laporan.setTanggal(request.getTanggal());
        laporan.setKodeLapangan(request.getKodeLapangan());
        laporan.setDeskripsi(request.getDeskripsi());
        laporan.setDebit(request.getDebit());
        laporan.setKredit(request.getKredit());
        laporan.setKeterangan(request.getKeterangan());
        laporan.setBuktiPath(buktiPath);
        laporan.setAkun(akun);
        laporan.setKegiatan(kegiatan);
        laporan.setNamaUser(request.getNamaUser());

        return repository.save(laporan);

    }

    public LaporanLapangan updateLaporan(Long id, LaporanLapanganRequest request, MultipartFile bukti) {
        LaporanLapangan laporan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laporan Tidak Ditemukan"));

        if (request.getTanggal() != null) laporan.setTanggal(request.getTanggal());
        laporan.setKodeLapangan(request.getKodeLapangan());
        laporan.setDeskripsi(request.getDeskripsi());
        laporan.setDebit(request.getDebit());
        laporan.setKredit(request.getKredit());
        laporan.setKeterangan(request.getKeterangan());
        laporan.setNamaUser(request.getNamaUser());

        if (request.getKodeAkun() != null){
            Akun akun = akunRepository.findById(request.getKodeAkun())
                    .orElseThrow(() -> new RuntimeException("Akun Tidak Ditemukan"));
            laporan.setAkun(akun);
        }
        if (request.getKodeKegiatan() != null){
            Kegiatan kegiatan = kegiatanRepository.findById(request.getKodeKegiatan())
                    .orElseThrow(() -> new RuntimeException("Kegiatan Tidak Ditemukan"));
            laporan.setKegiatan(kegiatan);
        }
        if (bukti != null && !bukti.isEmpty()) {
            String buktiPath = fileStrorageService.storeFile(bukti);
            laporan.setBuktiPath(buktiPath);
        }
        return repository.save(laporan);
    }
}
