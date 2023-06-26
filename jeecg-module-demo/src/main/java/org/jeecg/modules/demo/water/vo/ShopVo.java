package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.demo.water.entity.WaterShop;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShopVo extends WaterShop {
    private String retailPrice;
}
