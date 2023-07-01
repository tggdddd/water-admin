package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterSend;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.util.List;

@Data
public class OrderSendItemVO {
    WaterOrder order;
    WaterSend send;
    List<WaterShopItem> items;
}