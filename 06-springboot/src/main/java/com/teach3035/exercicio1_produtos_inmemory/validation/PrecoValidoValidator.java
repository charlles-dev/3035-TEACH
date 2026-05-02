package com.teach3035.exercicio1_produtos_inmemory.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class PrecoValidoValidator implements ConstraintValidator<PrecoValido, BigDecimal> {
    private double max;

    @Override
    public void initialize(PrecoValido constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.doubleValue() > 0 && value.doubleValue() <= max;
    }
}
