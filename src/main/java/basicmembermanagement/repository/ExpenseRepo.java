package basicmembermanagement.repository;

import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ExpenseRepo extends JpaRepository<Expense, Long> {

    List<Expense> findByMember(Member member);
}
