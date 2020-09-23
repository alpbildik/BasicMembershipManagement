package basicmembermanagement.service;

import basicmembermanagement.dto.UsageDTO;
import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import basicmembermanagement.entity.Usage;
import basicmembermanagement.enums.UsageType;
import basicmembermanagement.repository.ExpenseRepo;
import basicmembermanagement.repository.UsageRepo;
import basicmembermanagement.util.UsageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UsageService {

    @Autowired
    private UsageRepo usageRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ExpenseRepo expenseRepo;

    public void save(Usage usage) {
        try {
            usageRepository.save(usage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Usage usage) {
        try {
            usageRepository.delete(usage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Usage> findAll() {
        return usageRepository.findAll();
    }

    public List<Usage> findByMemberAndUsageType(Member member, UsageType usageType){
        return usageRepository.findByMemberAndType(member, usageType);
    }

    public Usage findLastUsageByMemberAndUsageType(Member member, UsageType usageType){
        try {
            return findByMemberAndUsageType(member, usageType)
                    .stream()
                    .max(Comparator.comparing(Usage::getRecord))
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<UsageDTO> findLastUsages(UsageType usageType) {
        List<UsageDTO> result = new LinkedList<>();
        List<Member> allMembers = memberService.findAll();

        for (Member member: allMembers) {
            Usage lastUsage = findLastUsageByMemberAndUsageType(member, usageType);
            result.add(new UsageDTO(member, lastUsage, usageType));
        }

        return result;
    }

    public void saveUsages(List<UsageDTO> usageDTOS, Double unitUsagePrice, boolean isFirstUsage) {
        if (isFirstUsage) {
            List<Usage> firstUsages = usageDTOS.stream()
                    .map(usageDTO -> UsageUtil.convert(usageDTO, usageDTO.getLastUsageRecord()))
                    .collect(Collectors.toList());

            usageRepository.saveAll(firstUsages);
        }

        List<Usage> newUsages = usageDTOS.stream()
                .map(usageDTO -> UsageUtil.convert(usageDTO, usageDTO.getNewUsageRecord()))
                .collect(Collectors.toList());

        usageRepository.saveAll(newUsages);


        List<Expense> expenses = usageDTOS.stream()
                .map(usageDTO -> UsageUtil.calculateFromExpense(usageDTO, unitUsagePrice))
                .collect(Collectors.toList());

        expenseRepo.saveAll(expenses);
    }
}
