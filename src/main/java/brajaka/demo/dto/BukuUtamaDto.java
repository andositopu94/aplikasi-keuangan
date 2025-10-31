package brajaka.demo.dto;

import brajaka.demo.validation.NotFuture;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BukuUtamaDto {
    private String traceNumber;
    @NotNull(message = "Tanggal tidak boleh null")
    @NotFuture
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime tanggal;
    @NotBlank(message = "Kode transaksi tidak boleh kosong")
    @Size(max = 10, message = "Kode transaksi maksimal 20 karakter")
    private String kodeTransaksi;
    @PositiveOrZero(message = "Nominal masuk harus bernilai positif atau nol")
    private BigDecimal nominalMasuk;
    @PositiveOrZero(message = "Nominal keluar harus bernilai positif atau nol")
    private BigDecimal nominalKeluar;
    private String sumberRekening;
    private String rekeningTujuan;
    private String jenisRekening;
    private String deskripsi;
    @NotBlank(message = "Kode akun tidak boleh kosong")
    private String kodeAkun;
    private String namaAkun;
    @NotBlank(message = "Kode kegiatan tidak boleh kosong")
    private String kodeKegiatan;
    private String namaKegiatan;
    private BigDecimal saldoMainBCA;
    private BigDecimal saldoPCU;
    private BigDecimal saldoBCADir;
    private BigDecimal saldoCash;
//    private Integer version;


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

    public String getSumberRekening() {
        return sumberRekening;
    }

    public void setSumberRekening(String sumberRekening) {
        this.sumberRekening = sumberRekening;
    }

    public String getRekeningTujuan() {
        return rekeningTujuan;
    }

    public void setRekeningTujuan(String rekeningTujuan) {
        this.rekeningTujuan = rekeningTujuan;
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

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
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

    public BigDecimal getSaldoMainBCA() {
        return saldoMainBCA;
    }

    public void setSaldoMainBCA(BigDecimal saldoMainBCA) {
        this.saldoMainBCA = saldoMainBCA;
    }

    public BigDecimal getSaldoPCU() {
        return saldoPCU;
    }

    public void setSaldoPCU(BigDecimal saldoPCU) {
        this.saldoPCU = saldoPCU;
    }

    public BigDecimal getSaldoBCADir() {
        return saldoBCADir;
    }

    public void setSaldoBCADir(BigDecimal saldoBCADir) {
        this.saldoBCADir = saldoBCADir;
    }

    public BigDecimal getSaldoCash() {
        return saldoCash;
    }

    public void setSaldoCash(BigDecimal saldoCash) {
        this.saldoCash = saldoCash;
    }

    public String getJenisRekening() {
        return jenisRekening;
    }

    public void setJenisRekening(String jenisRekening) {
        this.jenisRekening = jenisRekening;
    }

}
