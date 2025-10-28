package brajaka.demo.repository;

import brajaka.demo.model.Akun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AkunRepository extends JpaRepository<Akun, String> {
    Optional<Akun>findByKodeAkun(String kodeAkun);
}
