package studio.aroundhub.opensourceproject.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.aroundhub.opensourceproject.domain.user.UserDomain;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, Long> {
    UserDomain findByEmail(String email);

}
