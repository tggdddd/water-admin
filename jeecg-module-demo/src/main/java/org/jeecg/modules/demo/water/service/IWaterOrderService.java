package org.jeecg.modules.demo.water.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.po.SubmitOrderParamsPO;
import org.jeecg.modules.demo.water.vo.CartVo;
import org.jeecg.modules.demo.water.vo.OrderSendItemVO;
import org.jeecg.modules.demo.water.vo.SaleVO;

import java.util.List;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface IWaterOrderService extends MPJBaseService<WaterOrder> {

    /**
     * 获得订单的商品
     */
    List<CartVo> getOrderShopItems(String orderId, String username);

    ThinkResult generateOrderView(String orderId);

    ThinkResult generateOrderViewToJSON(String orderId);


    int deleteOrderByUsernameAndId(String orderId, String userNameByToken);

    boolean cancelOrderByUsernameAndId(String orderId, String userNameByToken);

    WaterOrder reOrderAgain(String orderId, String username);

    /**
     * 获取待发货订单及购买的商品  reserve为购买数量
     */
    Page<OrderSendItemVO> pagePaidOrderItem(Page<OrderSendItemVO> page, String username);

    /**
     * 获取已接取待发送订单与商品
     */
    Page<OrderSendItemVO> pageOwnOrderItemWithOutSend(Page<OrderSendItemVO> objectPage, String username);

    /**
     * 获取已接取已发送订单与商品
     */
    Page<OrderSendItemVO> pageOwnSendOrderItem(Page<OrderSendItemVO> objectPage, String username);

    /**
     * 获取已接取所有订单与商品
     */
    Page<OrderSendItemVO> pageOwnOrderAndItem(Page<OrderSendItemVO> objectPage, String username);

    /**
     * 领取派送订单
     */
    boolean pickUpOrder(String orderId, String username);

    /**
     * 取消订单
     */
    boolean cancelOrder(String sendId, String username);

    /**
     * 完成派送订单
     */
    boolean finishOrder(String sendId, String username);

    /**
     * 确定订单
     */
    boolean submitOrder(SubmitOrderParamsPO params, String username);

    /**
     * 根据订单生成微信支付订单
     */
    PrepayWithRequestPaymentResponse generateWeChatOrder(String orderId);

    /**
     * 检查订单是否已支付，支付则更新状态
     */
    boolean updateOrderStatusPaid(String orderId);

    /**
     * 检查订单是否已退款，退款了则更新状态
     */

    boolean updateOrderStatusRefund(String outTradeNo);

    /**
     * 统计每天的订单数
     */
    Page<SaleVO> calculateSale(Page<SaleVO> objectPage, String username);

    /**
     * 根据日期查询订单
     */
    Page<OrderSendItemVO> calculateSaleDetail(String time, Page<OrderSendItemVO> saleVOPage);

    /**
     * 根据orderId获取微信订单
     */
    void getPayOrder(String orderId);

    /**
     * 确认收货
     */
    boolean confirmReceipt(String orderId);

}
