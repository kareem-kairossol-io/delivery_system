package io.kairos.delivery_system.core.exceptions;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;

public abstract class ApplicationException
        extends RuntimeException {

    private final ResponseCodesEnum statusCode;

    protected ApplicationException(
            String message,
            ResponseCodesEnum statusCode
    ) {
        super(message);
        this.statusCode = statusCode;
    }

    public ResponseCodesEnum statusCode() {
        return statusCode;
    }
}