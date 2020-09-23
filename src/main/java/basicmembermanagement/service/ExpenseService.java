package basicmembermanagement.service;

import basicmembermanagement.dto.UsageDTO;
import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import basicmembermanagement.enums.ExpenseType;
import basicmembermanagement.repository.ExpenseRepo;
import basicmembermanagement.util.ExpenseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepository;

    @Autowired
    private MemberService memberService;

    public boolean save(Expense expense) {
        try {
            expenseRepository.save(expense);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(Expense expense) {
        try {
            expenseRepository.delete(expense);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public List<Expense> findByMember(Member member){
        return expenseRepository.findByMember(member).stream()
                .sorted(Comparator.comparing(Expense::getCreateDate))
                .collect(Collectors.toList());

    }

    public List<Expense> findByMemberId(String memberId) {
        Optional<Member> member = memberService.findById(Long.valueOf(memberId));
        return findByMember(member.get());
    }


    public void createCommonExpense(BigDecimal amount, ExpenseType expenseType, String description) {
        List<Expense> expenses = memberService.findAll().stream()
                .map(member -> ExpenseUtil.calculateFromExpense(member, amount, expenseType, description))
                .collect(Collectors.toList());

        expenseRepository.saveAll(expenses);
    }


    public void createExpense(List<UsageDTO> usageDTOS, BigDecimal unitAmount, ExpenseType expenseType, String description) {
        List<Expense> expenses = usageDTOS.stream()
                .filter(usages -> Objects.nonNull(usages.getNewUsageRecord()))
                .map(usageDTO -> ExpenseUtil.calculateFromExpense(usageDTO.getMember(), usageDTO.getDifference().multiply(unitAmount), expenseType, description))
                .collect(Collectors.toList());

        expenseRepository.saveAll(expenses);
    }

}
