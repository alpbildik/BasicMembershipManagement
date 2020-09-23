package basicmembermanagement.dto;

import basicmembermanagement.entity.Member;
import basicmembermanagement.entity.Usage;
import basicmembermanagement.enums.UsageType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class UsageDTO implements Serializable {

    public UsageDTO(Member member, Usage lastUsage, UsageType usageType) {
        this.memberId = member.getId();
        this.member = member;
        this.memberName = member.getName() + " " + member.getSurname();
        this.usageType = usageType;

        if (Objects.nonNull(lastUsage)) {
            this.lastUsageRecord = lastUsage.getRecord().doubleValue();
        }
    }

    private Long memberId;
    private transient Member member;
    private String memberName;
    private UsageType usageType;
    private Double lastUsageRecord;
    private Double newUsageRecord;

}


