package org.jeecg.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
@ConditionalOnBean(EnableWeChatPay.class)
public class WeChatConfigBean {
    @Autowired
    WeChatPayConfig weChatPayConfig;

    @Bean
    public RSAAutoCertificateConfig config(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + weChatPayConfig.getPrivateKeyPath());
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(weChatPayConfig.getMerchantId())
                .privateKeyFromPath(resource.getFile().getPath())
                .merchantSerialNumber(weChatPayConfig.getMerchantSerialNumber())
                .apiV3Key(weChatPayConfig.getApiV3key())
                .build();
    }

}
