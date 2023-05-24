package com.likelion.devroutine.certification.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CrawlingUrl {
    BASE_URL("https://api.github.com");

    private String url;
}