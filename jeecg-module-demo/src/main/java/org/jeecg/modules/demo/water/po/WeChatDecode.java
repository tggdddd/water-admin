package org.jeecg.modules.demo.water.po;

import lombok.Data;

import java.util.List;

@Data
public class WeChatDecode {

    private Amount amount;
    private String appid;
    private String attach;
    private String bankType;
    private String mchid;
    private String outTradeNo;
    private Payer payer;
    private List<PromotionDetail> promotionDetail;
    private SceneInfo sceneInfo;
    private String successTime;
    private String tradeState;
    private String tradeStateDesc;
    private String tradeType;
    private String transactionId;


}
