package com.islizx.model.enums;

/**
 * @author lizx
 * @date 2020-02-14 - 16:23
 */
public enum TrueFalseEnum {

    /**
     * 真
     */
    TRUE("true"),

    /**
     * 假
     */
    FALSE("false");

    private String value;

    TrueFalseEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
