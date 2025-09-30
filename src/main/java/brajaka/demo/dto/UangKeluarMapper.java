package brajaka.demo.dto;

import brajaka.demo.model.UangKeluar;

public class UangKeluarMapper {
    public static UangKeluarDto toDTO(UangKeluar uangKeluar){
        UangKeluarDto dto = new UangKeluarDto();
        dto.setTraceNumber(uangKeluar.getTraceNumber());
        dto.setTanggal(uangKeluar.getTanggal().toLocalDate());
        dto.setKodeTransaksi(uangKeluar.getKodeTransaksi());
        dto.setRekeningTujuan(uangKeluar.getRekeningTujuan());
        dto.setNominal(uangKeluar.getNominal());
        dto.setDeskripsi(uangKeluar.getDeskripsi());

        if(uangKeluar.getAkun() != null){
            dto.setKodeAkun(uangKeluar.getAkun().getKodeAkun());
            dto.setNamaAkun(uangKeluar.getAkun().getNamaAkun());
        }

        if(uangKeluar.getKegiatan() != null){
            dto.setKodeKegiatan(uangKeluar.getKegiatan().getKodeKegiatan());
            dto.setNamaKegiatan(uangKeluar.getKegiatan().getNamaKegiatan());
        }
        return dto;

    }
}
