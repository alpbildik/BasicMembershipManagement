package basicmembermanagement.util;

import basicmembermanagement.dto.UsageDTO;
import basicmembermanagement.enums.UsageType;
import basicmembermanagement.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class UsageValidationUtil {
    public static List<UsageDTO> validate(UsageType usageType, List<UsageDTO> usageToValidate, Double unitAmountVal, String description, boolean isFirstInput) throws ValidationException {
        //Necessary for all
        if (Objects.isNull(unitAmountVal) || unitAmountVal.equals(0.0)){
            throw new ValidationException("Birim kullanım ücreti boş olamaz");
        }



        if (usageType.isIndividual) {
            //Necessary for unit usage Electricity and Water
            if (usageType.isAllNecessary) {
                if (isFirstInput && usageToValidate.stream().anyMatch(usage -> Objects.isNull(usage.getLastUsageRecord()))) {
                    throw new ValidationException("Üye ilk endex boş olamaz");
                }

                if (usageToValidate.stream().anyMatch(usage -> Objects.isNull(usage.getNewUsageRecord()))) {
                    throw new ValidationException("Üye son endeks boş olamaz");
                }

            } else {
                if (StringUtils.isEmpty(description)) {
                    throw new ValidationException("Açıklama alanı boş olamaz");
                }
            }
        }

        return usageToValidate;
    }
}
