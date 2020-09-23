package basicmembermanagement.enums;

import lombok.Getter;

public enum UsageType {
    ELECTRICITY("Elektrik", true, true),
    WATER("Su", true, true),
    ANNUAL("Aidat", false, true),
    PENALTY("Ceza", false, true),
    OTHER("Diğer", true, false);

    @Getter
    public String tr;
    @Getter
    public boolean isIndividual;
    @Getter
    public boolean isAllNecessary;

    UsageType(String tr, boolean isIndividual, boolean isAllNecessary) {
        this.tr=tr;
        this.isIndividual = isIndividual;
        this.isAllNecessary = isAllNecessary;
    }


}
