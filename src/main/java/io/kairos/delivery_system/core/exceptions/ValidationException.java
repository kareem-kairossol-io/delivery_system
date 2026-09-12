package io.kairos.delivery_system.core.exceptions;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;

public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message, ResponseCodesEnum.VALIDATION_FAILED);
    }
}
