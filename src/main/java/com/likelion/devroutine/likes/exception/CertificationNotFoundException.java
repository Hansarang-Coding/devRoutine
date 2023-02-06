package com.likelion.devroutine.likes.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class CertificationNotFoundException extends NotFoundException {

    private static final String MESSAGE ="해당 인증글이 존재하지 않습니다";

    public CertificationNotFoundException() {
        super(MESSAGE);
    }
}
