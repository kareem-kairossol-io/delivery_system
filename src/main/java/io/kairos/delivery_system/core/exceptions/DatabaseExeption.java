package io.kairos.delivery_system.core.exceptions;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;

public class DatabaseExeption extends ApplicationException {

    public DatabaseExeption(ResponseCodesEnum statusCode, String message, Throwable cause) {
        super(message, statusCode);
        initCause(cause);
    }
}
