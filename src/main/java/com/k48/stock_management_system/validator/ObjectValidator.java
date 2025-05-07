package com.k48.stock_management_system.validator;

import com.k48.stock_management_system.exceptions.ObjectValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ObjectValidator<T> {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public void validate(T objToValidate) {

        Set<ConstraintViolation<T>> violations = validator.validate(objToValidate);

        if(!violations.isEmpty()){
            Set<String> errorMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            throw new ObjectValidationException(errorMessages,objToValidate.getClass().getName());
        }
    }


}
