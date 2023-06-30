package org.jeecg.modules.demo.water.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.vo.CartVo;

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
}
