package org.jeecg.modules.demo.water.appController;

import org.jeecg.config.EnableWeChatPay;
import org.jeecg.modules.demo.water.po.WeChatCallBack;
import org.jeecg.modules.demo.water.service.IWetChatJSPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/wechat")
@ConditionalOnBean(EnableWeChatPay.class)
public class WeChatController {
    @Autowired
    IWetChatJSPayService wetChatJSPayService;

    /**
     * 接收回调信息
     */
    @PostMapping("callback")
    public void getRecord(@RequestBody WeChatCallBack chatCallBack) {

    /**支付订单*/
    }
}
