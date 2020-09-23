package basicmembermanagement.enums;

import lombok.Getter;

public enum ExpenseType {
    ANNUAL("Aidat"),
    ELECTRICITY("Elektrik"),
    WATER("Su"),
    PAYMENT("Ödeme"),
    PENALTY("Ceza"),
    OTHER("Diğer");

    @Getter
    public String tr;

    ExpenseType(String tr) {
        this.tr=tr;
    }

    public static ExpenseType findFromUsageType(UsageType usageType) {
        if (UsageType.WATER.equals(usageType)) return WATER;
        if (UsageType.ELECTRICITY.equals(usageType)) return ELECTRICITY;
        if (UsageType.ANNUAL.equals(usageType)) return ANNUAL;
        if (UsageType.PENALTY.equals(usageType)) return PENALTY;
        return OTHER;
    }
}
