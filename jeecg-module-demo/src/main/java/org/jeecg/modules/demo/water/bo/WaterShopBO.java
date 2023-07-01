package org.jeecg.modules.demo.water.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.modules.demo.water.entity.WaterOrder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaterShopBO extends WaterOrder {
    private String attach;

}
