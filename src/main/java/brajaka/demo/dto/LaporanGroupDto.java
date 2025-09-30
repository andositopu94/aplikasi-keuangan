package brajaka.demo.dto;

import java.math.BigDecimal;

public class LaporanGroupDto {
    //    private String groupKey;
    private String kode;
    private String nama;
    private BigDecimal totalMasuk;
    private BigDecimal totalKeluar;
    private BigDecimal saldo;

    public LaporanGroupDto(String kode,String nama, BigDecimal totalMasuk, BigDecimal totalKeluar) {
        this.kode = kode;
        this.nama = nama;
        this.totalMasuk = totalMasuk != null ? totalMasuk : BigDecimal.ZERO;
        this.totalKeluar = totalKeluar != null ? totalKeluar : BigDecimal.ZERO;
        this.saldo = this.totalMasuk.subtract(this.totalKeluar);
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public BigDecimal getTotalMasuk() {
        return totalMasuk;
    }

    public void setTotalMasuk(BigDecimal totalMasuk) {
        this.totalMasuk = totalMasuk;
    }

    public BigDecimal getTotalKeluar() {
        return totalKeluar;
    }

    public void setTotalKeluar(BigDecimal totalKeluar) {
        this.totalKeluar = totalKeluar;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}

