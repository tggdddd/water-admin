package org.jeecg.modules.demo.water.po;

import lombok.Data;

import java.util.List;

@Data
public class PromotionDetail {

    private Long amount;
    private String couponId;
    private String currency;
    private List<GoodsDetail> goodsDetail;
    private Long merchantContribute;
    private String name;
    private Long otherContribute;
    private String scope;
    private String stockId;
    private Long wechatpayContribute;


}
