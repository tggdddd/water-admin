package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.demo.water.entity.WaterShop;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShopVo extends WaterShop {
    /**
     * 最低价格
     */
    private String retail;
    /**
     * 总库存
     */
    private String reserve;
}
