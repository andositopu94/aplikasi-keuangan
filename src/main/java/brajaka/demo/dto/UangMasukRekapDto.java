package brajaka.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UangMasukRekapDto {
    private LocalDate tanggal;
    private String jenisRekening;
    private BigDecimal totalUangMasuk;

    public UangMasukRekapDto(LocalDate tanggal, String jenisRekening, BigDecimal totalUangMasuk) {
        this.tanggal = tanggal;
        this.jenisRekening = jenisRekening;
        this.totalUangMasuk = totalUangMasuk;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    public String getJenisRekening() {
        return jenisRekening;
    }
    public void setJenisRekening(String jenisRekening) {
        this.jenisRekening = jenisRekening;
    }
    public BigDecimal getTotalUangMasuk() {
        return totalUangMasuk;
    }
    public void setTotalUangMasuk(BigDecimal totalUangMasuk) {
        this.totalUangMasuk = totalUangMasuk;
    }

}
