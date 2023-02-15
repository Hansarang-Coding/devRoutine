package com.likelion.devroutine.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.cert.Certificate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantCertificationResponse {
    private Long challengeId;
    //private List<CertificationResponse> myCertifications;
    //private List<List<CertificationResponse>> othersCertifications;
}
