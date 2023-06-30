package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

@Data
public class CartVo extends WaterShopItem {
    /**
     * 购物车的数量
     */
    private Integer number;
    /**
     * 商品不存在
     */
    private boolean isRemove;
}
