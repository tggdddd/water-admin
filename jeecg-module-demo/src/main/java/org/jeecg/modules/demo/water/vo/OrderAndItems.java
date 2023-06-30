package org.jeecg.modules.demo.water.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAndItems {
    WaterOrder waterOrder;
    List<WaterShopItem> items;
}
