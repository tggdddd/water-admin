package org.jeecg.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty("wechat.pay.merchantId")
@Configuration
public class EnableWeChatPay {

}
