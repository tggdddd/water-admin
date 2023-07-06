//package org.jeecg.modules.demo.water.Configuration;
//
//import com.getui.push.v2.sdk.api.PushApi;
//import com.getui.push.v2.sdk.api.UserApi;
//import com.getui.push.v2.sdk.common.ApiResult;
//import com.getui.push.v2.sdk.dto.req.Audience;
//import com.getui.push.v2.sdk.dto.req.Condition;
//import com.getui.push.v2.sdk.dto.req.UserDTO;
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
//import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class PushUtils {
//@Autowired
//PushApi pushApi;
//@Autowired
//    UserApi userApi;
//public PushDTO<Audience> buildPushDTO(String title,String body,String clickType,String url,String notifyId){
//    PushDTO<Audience> pushDTO = new PushDTO<Audience>();
//    // 设置推送参数
//    pushDTO.setRequestId(System.currentTimeMillis() + "");
//    /**** 设置个推通道参数 *****/
//    PushMessage pushMessage = new PushMessage();
//    pushDTO.setPushMessage(pushMessage);
//    GTNotification notification = new GTNotification();
//    pushMessage.setNotification(notification);
//    notification.setTitle(title);
//    notification.setBody(body);
//    notification.setClickType(clickType);
//    notification.setUrl(url);
//    notification.setNotifyId(notifyId);
//    /**** 设置厂商相关参数 ****/
//    PushChannel pushChannel = new PushChannel();
//    pushDTO.setPushChannel(pushChannel);
//    /*配置安卓厂商参数*/
//    AndroidDTO androidDTO = new AndroidDTO();
//    pushChannel.setAndroid(androidDTO);
//    Ups ups = new Ups();
//    androidDTO.setUps(ups);
//    ThirdNotification thirdNotification = new ThirdNotification();
//    ups.setNotification(thirdNotification);
//    thirdNotification.setTitle(title);
//    thirdNotification.setBody(body);
//    thirdNotification.setClickType(clickType);
//    thirdNotification.setUrl(url);
//    // 两条消息的notify_id相同，新的消息会覆盖老的消息，取值范围：0-2147483647
//    thirdNotification.setNotifyId(notifyId);
//    /*配置安卓厂商参数结束，更多参数请查看文档或对象源码*/
//
//    /*设置ios厂商参数*/
//    IosDTO iosDTO = new IosDTO();
//    pushChannel.setIos(iosDTO);
//    // 相同的collapseId会覆盖之前的消息
//    iosDTO.setApnsCollapseId(notifyId);
//    Aps aps = new Aps();
//    iosDTO.setAps(aps);
//    Alert alert = new Alert();
//    aps.setAlert(alert);
//    alert.setTitle(title);
//    alert.setBody(body);
//    /*设置ios厂商参数结束，更多参数请查看文档或对象源码*/
//
//    /*设置接收人信息*/
//    Audience audience = new Audience();
//    pushDTO.setAudience(audience);
//    return pushDTO;
//}
//public void bindTag(String tag ,String cid){
//    userApi.usersBindTag(tag, UserDTO.build().addCid(cid));
//}
//public void bindTag(String tag,List<String> cidList){
//    UserDTO build = UserDTO.build();
//    for (String s : cidList) {
//        build.addCid(s);
//    }
//    userApi.usersBindTag(tag,build);
//}
//private PushDTO<Audience> addCid(PushDTO<Audience> pushDTO,String cid){
//    pushDTO.getAudience().addCid(cid);
//    return pushDTO;
//}
//public void pushSingleByCid(PushDTO<Audience> pushDTO,String cid){
//        // 进行cid单推
//        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(addCid(pushDTO,cid));
//        if (apiResult.isSuccess()) {
//            // success
//            System.out.println(apiResult.getData());
//        } else {
//            // failed
//            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
//        }
//}
//public void pushListByTag(PushDTO<Audience> pushDTO, List<Condition> tag){
//    pushDTO.getAudience().setTag(tag);
//    ApiResult<TaskIdDTO> apiResult = pushApi.pushByTag(pushDTO);
//    if (apiResult.isSuccess()) {
//        // success
//        System.out.println(apiResult.getData());
//    } else {
//        // failed
//        System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
//    }
//}
//}
