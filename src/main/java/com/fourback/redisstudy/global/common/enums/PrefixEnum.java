package com.fourback.redisstudy.global.common.enums;

import lombok.Getter;

@Getter
public enum PrefixEnum {

    USER("user:"),
    ITEM("item:"),
    UNIQUE_USER("user:unique"),
    USER_LIKE("user:like:");

    private final String prefix;

    PrefixEnum(String prefix) {
        this.prefix = prefix;
    }
}
