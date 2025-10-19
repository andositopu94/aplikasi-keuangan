package brajaka.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AkunDto {
    @NotBlank(message = "Kode Akun wajib diisi")
    private String kodeAkun;
    @NotBlank(message = "Nama Akun wajib diisi")
    private String namaAkun;

    public AkunDto() {}

    public AkunDto(String kodeAkun, String namaAkun) {
        this.kodeAkun = kodeAkun;
        this.namaAkun = namaAkun;
    }

    public String getKodeAkun() {
        return kodeAkun;
    }

    public void setKodeAkun(String kodeAkun) {
        this.kodeAkun = kodeAkun;
    }

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }
}
