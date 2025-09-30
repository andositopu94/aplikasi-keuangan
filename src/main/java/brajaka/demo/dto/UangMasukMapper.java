package brajaka.demo.dto;

import brajaka.demo.model.Akun;
import brajaka.demo.model.Kegiatan;
import brajaka.demo.model.UangMasuk;

public class UangMasukMapper {

    public static UangMasukDto toDto(UangMasuk uangMasuk){
        UangMasukDto dto = new UangMasukDto();
        dto.setTraceNumber(uangMasuk.getTraceNumber());
        dto.setTanggal(uangMasuk.getTanggal().toLocalDate());
        dto.setKodeTransaksi(uangMasuk.getKodeTransaksi());
        dto.setSumberRekening(uangMasuk.getSumberRekening());
        dto.setNominal(uangMasuk.getNominal());
        dto.setDeskripsi(uangMasuk.getDeskripsi());

        if (uangMasuk.getAkun() != null) {
            Akun akun = uangMasuk.getAkun();
            dto.setKodeAkun(akun.getKodeAkun());
            dto.setNamaAkun(akun.getNamaAkun());
        }
        if (uangMasuk.getKegiatan() != null) {
            Kegiatan kegiatan = uangMasuk.getKegiatan();
            dto.setKodeKegiatan(kegiatan.getKodeKegiatan());
            dto.setNamaKegiatan(kegiatan.getNamaKegiatan());
        }
        return dto;
    }
}
