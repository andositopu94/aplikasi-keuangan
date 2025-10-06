package brajaka.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LaporanLapanganResponse {
    private Long id;
    private LocalDateTime tanggal;
    private String kodeLapangan;
    private String deskripsi;
    private BigDecimal debit;
    private BigDecimal kredit;
    private String keterangan;
    private String buktiPath;
    private String kodeAkun;
    private String kodeKegiatan;
    private String namaUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getKodeLapangan() {
        return kodeLapangan;
    }

    public void setKodeLapangan(String kodeLapangan) {
        this.kodeLapangan = kodeLapangan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getKredit() {
        return kredit;
    }

    public void setKredit(BigDecimal kredit) {
        this.kredit = kredit;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBuktiPath() {
        return buktiPath;
    }

    public void setBuktiPath(String buktiPath) {
        this.buktiPath = buktiPath;
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

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }
}
