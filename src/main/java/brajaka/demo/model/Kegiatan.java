package brajaka.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Kegiatan {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private String kodeKegiatan;
    private String namaKegiatan;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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
}
