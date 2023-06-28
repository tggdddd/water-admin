package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

@Data
public class CartVo extends WaterShopItem {
    private Integer number;
}
