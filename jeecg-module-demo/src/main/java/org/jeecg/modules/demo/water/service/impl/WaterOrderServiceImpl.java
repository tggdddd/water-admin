package org.jeecg.modules.demo.water.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.bo.SysUser;
import org.jeecg.modules.demo.water.bo.WechatOrderBO;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.constant.PaidConstant;
import org.jeecg.modules.demo.water.constant.SendOrderConstant;
import org.jeecg.modules.demo.water.entity.*;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.mapper.WaterSendMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopCartMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.po.CreateOrderBySendPO;
import org.jeecg.modules.demo.water.po.SubmitOrderParamsPO;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.service.IWetChatJSPayService;
import org.jeecg.modules.demo.water.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Autowired
    ISysBaseAPI sysBaseAPI;
    @Autowired
    CommonAPI commonAPI;
    @Autowired
    IWaterShopService shopService;

    @Override
    @Transactional
    public String createOrderWithOutPaid(String username, CreateOrderBySendPO params) {
        WaterShopItem item = itemMapper.selectById(params.getShopItemId());
        if (item.getStatus().equals(DictEnum.Disable.getValue())) {
            return "该商品未启用";
        }
        int reserve = Integer.parseInt(item.getReserve()) - Integer.parseInt(params.getNumber());
        if (reserve < 0) {
            return "库存不足";
        }
//        减少商品库存
        item.setReserve(String.valueOf(reserve));
        itemMapper.updateById(item);
        BigDecimal totalPrice = new BigDecimal(item.getRetail()).multiply(new BigDecimal(params.getNumber()));
//        修改商品的销量
        WaterShop shop = shopService.getById(item.getFromId());
        shop.setSale(String.valueOf(Integer.parseInt(shop.getSale()) + Integer.parseInt(params.getNumber())));
        shopService.updateById(shop);
//        生成订单
        WaterOrder waterOrder = new WaterOrder();
        waterOrder.setUserId(username);
        waterOrder.setOrdreStatus(OrderConstant.WAITING_SEND);
        waterOrder.setStartTime(LocalDateTime.now());
        waterOrder.setShopItemId(item.getId());
        waterOrder.setNumber(params.getNumber());
        waterOrder.setPrices(totalPrice.toString());
        waterOrder.setCreateTime(new Date());
        waterOrder.setPaidType(String.valueOf(params.getPaidType()));
        waterOrder.setAddress(params.getAddress());
        waterOrder.setArea(params.getArea());
        waterOrder.setLocatonType(params.getLocationCode());
        waterOrder.setName(params.getName());
        waterOrder.setPhone(params.getPhone());
        waterOrder.setRemark(params.getRemark());
        int insert = orderMapper.insert(waterOrder);
        if (insert == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "订单创建失败，请重试";
        }
//        创建派送单
        if (!generateSendOrder(waterOrder.getId(), params.getLocationCode())) {
            return "订单创建失败，请重试";
        }
        return null;
    }

    @Override
    public boolean confirmReceipt(String orderId) {
//      todo  将订单的状态修改为待评价
        WaterOrder waterOrder = orderMapper.selectById(orderId);
        waterOrder.setOrdreStatus(OrderConstant.FINISH);
        int i = orderMapper.updateById(waterOrder);
        return i == 1;
    }

    @Override
    public void getPayOrder(String orderId) {
        Transaction byOwnOrder = payService.getByOwnOrder(orderId);
    }

    @Override
    public Page<OrderSendItemVO> calculateSaleDetail(String time, Page<OrderSendItemVO> page, String sendName, String address, String receiveName, String phone) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .in(WaterOrder::getOrdreStatus, OrderConstant.SEND, OrderConstant.WAITING_SEND, OrderConstant.FINISH, OrderConstant.EVALUATE)
                .eq("DATE(t1.start_time)", LocalDate.parse(time.split(" ")[0]))

                .like(StringUtils.isNotBlank(address), WaterOrder::getAddress, address)
                .like(StringUtils.isNotBlank(receiveName), WaterOrder::getName, receiveName)
                .like(StringUtils.isNotBlank(phone), WaterOrder::getPhone, phone)


                .like(StringUtils.isNotBlank(sendName), SysUser::getRealname, sendName)

                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId)
                .leftJoin(SysUser.class, SysUser::getUsername, WaterOrder::getCreateBy)
                .disableSubLogicDel());
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
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModelName)
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
    public Page<SaleVO> calculateSale(Page<SaleVO> objectPage, String username) {
        MPJLambdaWrapper<WaterOrder> wrapper = new MPJLambdaWrapper<WaterOrder>();
        wrapper.in(WaterOrder::getOrdreStatus, OrderConstant.SEND, OrderConstant.WAITING_SEND, OrderConstant.FINISH, OrderConstant.EVALUATE)
                .selectAs("date_format(start_time, '%Y-%m-%d')", SaleVO::getDate)
                .selectCount(WaterOrder::getId, SaleVO::getSaleTotal)
                .selectSum(WaterOrder::getPrices, SaleVO::getAmountTotal)
                .in(WaterOrder::getLocatonType, getDepartCodes(username))
                .groupBy("date")
                .orderByDesc("date");
        Page<SaleVO> p = orderMapper.selectJoinPage(objectPage, SaleVO.class, wrapper);
        return p;
    }

    @Override
    @Transactional
    public boolean updateOrderStatusPaid(String orderId) {
        Transaction transaction = payService.getByOwnOrder(orderId);
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            WaterOrder order = orderMapper.selectById(orderId);
            if (Objects.equals(order.getOrdreStatus(), OrderConstant.UNPAID)) {
                order.setOrdreStatus(OrderConstant.WAITING_SEND);
                orderMapper.updateById(order);
//                创建派送单
                generateSendOrder(orderId, order.getLocatonType());
            }
            return true;
        }
        return false;
    }

    private boolean generateSendOrder(String orderId, String sysOrgCode) {
        WaterSend waterSend = new WaterSend();
        waterSend.setOrderId(orderId);
        waterSend.setSysOrgCode(sysOrgCode);
        waterSend.setStatus(SendOrderConstant.WAITING_GET);
        int result = sendMapper.insert(waterSend);
        if (result == 0) {
            return false;
        }
        List<String> ids = sysBaseAPI.getAllUserIdContainOrgCode(sysOrgCode);
        sysBaseAPI.sendWebSocketMsg(ids.toArray(new String[0]), "user");
        return true;
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
    public Page<OrderSendItemVO> pageOwnOrderItemWithOutSend(Page<OrderSendItemVO> page, String username, String address, String receiveName, String phone) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .eq(WaterSend::getStatus, SendOrderConstant.SENDING)
                .in(WaterSend::getSysOrgCode, getDepartCodes(username))
                .like(StringUtils.isNotBlank(address), WaterOrder::getAddress, address)
                .like(StringUtils.isNotBlank(receiveName), WaterOrder::getName, receiveName)
                .like(StringUtils.isNotBlank(phone), WaterOrder::getPhone, phone)
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
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModelName)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.getId().equals(s)) {
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
    public Page<OrderSendItemVO> pageOwnSendOrderItem(Page<OrderSendItemVO> page, String username, String address, String receiveName, String phone) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .in(WaterSend::getSysOrgCode, getDepartCodes(username))
                .eq(WaterSend::getStatus, SendOrderConstant.FINISH)
                .like(StringUtils.isNotBlank(address), WaterOrder::getAddress, address)
                .like(StringUtils.isNotBlank(receiveName), WaterOrder::getName, receiveName)
                .like(StringUtils.isNotBlank(phone), WaterOrder::getPhone, phone)
                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId)
                .disableSubLogicDel());
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
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModelName)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.getId().equals(s)) {
                        waterShopItem.setReserve(numbers[j]);
                        break;
                    }
                }
            }
            record.setItems(waterShopItems);
        }
        return p;
    }

    private List<String> getDepartCodes(String username) {
        return sysBaseAPI.getDepartSysCodesByUsername(username);
    }

    @Override
    public Page<OrderSendItemVO> pageOwnOrderAndItem(Page<OrderSendItemVO> page, String username, String address, String receiveName, String phone) {
        Page<OrderSendItemVO> p = sendMapper.selectJoinPage(page, OrderSendItemVO.class, new MPJLambdaWrapper<WaterSend>()
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .eq(WaterSend::getUserId, username)
                .in(WaterSend::getSysOrgCode, getDepartCodes(username))
                .like(StringUtils.isNotBlank(address), WaterOrder::getAddress, address)
                .like(StringUtils.isNotBlank(receiveName), WaterOrder::getName, receiveName)
                .like(StringUtils.isNotBlank(phone), WaterOrder::getPhone, phone)
                .leftJoin(WaterOrder.class, WaterOrder::getId, WaterSend::getOrderId)
                .disableSubLogicDel());
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
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModelName)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.getId().equals(s)) {
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
//        WaterSend waterSend = new WaterSend();
//        waterSend.setUserId(username);
//        waterSend.setOrderId(orderId);
//        waterSend.setStatus(SendOrderConstant.SENDING);
//        waterSend.setStartTime(LocalDateTime.now());
//        int i = sendMapper.insert(waterSend);
//        修改派送单状态
        WaterSend waterSend = sendMapper.selectOne(new LambdaQueryWrapper<WaterSend>()
                .eq(WaterSend::getOrderId, orderId).eq(WaterSend::getStatus, SendOrderConstant.WAITING_GET));
        if (!waterSend.getStatus().equals(SendOrderConstant.WAITING_GET)) {
            System.err.println("错误的派送订单状态：" + waterSend.getStatus());
            return false;
        }
        waterSend.setUserId(username);
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
        waterSend.setEndTime(LocalDateTime.now());
        int i = sendMapper.updateById(waterSend);
        if (i == 0) {
            return false;
        }
//        订单状态修改
        WaterOrder waterOrder = orderMapper.selectById(waterSend.getOrderId());
        waterOrder.setOrdreStatus(OrderConstant.WAITING_SEND);
        //        重新生成待派送单
        if (orderMapper.updateById(waterOrder) == 1 && generateSendOrder(waterSend.getOrderId(), waterOrder.getSysOrgCode())) {
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
//        订单状态修改
//        WaterOrder waterOrder = orderMapper.selectById(waterSend.getId());
//        waterOrder.setOrdreStatus(OrderConstant.CANCEL);
//        i = orderMapper.updateById(waterOrder);
//        if (i == 0) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return false;
//        }
//        // TODO: 2023/6/30 通知用户
    }

    @Override
    public PrepayWithRequestPaymentResponse generateWeChatOrder(String orderId) {
//        生成附件内容
        WaterOrder order = orderMapper.selectById(orderId);
        if (!order.getOrdreStatus().equals(OrderConstant.UNPAID)) {
            return null;
        }
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
        return payService.createPreOrder(new WechatOrderBO(orderId, totalPrice.toString(), stringBuilder.toString(), orderMapper.getAppid(order.getUserId())));
    }

    @Override
    @Transactional
    public boolean submitOrder(SubmitOrderParamsPO params, String username) {
        WaterOrder order = orderMapper.selectById(params.getOrderId());
        if (!order.getUserId().equals(username) || !Objects.equals(order.getOrdreStatus(), OrderConstant.UNCERTAIN)) {
            return false;
        }
        order.setAddress(params.getAddress().getAddress());
        order.setArea(params.getAddress().getArea());
        order.setPhone(params.getAddress().getPhone());
        order.setName(params.getAddress().getName());
        order.setRemark(params.getRemark());
        order.setStartTime(LocalDateTime.now());
        order.setLocatonType(params.getSysCode());
        order.setPaidType(params.getPaidType());
        order.setOrdreStatus(OrderConstant.UNPAID);
        boolean j = true;
        int i = 1;
        if (params.getPaidType().equals(PaidConstant.SEND_PAID)) {
            order.setOrdreStatus(OrderConstant.WAITING_SEND);
            i = orderMapper.updateById(order);
            j = generateSendOrder(order.getId(), order.getLocatonType());
        } else {
            i = orderMapper.updateById(order);
        }
        if (i == 1 && j) {
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    @Override
    @Transactional
    public boolean finishOrder(String sendId, String username, String paidType) {
        //        派送单状态修改
        WaterSend waterSend = sendMapper.selectById(sendId);
        waterSend.setStatus(SendOrderConstant.FINISH);
        waterSend.setEndTime(LocalDateTime.now());
        int i = sendMapper.updateById(waterSend);
        if (i == 0) {
            return false;
        }
//        订单状态修改
        WaterOrder waterOrder = orderMapper.selectById(waterSend.getOrderId());
        waterOrder.setOrdreStatus(OrderConstant.EVALUATE);
        if (paidType != null) {
            waterOrder.setPaidType(paidType);
        }
        i = orderMapper.updateById(waterOrder);
        if (i == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public Page<OrderSendItemVO> pagePaidOrderItem(Page<OrderSendItemVO> page, String username, String address, String receiveName, String phone) {
        MPJLambdaWrapper<WaterOrder> wrapper = new MPJLambdaWrapper<WaterOrder>()
                .eq(WaterOrder::getOrdreStatus, OrderConstant.WAITING_SEND)
                .selectAssociation(WaterOrder.class, OrderSendItemVO::getOrder)
                .selectAssociation(WaterSend.class, OrderSendItemVO::getSend)
                .leftJoin(WaterSend.class, WaterSend::getOrderId, WaterOrder::getId)
                .in(WaterSend::getSysOrgCode, getDepartCodes(username))
                .eq(WaterSend::getStatus, SendOrderConstant.WAITING_GET)
                .like(StringUtils.isNotBlank(address), WaterOrder::getAddress, address)
                .like(StringUtils.isNotBlank(receiveName), WaterOrder::getName, receiveName)
                .like(StringUtils.isNotBlank(phone), WaterOrder::getPhone, phone);
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
                            .selectAs(WaterShopModel::getModel, WaterShopItem::getModelName)
                            .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
            );
            String[] numbers = number.split(",");
            for (WaterShopItem waterShopItem : waterShopItems) {
                for (int j = 0; j < ids.length; j++) {
                    String s = ids[j];
                    if (waterShopItem.getId().equals(s)) {
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
