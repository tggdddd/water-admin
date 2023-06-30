package org.jeecg.modules.demo.water.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.entity.WaterShopModel;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.jeecg.modules.demo.water.vo.CartVo;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.OrderAndItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    IWaterShopCartService cartService;

    @Override
    public int deleteOrderByUsernameAndId(String orderId, String username) {
        LambdaQueryWrapper<WaterOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterOrder::getId, orderId)
                .eq(WaterOrder::getUserId, username);
        return orderMapper.delete(wrapper);
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
