package org.jeecg.modules.demo.water.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.util.List;

/**
 * @Description: 售卖商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface IWaterShopItemService extends MPJBaseService<WaterShopItem> {

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<WaterShopItem>
     */
    public List<WaterShopItem> selectByMainId(String mainId);
}
