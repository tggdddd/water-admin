package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.util.List;

@Data
public class ShopAndItem {
    List<WaterShop> shops;
    List<WaterShopItem> items;
}
