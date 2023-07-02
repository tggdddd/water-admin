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
    private String merchantId = "1637666135";
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath = "pay_private.pem";
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber = "4EDDD91B8B8772E926F8121978747AD4BA832A23";
    /**
     * 商户APIV3密钥
     */
    private String apiV3key = "26bd00691e6b43369e3e4e0753fcbce0";
    /**
     * 支付订单描述
     */
    private String description = "溪山贵水";
}
