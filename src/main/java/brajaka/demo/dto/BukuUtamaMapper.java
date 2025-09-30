package brajaka.demo.dto;

import brajaka.demo.model.Akun;
import brajaka.demo.model.BukuUtama;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.repository.AkunRepository;
import brajaka.demo.repository.KegiatanRepository;

import java.math.BigDecimal;

public class BukuUtamaMapper {


    public static BukuUtamaDto toDTO(BukuUtama bukuUtama){
        BukuUtamaDto dto = new BukuUtamaDto();
        dto.setTraceNumber(bukuUtama.getTraceNumber());
        dto.setTanggal(bukuUtama.getTanggal());
        dto.setKodeTransaksi(bukuUtama.getKodeTransaksi());
        dto.setJenisRekening(bukuUtama.getJenisRekening());

        dto.setNominalMasuk(bukuUtama.getNominalMasuk() != null ? bukuUtama.getNominalMasuk(): BigDecimal.ZERO);
        dto.setNominalKeluar(bukuUtama.getNominalKeluar() != null ? bukuUtama.getNominalKeluar(): BigDecimal.ZERO);
        dto.setSumberRekening(bukuUtama.getSumberRekening());
        dto.setRekeningTujuan(bukuUtama.getRekeningTujuan());
        dto.setDeskripsi(bukuUtama.getDeskripsi());

        dto.setSaldoMainBCA(bukuUtama.getSaldoMainBCA() != null ? bukuUtama.getSaldoMainBCA():BigDecimal.ZERO);
        dto.setSaldoPCU(bukuUtama.getSaldoPCU() != null ? bukuUtama.getSaldoPCU():BigDecimal.ZERO);
        dto.setSaldoBCADir(bukuUtama.getSaldoBCADir() != null ? bukuUtama.getSaldoBCADir():BigDecimal.ZERO);
        dto.setSaldoCash(bukuUtama.getSaldoCash() != null ? bukuUtama.getSaldoCash():BigDecimal.ZERO);


        if (bukuUtama.getAkun() != null){
            dto.setKodeAkun(bukuUtama.getAkun().getKodeAkun());
            dto.setNamaAkun(bukuUtama.getAkun().getNamaAkun());
        }else {
            dto.setKodeAkun("N/A");
            dto.setNamaAkun("N/A");
        }

        if (bukuUtama.getKegiatan() != null){
            dto.setKodeKegiatan(bukuUtama.getKegiatan().getKodeKegiatan());
            dto.setNamaKegiatan(bukuUtama.getKegiatan().getNamaKegiatan());
        }else {
            dto.setKodeKegiatan("N/A");
            dto.setNamaKegiatan("N/A");
        }

        return dto;
    }

    public static BukuUtama toEntity(BukuUtamaDto dto, AkunRepository akunRepository, KegiatanRepository kegiatanRepository){
        BukuUtama entity = new BukuUtama();

        entity.setTanggal(dto.getTanggal());
        entity.setKodeTransaksi(dto.getKodeTransaksi());
        entity.setJenisRekening(dto.getJenisRekening());
        entity.setNominalMasuk(dto.getNominalMasuk() != null ? dto.getNominalMasuk() : BigDecimal.ZERO);
        entity.setNominalKeluar(dto.getNominalKeluar() != null ? dto.getNominalKeluar(): BigDecimal.ZERO);
        entity.setSumberRekening(dto.getSumberRekening() != null ? dto.getSumberRekening() : "");
        entity.setRekeningTujuan(dto.getRekeningTujuan() != null ? dto.getRekeningTujuan() : "");
        entity.setDeskripsi(dto.getDeskripsi() != null ? dto.getDeskripsi(): "");

        if (dto.getKodeAkun() != null && !dto.getKodeAkun().isBlank()){
            Akun akun = akunRepository.findById(dto.getKodeAkun()).orElseThrow(() -> new IllegalArgumentException("Akun Tidak Ditemukan:" + dto.getKodeAkun()));
            entity.setAkun(akun);
        }
        if (dto.getKodeKegiatan() != null && !dto.getKodeKegiatan().isBlank()){
            Kegiatan kegiatan = kegiatanRepository.findById(dto.getKodeKegiatan())
                    .orElseThrow(() -> new IllegalArgumentException("Kegiatan Tidak Ditemukan: " + dto.getKodeKegiatan()));
            entity.setKegiatan(kegiatan);
        }

        return entity;
    }
    public static LaporanKeuanganDto toLaporanKeuanganDto(BukuUtama bukuUtama) {
        LaporanKeuanganDto dto = new LaporanKeuanganDto();
        dto.setTraceNumber(bukuUtama.getTraceNumber());
        dto.setTanggal(bukuUtama.getTanggal());
        dto.setKodeTransaksi(bukuUtama.getKodeTransaksi());
        dto.setDeskripsi(bukuUtama.getDeskripsi());
        dto.setNominalMasuk(bukuUtama.getNominalMasuk());
        dto.setNominalKeluar(bukuUtama.getNominalKeluar());
        dto.setJenisRekening(bukuUtama.getJenisRekening());
        if (bukuUtama.getAkun() != null) {
            dto.setKodeAkun(bukuUtama.getAkun().getKodeAkun());
            dto.setNamaAkun(bukuUtama.getAkun().getNamaAkun());
        }
        return dto;
    }
}
