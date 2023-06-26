package org.jeecg.modules.demo.water.vo;


public enum DictEnum {
    Enable("0"),
    Disable("1");
    private final String value;

    DictEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
