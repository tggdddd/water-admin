//package org.jeecg.modules.demo.water.Configuration;
//
//import com.getui.push.v2.sdk.ApiHelper;
//import com.getui.push.v2.sdk.GtApiConfiguration;
//import com.getui.push.v2.sdk.api.PushApi;
//import com.getui.push.v2.sdk.api.StatisticApi;
//import com.getui.push.v2.sdk.api.UserApi;
//import com.getui.push.v2.sdk.common.ApiResult;
//import com.getui.push.v2.sdk.dto.req.Audience;
//import com.getui.push.v2.sdk.dto.req.message.PushChannel;
//import com.getui.push.v2.sdk.dto.req.message.PushDTO;
//import com.getui.push.v2.sdk.dto.req.message.PushMessage;
//import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
//import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
//import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
//import com.getui.push.v2.sdk.dto.req.message.android.Ups;
//import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
//import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
//import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//
//@Configuration
//public class UniPush {
//
//    @Autowired
//    ApiHelper apiHelper;
//
//    @Bean
//    public PushApi pushApi() {
//        return apiHelper.creatApi(PushApi.class);
//    }
//
//    @Bean
//    public UserApi userApi() {
//        return apiHelper.creatApi(UserApi.class);
//    }
//    @Bean
//    public StatisticApi statisticApi() {
//        return apiHelper.creatApi(StatisticApi.class);
//    }
//}
