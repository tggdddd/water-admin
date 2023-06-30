package org.jeecg.modules.demo.water.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterShopCart;

import java.util.List;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface IWaterShopCartService extends MPJBaseService<WaterShopCart> {
    /**
     * 购物车检查，
     * 若无法支付，则更新购物车到能够支付状态
     * 并返回 失败哦
     * <p>
     * 否则 生成订单
     */
    WaterOrder payOrUpdate(List<WaterShopCart> jsonObjects, String username);

    /**
     * 获取购物车的可购买商品 数量
     */
    long getCardCount(String username);
}
