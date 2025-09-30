package brajaka.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LaporanKeuanganDto {
    private String traceNumber;
    private LocalDateTime tanggal;
    private String kodeTransaksi;
    private String deskripsi;
    private BigDecimal nominalMasuk;
    private BigDecimal nominalKeluar;
    private String jenisRekening;
    private String kodeAkun;
    private String namaAkun;

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getNominalMasuk() {
        return nominalMasuk;
    }

    public void setNominalMasuk(BigDecimal nominalMasuk) {
        this.nominalMasuk = nominalMasuk;
    }

    public BigDecimal getNominalKeluar() {
        return nominalKeluar;
    }

    public void setNominalKeluar(BigDecimal nominalKeluar) {
        this.nominalKeluar = nominalKeluar;
    }

    public String getJenisRekening() {
        return jenisRekening;
    }

    public void setJenisRekening(String jenisRekening) {
        this.jenisRekening = jenisRekening;
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
