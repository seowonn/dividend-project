package com.example.dividend.exception;

import com.example.dividend.model.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividendException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public DividendException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
