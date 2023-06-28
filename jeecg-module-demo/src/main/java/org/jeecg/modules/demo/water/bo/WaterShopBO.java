package org.jeecg.modules.demo.water.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.modules.demo.water.entity.WaterOrder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterShopBO extends WaterOrder {
    private String goodAndItemName;
    private String model;

    public void setNameAndModel(String good, String item, String model) {
        this.goodAndItemName = good + ":" + item;
        this.model = model;
    }
}
