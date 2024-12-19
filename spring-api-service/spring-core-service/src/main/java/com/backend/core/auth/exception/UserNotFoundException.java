package com.backend.core.auth.exception;

import com.backend.core.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message); // 404 NOT_FOUND
    }
}
