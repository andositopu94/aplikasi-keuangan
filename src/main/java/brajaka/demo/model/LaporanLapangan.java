package brajaka.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class LaporanLapangan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime tanggal;
    private String kodeLapangan;
    private String deskripsi;
    private BigDecimal debit;
    private BigDecimal kredit;
    private String keterangan;
    private String namaUser;

    @ManyToOne
    @JoinColumn(name = "kode_kegiatan", referencedColumnName = "kodeKegiatan")
    private Kegiatan kegiatan;
    @ManyToOne
    @JoinColumn(name = "kode_akun", referencedColumnName = "kodeAkun")
    private Akun akun;

    private String buktiPath;

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

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }

    public Akun getAkun() {
        return akun;
    }

    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public String getBuktiPath() {
        return buktiPath;
    }

    public void setBuktiPath(String buktiPath) {
        this.buktiPath = buktiPath;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }
}
