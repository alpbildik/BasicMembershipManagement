package basicmembermanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExpenseSummation {

    private BigDecimal paymentTotal;
    private BigDecimal expenseTotal;
    private BigDecimal total;

    public ExpenseSummation(BigDecimal paymentTotal, BigDecimal expenseTotal, BigDecimal total) {
        this.paymentTotal = paymentTotal;
        this.expenseTotal = expenseTotal;
        this.total = total;
    }

    public String getText() {
        return "toplam ödeme: " + paymentTotal +
                ", toplam harcama: " + expenseTotal +
                ", toplam: " + total;
    }
}
