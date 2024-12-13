package com.backend.core.common.handler;

import com.backend.core.common.exception.CustomException;
import com.backend.core.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice  // 이 어노테이션을 사용하여 애플리케이션 전역에서 예외를 처리
public class GlobalExceptionHandler {

    // CustomException을 처리하는 핸들러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ApiResponse<Void> response = ApiResponse.fail(ex);  // ApiResponse.fail()을 사용하여 예외 처리
        return ResponseEntity.status(ex.getHttpStatus()).body(response);  // CustomException의 HTTP 상태 코드로 응답 반환
    }

    // 기타 예외를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        CustomException customException = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        ApiResponse<Void> response = ApiResponse.fail(customException);  // 일반 예외를 CustomException으로 래핑하여 처리
        return ResponseEntity.status(customException.getHttpStatus()).body(response);
    }
}
