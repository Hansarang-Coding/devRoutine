package com.likelion.devroutine.certification.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class CertificationNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 인증을 찾을 수 없습니다.";

    public CertificationNotFoundException() {
        super(MESSAGE);
    }
}
