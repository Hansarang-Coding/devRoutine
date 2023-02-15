package com.likelion.devroutine.certification.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class CertificationForbiddenException extends BadRequestException {
    private static final String MESSAGE = "인증은 하루에 한 번 가능합니다.";

    public CertificationForbiddenException() {
        super(MESSAGE);
    }
}
