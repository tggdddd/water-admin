package org.jeecg.modules.demo.water.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.constant.PromoteConstant;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterPromoteWinning;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.mapper.WaterPromoteWinningMapper;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWaterPromoteWinningService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 推广活动获奖记录
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
@Service
public class WaterPromoteWinningServiceImpl extends MPJBaseServiceImpl<WaterPromoteWinningMapper, WaterPromoteWinning> implements IWaterPromoteWinningService {
    @Autowired
    IWaterShopItemService itemService;
    @Autowired
    IWaterOrderService orderService;
    @Autowired
    WaterPromoteWinningMapper winningMapper;

    @Override
    @Transactional
    public WaterOrder generateRewardOrder(WaterPromoteWinning record, String username) {
        WaterShopItem shopItem = itemService.getById(record.getShopItemId());
//       逐个检查商品是否有效
        MPJLambdaWrapper<WaterShopItem> checkWrapper = new MPJLambdaWrapper<>();
        checkWrapper.eq(WaterShopItem::getId, shopItem.getId())
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                .leftJoin(WaterShop.class, WaterShop::getId, WaterShopItem::getFromId)
                .eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                .ge(WaterShopItem::getReserve, 1);
        long recordReserve = itemService.selectJoinCount(checkWrapper);
        if (recordReserve == 0) {
            return null;
        }
//        减少商品库存
        int end = Integer.parseInt(shopItem.getReserve()) - 1;
        if (end < 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
        shopItem.setReserve(String.valueOf(end));
        itemService.updateById(shopItem);
        BigDecimal totalPrice = new BigDecimal(shopItem.getRetail()).subtract(new BigDecimal(record.getRelief()));
        if (totalPrice.compareTo(new BigDecimal(0)) < 0) {
            totalPrice = new BigDecimal(0);
        }
        //        生成订单
        WaterOrder waterOrder = new WaterOrder();
        waterOrder.setUserId(username);
        waterOrder.setOrdreStatus(OrderConstant.UNCERTAIN);
        waterOrder.setStartTime(LocalDateTime.now());
        waterOrder.setShopItemId(record.getShopItemId());
        waterOrder.setNumber("1");
        waterOrder.setPrices(totalPrice.toString());
        boolean insert = orderService.save(waterOrder);
        if (!insert) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
//        设置已领取
        record.setStatus(PromoteConstant.END);
        winningMapper.updateById(record);
        return waterOrder;
    }
}
