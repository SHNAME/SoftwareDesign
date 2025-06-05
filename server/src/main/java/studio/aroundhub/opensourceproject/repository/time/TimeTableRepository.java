package studio.aroundhub.opensourceproject.repository.time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.aroundhub.opensourceproject.domain.time.TimeDomain;

import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeDomain, Long> {
    List<TimeDomain> findByEmail(String email);
    TimeDomain findByEmailAndId(String email, long id);
}
