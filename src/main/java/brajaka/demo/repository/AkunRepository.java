package brajaka.demo.repository;

import brajaka.demo.model.Akun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AkunRepository extends JpaRepository<Akun, String> {

}
