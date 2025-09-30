package brajaka.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoriSaldoDto {
    private LocalDate tanggal;
    private BigDecimal saldoCash;
    private BigDecimal saldoMainBCA;
    private BigDecimal saldoBCADir;
    private BigDecimal saldoPCU;

    public HistoriSaldoDto(){}
    public HistoriSaldoDto(LocalDate tanggal, BigDecimal saldoCash, BigDecimal saldoMainBCA, BigDecimal saldoBCADir, BigDecimal saldoPCU) {
        this.tanggal = tanggal;
        this.saldoCash = saldoCash;
        this.saldoMainBCA = saldoMainBCA;
        this.saldoBCADir = saldoBCADir;
        this.saldoPCU = saldoPCU;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public BigDecimal getSaldoCash() {
        return saldoCash;
    }

    public void setSaldoCash(BigDecimal saldoCash) {
        this.saldoCash = saldoCash;
    }

    public BigDecimal getSaldoMainBCA() {
        return saldoMainBCA;
    }

    public void setSaldoMainBCA(BigDecimal saldoMainBCA) {
        this.saldoMainBCA = saldoMainBCA;
    }

    public BigDecimal getSaldoBCADir() {
        return saldoBCADir;
    }

    public void setSaldoBCADir(BigDecimal saldoBCADir) {
        this.saldoBCADir = saldoBCADir;
    }

    public BigDecimal getSaldoPCU() {
        return saldoPCU;
    }

    public void setSaldoPCU(BigDecimal saldoPCU) {
        this.saldoPCU = saldoPCU;
    }
}
