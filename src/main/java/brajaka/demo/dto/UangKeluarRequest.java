package brajaka.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UangKeluarRequest {
    @NotNull(message = "Tanggal wajib diisi")
    private LocalDate tanggal;
    @NotBlank(message = "Rekening Tujuan Wajib diisi")
    private String rekeningTujuan;
    @DecimalMin(value = "0.01", message = "Nominal harus lebih besar dari nol")
    @NotNull
    private BigDecimal nominal;
    @Size(max = 500)
    private String deskripsi;
    @NotBlank
    private String kodeAkun;
    @NotBlank
    private String kodeKegiatan;

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getRekeningTujuan() {
        return rekeningTujuan;
    }

    public void setRekeningTujuan(String rekeningTujuan) {
        this.rekeningTujuan = rekeningTujuan;
    }

    public BigDecimal getNominal() {
        return nominal;
    }

    public void setNominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKodeAkun() {
        return kodeAkun;
    }

    public void setKodeAkun(String kodeAkun) {
        this.kodeAkun = kodeAkun;
    }

    public String getKodeKegiatan() {
        return kodeKegiatan;
    }

    public void setKodeKegiatan(String kodeKegiatan) {
        this.kodeKegiatan = kodeKegiatan;
    }
}
