package com.fourback.redisstudy.global.common.enums;

import lombok.Getter;

@Getter
public enum PrefixEnum {

    USER("user:");

    private String prefix;

    PrefixEnum(String prefix) {
        this.prefix = prefix;
    }
}
