package com.teach3035.exercicio2_produtos_jpa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrecoValidoValidator.class)
@Documented
public @interface PrecoValido {
    String message() default "Preço inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    double max() default 99999.99;
}

