package basicmembermanagement.repository;

import basicmembermanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberRepo extends JpaRepository<Member, Long> {

}
