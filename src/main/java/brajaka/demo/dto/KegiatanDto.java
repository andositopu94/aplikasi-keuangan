package brajaka.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class KegiatanDto {
    @NotBlank(message = "Kode Kegiatan wajib diisi")
    private String kodeKegiatan;
    @NotBlank(message = "Nama Kegiatan wajib diisi")
    private String namaKegiatan;

    public KegiatanDto() {}

    public KegiatanDto(String kodeKegiatan, String namaKegiatan) {
        this.kodeKegiatan = kodeKegiatan;
        this.namaKegiatan = namaKegiatan;
    }

    public String getKodeKegiatan() {
        return kodeKegiatan;
    }

    public void setKodeKegiatan(String kodeKegiatan) {
        this.kodeKegiatan = kodeKegiatan;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }
}
