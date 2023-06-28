package org.jeecg.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.refund.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EnableWeChatPay.class)
public class WeChatPayConfiguration {
    @Autowired
    Config config;

    @Bean
    public JsapiServiceExtension getJsServiceExtension() {
        return new JsapiServiceExtension.Builder().config(config).build();
    }

    @Bean
    public RefundService getRefundService() {
        return new RefundService.Builder().config(config).build();
    }
}
