package basicmembermanagement.repository;

import basicmembermanagement.entity.Member;
import basicmembermanagement.entity.Usage;
import basicmembermanagement.enums.UsageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UsageRepo extends JpaRepository<Usage, Long> {

    List<Usage> findByMemberAndType(Member member, UsageType usageType);
}
