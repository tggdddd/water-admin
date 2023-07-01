package org.jeecg.modules.demo.water.po;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubmitOrderParamsPO {
    @NotNull
    private WeChatAddress address;
    private String remark;
    @NotNull
    private String sysCode;
    @NotNull
    private String orderId;
}
