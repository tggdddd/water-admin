package org.jeecg.modules.demo.water.vo;

import lombok.Data;
import org.jeecg.modules.demo.water.entity.WaterOrder;

@Data
public class OrderBriefVO extends WaterOrder {
    //    一张图片
    private String imageString;
    //    商品名
    private String shop;
}
