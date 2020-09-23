package basicmembermanagement.entity;

import basicmembermanagement.enums.ExpenseType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EXPENSE")
@Getter
@Setter
public class Expense implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @JoinColumn
    @ManyToOne
    private Member member;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "EXPENSE_TYPE")
    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

}
