package com.backend.core.auth.exception;

import com.backend.core.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException(String message) {
        super(HttpStatus.BAD_REQUEST, message);  // BAD_REQUEST 상태 코드와 메시지 설정
    }
}
