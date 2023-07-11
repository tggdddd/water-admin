package org.jeecg.modules.demo.water.po;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateOrderBySendPO {
    @NotEmpty
    private String address;
    @NotEmpty
    private String area;
    @NotEmpty
    private String locationCode;
    @NotEmpty
    private String name;
    @NotEmpty
    private String number;
    @NotEmpty
    private Long paidType;
    @NotEmpty
    private String phone;
    private String remark;
    @NotEmpty
    private String shopItemId;


}
