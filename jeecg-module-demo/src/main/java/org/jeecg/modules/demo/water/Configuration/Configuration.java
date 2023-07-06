//package org.jeecg.modules.demo.water.Configuration;
//
//import com.getui.push.v2.sdk.ApiHelper;
//import com.getui.push.v2.sdk.GtApiConfiguration;
//import org.springframework.context.annotation.Bean;
//@org.springframework.context.annotation.Configuration
//public class Configuration {
//    //    AppID：    woVwP0gZkl8tHZkVrt7jc5
////    AppKey：    bj2CshW4vY5RiIqRSzaXB8
////    AppSecret：7F5jm9YOe79T8c5rJ6Jd51
////    MasterSecret：    Gqc8d27ecB7cBx1LfpwH16
//    @Bean
//    ApiHelper apiHelper(){
//        System.setProperty("http.maxConnections", "200");
//        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
//        //填写应用配置
//        apiConfiguration.setAppId("woVwP0gZkl8tHZkVrt7jc5");
//        apiConfiguration.setAppKey("bj2CshW4vY5RiIqRSzaXB8");
//        apiConfiguration.setMasterSecret("Gqc8d27ecB7cBx1LfpwH16");
//        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
//        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
//        // 实例化ApiHelper对象，用于创建接口对象
//        return ApiHelper.build(apiConfiguration);
//    }
//}
