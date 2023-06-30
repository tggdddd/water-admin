package org.jeecg.modules.demo.water.po;

import lombok.Data;

@Data
public class WeChatAddress {
    private String userName;
    private String postalCode;
    private String provinceName;
    private String cityName;
    private String countyName;
    private String streetName;
    private String detailInfo;
    private String detailInfoNew;
    private String nationalCode;
    private String telNumber;
}
