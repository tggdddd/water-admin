package org.jeecg.modules.demo.water.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.bo.WechatOrderBO;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.constant.SendOrderConstant;
import org.jeecg.modules.demo.water.entity.*;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.mapper.WaterSendMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopCartMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.po.SubmitOrderParamsPO;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWetChatJSPayService;
import org.jeecg.modules.demo.water.vo.CartVo;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.OrderAndItems;
import org.jeecg.modules.demo.water.vo.OrderSendItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Service
public class WaterOrderServiceImpl extends MPJBaseServiceImpl<WaterOrderMapper, WaterOrder> implements IWaterOrderService {
    @Autowired
    WaterOrderMapper orderMapper;
    @Autowired
    WaterShopItemMapper itemMapper;
    @Autowired
    WaterShopCartMapper cartMapper;
    @Autowired
    WaterSendMapper sendMapper;
    @Autowired
    IWetChatJSPayService payService;

    @Override
    @Transactional
    public boolean updateOrderStatusPaid(String orderId) {
        Transaction transaction = payService.getByOwnOrder(orderId);
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            WaterOrder order = orderMapper.selectById(orderId);
            if (Objects.equals(order.getOrdreStatus(), OrderConstant.UNPAID)) {
                order.setOrdreStatus(OrderConstant.WAITING_SEND);
                orderMapper.updateById(order);
                // TODO: 2023/7/1 发送通知给骑手端
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateOrderStatusRefund(String orderId) {
        Transaction transaction = payService.getByOwnOrder(orderId);
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.REFUND)) {
            WaterOrder order = orderMapper.selectById(orderId);
            if (!Objects.equals(order.getOrdreStatus(), OrderConstant.REFUND)) {
                order.setOrdreStatus(OrderConstant.REFUND);
                orderMapper.updateById(order);
            }
            return true;
        }
        return false;
    }

    @Override
    public int deleteOrderByUsernameAndId(String orderId, String username) {
        LambdaQueryWrapper<WaterOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterOrder::getId, orderId)
                .eq(WaterOrder::getUserId, username);
        return orderMapper.delete(wrapper);
    }

    @Override
    public Page<OrderSendItemVO> pageOwnOrderItemWithOutSend(Page<OrderSendItemVO> page, String username) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .eq(WaterSend::getStatus, SendOrderConstant.SENDING)
                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId));
        List<OrderSendItemVO> records = p.getRecords();
        for (OrderSendItemVO record : records) {
            WaterOrder order = record.getOrder();
            String shopItemId = order.getShopItemId();
            String[] numbers = order.getNumber().split(",");
            String[] ids = order.getShopItemId().split(",");
            List<WaterShopItem> waterShopItems = itemMapper.selectJoinList(
                    WaterShopItem.class,
                    new MPJLambdaWrapper<WaterShopItem>()
                            .in(WaterShopItem::getId, Arrays.asList(ids))
                            .selectAll(WaterShopItem.class)
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModel)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.equals(s)) {
                        waterShopItem.setReserve(numbers[j]);
                        break;
                    }
                }
            }
            record.setItems(waterShopItems);
        }
        return p;
    }

    @Override
    public Page<OrderSendItemVO> pageOwnSendOrderItem(Page<OrderSendItemVO> page, String username) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .eq(WaterSend::getStatus, SendOrderConstant.FINISH)
                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId));
        List<OrderSendItemVO> records = p.getRecords();
        for (OrderSendItemVO record : records) {
            WaterOrder order = record.getOrder();
            String shopItemId = order.getShopItemId();
            String[] numbers = order.getNumber().split(",");
            String[] ids = order.getShopItemId().split(",");
            List<WaterShopItem> waterShopItems = itemMapper.selectJoinList(
                    WaterShopItem.class,
                    new MPJLambdaWrapper<WaterShopItem>()
                            .in(WaterShopItem::getId, Arrays.asList(ids))
                            .selectAll(WaterShopItem.class)
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModel)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.equals(s)) {
                        waterShopItem.setReserve(numbers[j]);
                        break;
                    }
                }
            }
            record.setItems(waterShopItems);
        }
        return p;
    }

    @Override
    public Page<OrderSendItemVO> pageOwnOrderAndItem(Page<OrderSendItemVO> page, String username) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId));
        List<OrderSendItemVO> records = p.getRecords();
        for (OrderSendItemVO record : records) {
            WaterOrder order = record.getOrder();
            String shopItemId = order.getShopItemId();
            String[] numbers = order.getNumber().split(",");
            String[] ids = order.getShopItemId().split(",");
            List<WaterShopItem> waterShopItems = itemMapper.selectJoinList(
                    WaterShopItem.class,
                    new MPJLambdaWrapper<WaterShopItem>()
                            .in(WaterShopItem::getId, Arrays.asList(ids))
                            .selectAll(WaterShopItem.class)
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModel)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.equals(s)) {
                        waterShopItem.setReserve(numbers[j]);
                        break;
                    }
                }
            }
            record.setItems(waterShopItems);
        }
        return p;
    }

    @Override
    @Transactional
    public boolean pickUpOrder(String orderId, String username) {

        WaterOrder waterOrder = orderMapper.selectById(orderId);
//        派送单生成
        WaterSend waterSend = new WaterSend();
        waterSend.setUserId(username);
        waterSend.setOrderId(orderId);
        waterSend.setStatus(SendOrderConstant.SENDING);
        waterSend.setStartTime(LocalDateTime.now());
        int i = sendMapper.updateById(waterSend);
        if (i == 0) {
            return false;
        }
//        订单状态修改
        waterOrder.setOrdreStatus(OrderConstant.SEND);
        i = orderMapper.updateById(waterOrder);
        if (i == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
//        todo 通知用户
        return true;
    }

    @Override
    @Transactional
    public boolean cancelOrder(String sendId, String username) {
        //        派送单状态修改
        WaterSend waterSend = sendMapper.selectById(sendId);
        waterSend.setStatus(SendOrderConstant.CANCEL);
        waterSend.setStartTime(LocalDateTime.now());
        int i = sendMapper.updateById(waterSend);
        if (i == 0) {
            return false;
        }
//        订单状态修改
        WaterOrder waterOrder = orderMapper.selectById(waterSend.getId());
        waterOrder.setOrdreStatus(OrderConstant.CANCEL);
        i = orderMapper.updateById(waterOrder);
        if (i == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        // TODO: 2023/6/30 通知用户
        return true;
    }

    @Override
    public PrepayWithRequestPaymentResponse generateWeChatOrder(String orderId) {
//        生成附件内容
        WaterOrder order = orderMapper.selectById(orderId);
        String[] numbers = order.getNumber().split(",");
        String[] shopIds = order.getShopItemId().split(",");
        MPJLambdaWrapper<WaterShopItem> cartIWrapper = new MPJLambdaWrapper<>();
        cartIWrapper.in(WaterShopItem::getId, Arrays.asList(shopIds))
                .selectAs(WaterShopItem::getName, "itemName")
                .selectAs(WaterShopItem::getId, "itemId")
                .selectAs(WaterShopModel::getModel, "modelName")
                .selectAs(WaterShop::getName, "shopName")
                .selectAs(WaterShopItem::getRetail, "price")
                .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
                .leftJoin(WaterShop.class, WaterShop::getId, WaterShopItem::getFromId);
        List<JSONObject> list = itemMapper.selectJoinList(JSONObject.class, cartIWrapper);
        HashMap<String, LinkedList<JSONObject>> map = new HashMap<>();
        for (JSONObject object : list) {
            for (int i = 0; i < shopIds.length; i++) {
                String itemId = shopIds[i];
                if (object.get("itemId").equals(itemId)) {
                    LinkedList<JSONObject> values = map.getOrDefault(object.getString("shopName"), new LinkedList<>());
                    object.put("number", numbers[i]);
                    values.add(object);
                    map.put(object.getString("shopName"), values);
                    break;
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> strings = map.keySet();
        BigDecimal totalPrice = new BigDecimal(0);
        for (String string : strings) {
            stringBuilder.append(string).append(":\r\n");
            LinkedList<JSONObject> jsonObjects = map.get(string);
            for (JSONObject object : jsonObjects) {
                stringBuilder.append("\t")
                        .append(object.getString("itemName"))
                        .append(":")
                        .append(object.getString("modelName"))
                        .append("\tX\t")
                        .append(object.getString("number"))
                        .append("\r\n");
                totalPrice = totalPrice.add(new BigDecimal(object.getString("number"))
                        .multiply(new BigDecimal(object.getString("price"))));
            }
        }
        return payService.createPreOrder(new WechatOrderBO(orderId, totalPrice.toString(), stringBuilder.toString()));
    }

    @Override
    public boolean submitOrder(SubmitOrderParamsPO params, String username) {
        WaterOrder order = orderMapper.selectById(params.getOrderId());
        if (!order.getUserId().equals(username) || !Objects.equals(order.getOrdreStatus(), OrderConstant.UNCERTAIN)) {
            return false;
        }
        order.setOrdreStatus(OrderConstant.UNPAID);
        int i = orderMapper.updateById(order);
        return i == 1;
    }

    @Override
    @Transactional
    public boolean finishOrder(String sendId, String username) {
        //        派送单状态修改
        WaterSend waterSend = sendMapper.selectById(sendId);
        waterSend.setStatus(SendOrderConstant.FINISH);
        waterSend.setStartTime(LocalDateTime.now());
        int i = sendMapper.updateById(waterSend);
        if (i == 0) {
            return false;
        }
//        订单状态修改
        WaterOrder waterOrder = orderMapper.selectById(waterSend.getId());
        waterOrder.setOrdreStatus(OrderConstant.EVALUATE);
        i = orderMapper.updateById(waterOrder);
        if (i == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public Page<OrderSendItemVO> pagePaidOrderItem(Page<OrderSendItemVO> page) {
        MPJLambdaWrapper<WaterOrder> wrapper = new MPJLambdaWrapper<WaterOrder>()
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .leftJoin(WaterSend.class, WaterSend::getOrderId, WaterOrder::getId)
                .eq(WaterOrder::getOrdreStatus, OrderConstant.WAITING_SEND);
        Page<OrderSendItemVO> orderPage = orderMapper.selectJoinPage(page, OrderSendItemVO.class, wrapper);
        List<OrderSendItemVO> records = orderPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            OrderSendItemVO orderSendItemVO = records.get(i);
            WaterOrder waterOrder = orderSendItemVO.getOrder();
            String shopItemId = waterOrder.getShopItemId();
            String number = waterOrder.getNumber();
            String[] ids = shopItemId.split(",");
            List<WaterShopItem> waterShopItems = itemMapper.selectJoinList(
                    WaterShopItem.class,
                    new MPJLambdaWrapper<WaterShopItem>()
                            .in(WaterShopItem::getId, Arrays.asList(ids))
                            .selectAll(WaterShopItem.class)
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModel)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            String[] numbers = number.split(",");
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.equals(s)) {
                        waterShopItem.setReserve(numbers[j]);
                        break;
                    }
                }
            }
            orderSendItemVO.setItems(waterShopItems);
        }
        return orderPage;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public WaterOrder reOrderAgain(String orderId, String username) {
//        获取商品及数量
        WaterOrder order = orderMapper.selectById(orderId);
        String[] shopItemIds = order.getShopItemId().split(",");
        String[] numbers = order.getNumber().split(",");
//       逐个检查商品是否有效
        for (int i = 0; i < shopItemIds.length; i++) {
            String itemId = shopItemIds[i];
            String number = numbers[i];
            MPJLambdaWrapper<WaterShopItem> checkWrapper = new MPJLambdaWrapper<>();
            checkWrapper.eq(WaterShopItem::getId, itemId)
                    .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                    .leftJoin(WaterShop.class, WaterShop::getId, WaterShopItem::getFromId)
                    .eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                    .ge(WaterShopItem::getReserve, number);
            long record = itemMapper.selectJoinCount(checkWrapper);
            if (record == 0) {
                return null;
            }
        }
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//        减少商品库存
        BigDecimal totalPrice = new BigDecimal(0);
        for (int i = 0; i < shopItemIds.length; i++) {
            String itemId = shopItemIds[i];
            String number = numbers[i];
            WaterShopItem item = itemMapper.selectById(itemId);
            int end = Integer.parseInt(item.getReserve()) - Integer.parseInt(number);
            if (end < 0) {
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
                return null;
            }
            item.setReserve(String.valueOf(end));
            itemMapper.updateById(item);
            totalPrice = totalPrice.add(new BigDecimal(number).multiply(new BigDecimal(item.getRetail())));
        }
//        生成订单
        WaterOrder waterOrder = new WaterOrder();
        waterOrder.setUserId(username);
        waterOrder.setOrdreStatus(OrderConstant.UNCERTAIN);
        waterOrder.setStartTime(LocalDateTime.now());
        waterOrder.setShopItemId(order.getShopItemId());
        waterOrder.setNumber(order.getNumber());
        waterOrder.setPrices(totalPrice.toString());
        int insert = orderMapper.insert(waterOrder);
        if (insert == 0) {
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return null;
        }
        return waterOrder;
    }

    @Override
    public boolean cancelOrderByUsernameAndId(String orderId, String username) {
        LambdaQueryWrapper<WaterOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterOrder::getId, orderId)
                .eq(WaterOrder::getUserId, username);
        WaterOrder waterOrder = orderMapper.selectOne(wrapper);
//        只能在付款前取消
        if (Objects.equals(waterOrder.getOrdreStatus(), OrderConstant.UNCERTAIN) ||
                Objects.equals(waterOrder.getOrdreStatus(), OrderConstant.UNPAID)) {
            waterOrder.setOrdreStatus(OrderConstant.CANCEL);
            waterOrder.setEndTime(LocalDateTime.now());
            return 1 == orderMapper.updateById(waterOrder);
        }
        return false;
    }

    @Override
    public ThinkResult generateOrderViewToJSON(String orderId) {
        WaterOrder waterOrder = orderMapper.selectById(orderId);
        if (waterOrder == null) {
            return ThinkResult.error("订单不存在");
        }
//        if(!waterOrder.getOrdreStatus().equals(OrderConstant.UNCERTAIN)&&!waterOrder.getOrdreStatus().equals(OrderConstant.UNPAID)){
//            return ThinkResult.error("订单状态异常");
//        }
        LambdaQueryWrapper<WaterShopItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(WaterShopItem::getId, Arrays.asList(waterOrder.getShopItemId().split(",")));
        List<WaterShopItem> waterShopItems = itemMapper.selectList(wrapper);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", waterShopItems);
        jsonObject.put("waterOrder", waterOrder);
        return ThinkResult.ok(jsonObject);
    }

    @Override
    public List<CartVo> getOrderShopItems(String orderId, String username) {
        LambdaQueryWrapper<WaterOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterOrder::getId, orderId)
                .eq(WaterOrder::getUserId, username);
        WaterOrder waterOrder = orderMapper.selectOne(wrapper);
        String[] numbers = waterOrder.getNumber().split(",");
        String[] shopIds = waterOrder.getShopItemId().split(",");
        MPJLambdaWrapper<WaterShopItem> cartIWrapper = new MPJLambdaWrapper<>();
        cartIWrapper.in(WaterShopItem::getId, Arrays.asList(shopIds))
                .selectAll(WaterShopItem.class)
                .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
                .selectAs(WaterShopModel::getModel, CartVo::getModelName);
        List<CartVo> list = itemMapper.selectJoinList(CartVo.class, cartIWrapper);
        for (CartVo cartVo : list) {
            cartVo.setNumber(Integer.valueOf(numbers[getNumIndex(cartVo, shopIds)]));
        }
        return list;
    }

    private int getNumIndex(CartVo cartVo, String[] shopIds) {
        for (int i = 0; i < shopIds.length; i++) {
            String shopId = shopIds[i];
            if (cartVo.getId().equals(shopId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ThinkResult generateOrderView(String orderId) {
        WaterOrder waterOrder = orderMapper.selectById(orderId);
        if (waterOrder == null) {
            return ThinkResult.error("订单不存在");
        }
        if (!waterOrder.getOrdreStatus().equals(OrderConstant.UNCERTAIN) && !waterOrder.getOrdreStatus().equals(OrderConstant.UNPAID)) {
            return ThinkResult.error("订单状态异常");
        }
        LambdaQueryWrapper<WaterShopItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(WaterShopItem::getId, Arrays.asList(waterOrder.getShopItemId().split(",")));
        List<WaterShopItem> waterShopItems = itemMapper.selectList(wrapper);
        OrderAndItems o = new OrderAndItems(waterOrder, waterShopItems);
        return ThinkResult.ok(o);
    }
}
