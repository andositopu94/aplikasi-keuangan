    package brajaka.demo.dto;

    import java.math.BigDecimal;
    import java.time.LocalDate;

    public class UangKeluarRekapDto {
        private LocalDate tanggal;
        private String jenisRekening;
        private BigDecimal totalUangKeluar;

        public UangKeluarRekapDto(LocalDate tanggal, String jenisRekening, BigDecimal totalUangKeluar) {
            this.tanggal = tanggal;
            this.jenisRekening = jenisRekening;
            this.totalUangKeluar = totalUangKeluar;
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
        public BigDecimal getTotalUangKeluar() {
            return totalUangKeluar;
        }
        public void setTotalUangKeluar(BigDecimal totalUangKeluar) {
            this.totalUangKeluar = totalUangKeluar;
        }

    }
