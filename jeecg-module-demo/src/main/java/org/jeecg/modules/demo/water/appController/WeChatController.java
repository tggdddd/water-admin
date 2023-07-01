package org.jeecg.modules.demo.water.appController;

import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.jeecg.config.EnableWeChatPay;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
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
    @Autowired
    IWaterOrderService orderService;

    /**
     * 接收回调信息
     */
    @PostMapping("pay/callback")
    public void getRecord(@RequestBody RequestParam callbackParam) {
        /**支付订单*/
        Transaction transaction = wetChatJSPayService.payCallBackDecode(callbackParam);
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            orderService.updateOrderStatusPaid(transaction.getOutTradeNo());
        } else if (transaction.getTradeState().equals(Transaction.TradeStateEnum.REFUND)) {
            orderService.updateOrderStatusRefund(transaction.getOutTradeNo());
        }
    }
}
