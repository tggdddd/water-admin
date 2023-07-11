package org.jeecg.modules.demo.water.po;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterAddress;

import javax.validation.constraints.NotNull;

@Data
public class SubmitOrderParamsPO {
    @NotNull
    private WaterAddress address;
    private String remark;
    @NotNull
    private String sysCode;
    @NotNull
    private String orderId;
    @NotNull
    private String paidType;
}
