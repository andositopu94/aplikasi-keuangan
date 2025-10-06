package brajaka.demo.dto;

import brajaka.demo.model.LaporanLapangan;

public class LaporanLapanganMapper {
    public static LaporanLapanganResponse toResponse(LaporanLapangan entity){
        LaporanLapanganResponse dto = new LaporanLapanganResponse();
        dto.setId(entity.getId());
        dto.setTanggal(entity.getTanggal());
        dto.setKodeLapangan(entity.getKodeLapangan());
        dto.setDebit(entity.getDebit());
        dto.setKredit(entity.getKredit());
        dto.setKeterangan(entity.getKeterangan());
        dto.setDeskripsi(entity.getDeskripsi());
        dto.setBuktiPath(entity.getBuktiPath());

        if (entity.getAkun() != null){
            dto.setKodeAkun(entity.getAkun().getKodeAkun());
        }
        if (entity.getKegiatan() != null){
            dto.setKodeKegiatan(entity.getKegiatan().getKodeKegiatan());
        }
        dto.setNamaUser(entity.getNamaUser());
        return dto;
    }
}
