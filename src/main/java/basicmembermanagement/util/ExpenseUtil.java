package basicmembermanagement.util;

import basicmembermanagement.dto.ExpenseSummation;
import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import basicmembermanagement.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExpenseUtil {

    public static Expense calculateFromExpense(Member member, BigDecimal amount, ExpenseType expenseType, String decription) {
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setMember(member);
        expense.setCreateDate(new Date());
        expense.setType(expenseType);
        expense.setDescription(decription + "\n" + createExpenseDescripion(expenseType, amount));
        return expense;
    }

    public static Expense createPayment(Member member, BigDecimal amount, String description) {
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setMember(member);
        expense.setCreateDate(new Date());
        expense.setType(ExpenseType.PAYMENT);
        expense.setDescription(description);
        return expense;
    }

    private static String createExpenseDescripion(ExpenseType expenseType, BigDecimal amount) {
        return Util.createDescription(new Date(),
                expenseType.tr,
                amount);
    }

    public static ExpenseSummation calculateTotal(List<Expense> expenses) {
        BigDecimal payment = expenses.stream()
                .filter(ExpenseUtil::isPayment)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = expenses.stream()
                .filter(ExpenseUtil::isExpense)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = payment.subtract(expense);

        return new ExpenseSummation(payment, expense, total);
    }

    public static boolean isPayment(Expense expense) {
        return ExpenseType.PAYMENT.equals(expense.getType());
    }

    public static boolean isExpense(Expense expense) {
        return !isPayment(expense);
    }

    public static BigDecimal getAmount(Expense expense){
        if (isExpense(expense)) {
            return expense.getAmount().negate();
        }
        return expense.getAmount();
    }

    public static String getTotal(List<Expense> expenseList) {
        return expenseList.stream().map(ExpenseUtil::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString();
    }
}
