package com.fourback.redisstudy.global.common.enums;

import lombok.Getter;

@Getter
public enum PrefixEnum {
    USER("user:"),
    ITEM("item:"),
    VIEW("view:"),
    HISTORY("history:"),
    USERNAME("username"),
    ITEM_VIEW("item:view"),
    USER_LIKE("user:like:"),
    UNIQUE_USER("user:unique"),
    ITEM_ENDING_AT("item:endingAt");

    private final String prefix;

    PrefixEnum(String prefix) {
        this.prefix = prefix;
    }
}
