package com.fourback.redisstudy.global.common.enums;

import lombok.Getter;

@Getter
public enum PrefixEnum {

    USER("user:"),
    ITEM("item:"),
    UNIQUE_USER("user:unique"),
    USER_LIKE("user:like:"),
    USERNAME("username"),
    ITEM_VIEW("item:view"),
    ITEM_ENDING_AT("item:endingAt"),
    VIEW("view:"),
    HISTORY("history:");

    private final String prefix;

    PrefixEnum(String prefix) {
        this.prefix = prefix;
    }
}
