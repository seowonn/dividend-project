package com.example.dividend.exception.impl;

import com.example.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getMessage() {
        return "일치하지 않는 비밀번호입니다.";
    }
}
