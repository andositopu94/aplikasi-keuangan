package brajaka.demo.service;

import brajaka.demo.repository.BukuUtamaRepository;
import brajaka.demo.repository.KegiatanRepository;
import brajaka.demo.repository.UangKeluarRepository;
import brajaka.demo.repository.UangMasukRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KegiatanService {
    private final KegiatanRepository kegiatanRepository;
    private final BukuUtamaRepository bukuUtamaRepository;
    private final UangMasukRepository uangMasukRepository;
    private final UangKeluarRepository uangKeluarRepository;

    public KegiatanService(KegiatanRepository kegiatanRepository,
                           BukuUtamaRepository bukuUtamaRepository,
                           UangMasukRepository uangMasukRepository,
                           UangKeluarRepository uangKeluarRepository) {
        this.kegiatanRepository = kegiatanRepository;
        this.bukuUtamaRepository = bukuUtamaRepository;
        this.uangMasukRepository = uangMasukRepository;
        this.uangKeluarRepository = uangKeluarRepository;
    }
    @Transactional
    public void deleteKegiatanIfUnused(String kode) {
        if (!kegiatanRepository.existsById(kode)) {
            throw new IllegalArgumentException("Kegiatan tidak ditemukan: " + kode);
        }

        boolean usedInBukuUtama = bukuUtamaRepository.findByKegiatan_KodeKegiatan(kode).size() > 0;
        boolean usedInUangMasuk = uangMasukRepository.findAll().stream()
                .anyMatch(um -> um.getKegiatan() != null && kode.equals(um.getKegiatan().getKodeKegiatan()));
        boolean usedInUangKeluar = uangKeluarRepository.findAll().stream()
                .anyMatch(uk -> uk.getKegiatan() != null && kode.equals(uk.getKegiatan().getKodeKegiatan()));

        // Jika kamu memiliki repository / query existsBy.. lebih efisien, ubah pemanggilan di atas ke existsBy...
        if (usedInBukuUtama || usedInUangMasuk || usedInUangKeluar) {
            String reason = "Kegiatan masih direferensi di: " +
                    (usedInBukuUtama ? "buku_utama " : "") +
                    (usedInUangMasuk ? "uang_masuk " : "") +
                    (usedInUangKeluar ? "uang_keluar " : "");
            throw new DataIntegrityViolationException(reason.trim());
        }

        kegiatanRepository.deleteById(kode);
    }
}
