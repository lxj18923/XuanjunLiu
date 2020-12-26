package com.lxj.common.enums;

/**
 * Created by LXJ
 */
public enum ProductStatusEnum{
    ON_SALE(1,"在线");
    private String value;
    private int code;
    ProductStatusEnum(int code,String value){
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
