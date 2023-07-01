package org.jeecg.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EnableWeChatPay.class)
public class WeChatConfigBean {
    @Autowired
    WeChatPayConfig weChatPayConfig;

    @Bean
    public RSAAutoCertificateConfig config() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(weChatPayConfig.getMerchantId())
                .privateKeyFromPath(weChatPayConfig.getPrivateKeyPath())
                .merchantSerialNumber(weChatPayConfig.getMerchantSerialNumber())
                .apiV3Key(weChatPayConfig.getApiV3key())
                .build();
    }
}
