package org.jeecg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
@Data
public class WeChatPayConfig {
    /**
     * 商户号
     */
    private String merchantId = "";
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath = "";
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber = "";
    /**
     * 商户APIV3密钥
     */
    private String apiV3key = "";
    /**
     * 支付订单描述
     */
    private String description = "";
}
