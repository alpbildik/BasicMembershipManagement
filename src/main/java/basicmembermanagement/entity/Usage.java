package basicmembermanagement.entity;

import basicmembermanagement.enums.ExpenseType;
import basicmembermanagement.enums.UsageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "USAGE")
@Getter
@Setter
public class Usage implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @JoinColumn
    @ManyToOne
    private Member member;

    @Column(name = "RECORD")
    private BigDecimal record;

    @Column(name = "USAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private UsageType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

}
