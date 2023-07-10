package org.jeecg.modules.demo.water.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.config.EnableWeChatPay;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopCart;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopCartMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Service
@ConditionalOnClass(EnableWeChatPay.class)
public class WaterShopCartServiceImpl extends MPJBaseServiceImpl<WaterShopCartMapper, WaterShopCart> implements IWaterShopCartService {
    @Autowired
    WaterShopCartMapper cartMapper;
    @Autowired
    WaterShopItemMapper itemMapper;
    @Autowired
    IWaterShopService shopService;
    @Autowired
    WaterOrderMapper orderMapper;

    @Override
    public long getCardCount(String username) {
        MPJLambdaWrapper<WaterShopCart> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(WaterShopCart::getUserId, username)
                .gt(WaterShopCart::getNumber, 0)
                .innerJoin(WaterShopItem.class, WaterShopItem::getId, WaterShopCart::getShopId)
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue());
        return cartMapper.selectJoinCount(wrapper);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public WaterOrder payOrUpdate(List<WaterShopCart> list, String username) {
        //        逐个检查商品是否有效
        List<WaterShopItem> waitingProducts = new ArrayList<>();
        List<WaterShopCart> disables = new ArrayList<>();
        boolean canPay = true;
        for (WaterShopCart waterShopCart : list) {
            MPJLambdaWrapper<WaterShopItem> checkWrapper = new MPJLambdaWrapper<>();
            checkWrapper.eq(WaterShopItem::getId, waterShopCart.getShopId())
                    .selectAll(WaterShopItem.class)
                    .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                    .leftJoin(WaterShop.class, WaterShop::getId, WaterShopItem::getFromId)
                    .eq(WaterShop::getStatus, DictEnum.Enable.getValue());
            WaterShopItem items = itemMapper.selectJoinOne(WaterShopItem.class, checkWrapper);
            if (items == null) {
                canPay = false;
                disables.add(waterShopCart);
                continue;
            }
            waitingProducts.add(items);
        }
//        检查商品的数量是否超标
        for (WaterShopItem item : waitingProducts) {
            WaterShopCart cartItem = filterByItemId(list, item.getId());
            int cartNum = Integer.parseInt(cartItem.getNumber());
            int reserve = Integer.parseInt(item.getReserve());
//                更新
            if (cartNum > reserve) {
                canPay = false;
                cartItem.setNumber(item.getReserve());
                cartMapper.updateById(cartItem);
            }
        }
//       对已下架商品 购物车为0
        for (WaterShopCart cartItem : disables) {
            cartItem.setNumber("0");
            cartMapper.updateById(cartItem);
        }
        if (!canPay) {
            return null;
        }
//        提交事务
        TransactionAspectSupport.currentTransactionStatus().flush();
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//        减少商品库存
        List<String> itemIds = new ArrayList<>();
        List<String> itemNumbers = new ArrayList<>();
        List<String> cartIds = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (WaterShopCart waterShopCart : list) {
            String itemId = waterShopCart.getShopId();
            String number = waterShopCart.getNumber();
            WaterShopItem item;
            try {
                item = getItemByIdMustExist(waitingProducts, itemId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int end = Integer.parseInt(item.getReserve()) - Integer.parseInt(number);
            item.setReserve(String.valueOf(end));
            itemMapper.updateById(item);
            if (!number.equals("0")) {
                itemIds.add(itemId);
                itemNumbers.add(number);
                cartIds.add(waterShopCart.getId());
            }
            totalPrice = totalPrice.add(new BigDecimal(number).multiply(new BigDecimal(item.getRetail())));

//        修改商品的销量
            WaterShop shop = shopService.getById(item.getFromId());
            shop.setSale(String.valueOf(Integer.parseInt(shop.getSale()) + Integer.parseInt(number)));
            shopService.updateById(shop);
        }
//        生成订单
        WaterOrder waterOrder = new WaterOrder();
        waterOrder.setUserId(username);
        waterOrder.setOrdreStatus(OrderConstant.UNCERTAIN);
        waterOrder.setStartTime(LocalDateTime.now());
        waterOrder.setShopItemId(itemIds.stream().reduce((a, b) -> a + "," + b).get());
        waterOrder.setNumber(itemNumbers.stream().reduce((a, b) -> a + "," + b).get());
        waterOrder.setPrices(totalPrice.toString());
        waterOrder.setCreateTime(new Date());
        int insert = orderMapper.insert(waterOrder);
        if (insert == 0) {
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return null;
        }
//        清空购物车
        cartMapper.deleteBatchIds(cartIds);
        return waterOrder;
    }

    private WaterShopItem getItemByIdMustExist(List<WaterShopItem> list, String itemId) throws Exception {
        for (WaterShopItem item : list) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        throw new Exception("itemId不在list中");
    }

    /**
     * 获得对应对象
     */
    private WaterShopCart filterByItemId(List<WaterShopCart> list, WaterShopItem item) {
        return filterByItemId(list, item.getId());
    }

    /**
     * 获得对应对象
     */
    private WaterShopCart filterByItemId(List<WaterShopCart> list, String itemId) {
        if (itemId == null) {
            return null;
        }
        for (WaterShopCart waterShopCart : list) {
            if (waterShopCart.getShopId().equals(itemId)) {
                return waterShopCart;
            }
        }
        return null;
    }
}
