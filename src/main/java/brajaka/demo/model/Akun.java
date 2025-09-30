package brajaka.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Akun {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private String kodeAkun;
    private String namaAkun;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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
