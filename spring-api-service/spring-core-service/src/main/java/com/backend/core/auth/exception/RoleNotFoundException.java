package com.backend.core.auth.exception;

import com.backend.core.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends CustomException {

    public RoleNotFoundException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);  // INTERNAL_SERVER_ERROR 상태 코드와 메시지 설정
    }
}
