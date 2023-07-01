package org.jeecg.modules.demo.water.service.impl;

import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import org.jeecg.common.util.RestUtil;
import org.jeecg.config.EnableWeChatPay;
import org.jeecg.config.WeChatPayConfig;
import org.jeecg.config.thirdapp.ThirdAppTypeConfig;
import org.jeecg.modules.demo.water.bo.WechatOrderBO;
import org.jeecg.modules.demo.water.service.IWetChatJSPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@ConditionalOnBean(EnableWeChatPay.class)
public class WetChatJSPayServiceImpl implements IWetChatJSPayService {
    private final static String CALL_BACK_PATH = "/app/weChat/pay/callback";
    @Autowired
    @Lazy
    JsapiServiceExtension appServiceExtension;
    @Autowired
    ThirdAppTypeConfig thirdAppTypeConfig;
    @Autowired
    WeChatPayConfig wetChatPayConfig;
    @Autowired
    RefundService refundService;
    @Autowired
    NotificationParser notificationParser;
    private DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    private Duration duration = Duration.ofHours(24);

    private String getExpireTime() {
        return formatter.format(LocalDateTime.now().plus(duration));
    }

    @Override
    public PrepayWithRequestPaymentResponse createPreOrder(WechatOrderBO wechatOrderBO) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        String[] split = wechatOrderBO.getPrices().split("\\.");
        Integer moneyByPer = Integer.parseInt(split[0] + split[1]);
        amount.setTotal(moneyByPer);
        request.setAmount(amount);
        request.setAppid(thirdAppTypeConfig.getWECHAT_SMALL().getClientId());
        request.setMchid(wetChatPayConfig.getMerchantId());
        request.setDescription(wetChatPayConfig.getDescription());
        request.setAttach(wechatOrderBO.getAttach());
        request.setOutTradeNo(wechatOrderBO.getOrderId());
        request.setTimeExpire(getExpireTime());
        request.setNotifyUrl(RestUtil.getBaseUrl() + CALL_BACK_PATH);
        request.setOutTradeNo(wechatOrderBO.getOrderId());
        return appServiceExtension.prepayWithRequestPayment(request);
    }

    @Override
    public void cancelOrder(String orderId) {
        CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
        closeOrderRequest.setMchid(wetChatPayConfig.getMerchantId());
        closeOrderRequest.setOutTradeNo(orderId);
        appServiceExtension.closeOrder(closeOrderRequest);
    }

    public Refund refund(String wechatOrderId, String orderId, AmountReq amountReq, String reason) {
        CreateRequest request = new CreateRequest();
        request.setAmount(amountReq);
        request.setNotifyUrl(RestUtil.getBaseUrl() + CALL_BACK_PATH);
        request.setOutTradeNo(orderId);
        request.setOutRefundNo(orderId);
        request.setReason(reason);
        request.setTransactionId(wechatOrderId);
        return refundService.create(request);
    }

    @Override
    public Transaction getByWetChatOrder(String wechatOrder) {
        QueryOrderByIdRequest queryOrderByIdRequest = new QueryOrderByIdRequest();
        queryOrderByIdRequest.setMchid(wetChatPayConfig.getMerchantId());
        queryOrderByIdRequest.setTransactionId(wechatOrder);
        return appServiceExtension.queryOrderById(queryOrderByIdRequest);
    }

    public Transaction getByOwnOrder(String orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wetChatPayConfig.getMerchantId());
        request.setOutTradeNo(orderId);
        return appServiceExtension.queryOrderByOutTradeNo(request);
    }

    @Override
    public Transaction payCallBackDecode(RequestParam callbackParam) {
        return notificationParser.parse(callbackParam, Transaction.class);
    }
}
