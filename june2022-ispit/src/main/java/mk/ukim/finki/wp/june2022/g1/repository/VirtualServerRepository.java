package mk.ukim.finki.wp.june2022.g1.repository;

import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VirtualServerRepository extends JpaRepository<VirtualServer,Long> {
    List<VirtualServer> findAllByLaunchDateBeforeAndAndOwnersContaining(LocalDate launch, User user);
    List<VirtualServer> findAllByOwnersContaining(User user);
    List<VirtualServer> findAllByLaunchDateBefore(LocalDate launch);
}
