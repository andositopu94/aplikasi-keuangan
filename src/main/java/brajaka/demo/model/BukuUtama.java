package brajaka.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "trace_number"))
public class BukuUtama extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String traceNumber;
    private LocalDateTime tanggal;
    private String kodeTransaksi;
    private BigDecimal nominalMasuk;
    private BigDecimal nominalKeluar;
    private String sumberRekening;
    private String rekeningTujuan;
    private String deskripsi;
    private BigDecimal saldoMainBCA;
    private BigDecimal saldoPCU;
    private BigDecimal saldoBCADir;
    private BigDecimal saldoCash;
    private String jenisRekening;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kode_akun", referencedColumnName = "kodeAkun")
    private Akun akun;
    @ManyToOne(optional = false)
    @JoinColumn(name = "kode_kegiatan", referencedColumnName = "kodeKegiatan")
    private Kegiatan kegiatan;

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

    public Akun getAkun() {
        return akun;
    }

    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getJenisRekening() {
        return jenisRekening;
    }

    public void setJenisRekening(String jenisRekening) {
        this.jenisRekening = jenisRekening;
    }

    public BukuUtama(){
    }
}
