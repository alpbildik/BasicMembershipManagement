package basicmembermanagement.util;

import basicmembermanagement.dto.UsageDTO;
import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Usage;
import basicmembermanagement.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.Date;

public class UsageUtil {

    public static Usage convert(UsageDTO usageDTO, Double record) {
        Usage usage = new Usage();
        usage.setMember(usageDTO.getMember());
        usage.setRecord(BigDecimal.valueOf(record));
        usage.setCreateDate(new Date());
        usage.setType(usageDTO.getUsageType());
        return usage;
    }

    public static Expense calculateFromExpense(UsageDTO usageDTO, Double unitPrice) {
        BigDecimal amount = BigDecimal.valueOf(usageDTO.getNewUsageRecord())
                .subtract(BigDecimal.valueOf(usageDTO.getLastUsageRecord()))
                .multiply(BigDecimal.valueOf(unitPrice));

        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setMember(usageDTO.getMember());
        expense.setCreateDate(new Date());
        expense.setType(ExpenseType.findFromUsageType(usageDTO.getUsageType()));
        expense.setDescription(createExpenseDescripion(usageDTO, unitPrice));
        return expense;
    }


    private static String createExpenseDescripion(UsageDTO usageDTO, Double unitPrice) {
        return Util.createDescription(new Date(),
                usageDTO.getUsageType().tr,
                usageDTO.getLastUsageRecord(),
                usageDTO.getNewUsageRecord(),
                unitPrice);
    }
}
