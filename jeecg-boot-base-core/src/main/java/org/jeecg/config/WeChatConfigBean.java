package org.jeecg.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Configuration
@ConditionalOnBean(EnableWeChatPay.class)
public class WeChatConfigBean {
    @Autowired
    WeChatPayConfig weChatPayConfig;

    @Bean
    public RSAAutoCertificateConfig config(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + weChatPayConfig.getPrivateKeyPath());
        Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".pa");
        FileWriter writer = new FileWriter(tempFile.toFile());
        InputStream inputStream = resource.getInputStream();
        Reader reader = new InputStreamReader(inputStream);
        char[] chars = new char[1024];
        int len = reader.read(chars);
        while (len != -1) {
            writer.write(chars, 0, len);
            len = reader.read(chars);
        }
        writer.close();
        reader.close();
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(weChatPayConfig.getMerchantId())
                .privateKeyFromPath(tempFile.toString())
                .merchantSerialNumber(weChatPayConfig.getMerchantSerialNumber())
                .apiV3Key(weChatPayConfig.getApiV3key())
                .build();
    }

}
