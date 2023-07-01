package org.jeecg.modules.demo.water.service;

import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.Refund;
import org.jeecg.modules.demo.water.bo.WechatOrderBO;

public interface IWetChatJSPayService {
    /**
     * 创建订单
     *
     * @return
     */
    PrepayWithRequestPaymentResponse createPreOrder(WechatOrderBO wechatOrderBO);

    /**
     * 取消订单
     */
    void cancelOrder(String orderId);

    /**
     * 退款
     */
    Refund refund(String wechatOrderId, String orderId, AmountReq amountReq, String reason);

    /**
     * 查询订单
     *
     * @Param order 微信订单
     */
    Transaction getByWetChatOrder(String wechatOrder);

    /**
     * 查询订单
     *
     * @Param order 商家订单
     */
    Transaction getByOwnOrder(String orderId);

    /**
     * 支付回调解析
     */
    Transaction payCallBackDecode(RequestParam callbackParam);
}
