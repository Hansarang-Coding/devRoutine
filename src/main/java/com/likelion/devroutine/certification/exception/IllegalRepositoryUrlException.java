package com.likelion.devroutine.certification.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class IllegalRepositoryUrlException extends BadRequestException {
    private static final String MESSAGE = "잘못된 Url 주소입니다.";

    public IllegalRepositoryUrlException() {
        super(MESSAGE);
    }
}
