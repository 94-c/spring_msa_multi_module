package com.backend.core.common.response;

import com.backend.core.common.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        @NotNull boolean success,
        @Nullable T data,
        @Nullable String errorCode,
        @Nullable String errorMessage,
        @Nullable String errorDetails
) {

    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.OK, true, data, null, null, null);
    }

    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.CREATED, true, data, null, null, null);
    }

    public static <T> ApiResponse<T> fail(final CustomException exception) {
        return new ApiResponse<>(exception.getHttpStatus(), false, null,
                "ERROR", exception.getMessage(), exception.toString());
    }
}
