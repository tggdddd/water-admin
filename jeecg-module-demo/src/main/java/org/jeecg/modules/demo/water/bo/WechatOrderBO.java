package org.jeecg.modules.demo.water.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatOrderBO {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 支付价格   单位（元）
     */
    private String prices;
    /**
     * 订单附件内容
     */
    private String attach;
}
