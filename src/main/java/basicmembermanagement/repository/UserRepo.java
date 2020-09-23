package basicmembermanagement.repository;

import basicmembermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
