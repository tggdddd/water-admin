package org.jeecg.modules.demo.water.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface IWaterShopService extends MPJBaseService<WaterShop> {

    /**
     * 添加一对多
     *
     * @param waterShop
     * @param waterShopItemList
     */
    public void saveMain(WaterShop waterShop, List<WaterShopItem> waterShopItemList);

    /**
     * 修改一对多
     *
     * @param waterShop
     * @param waterShopItemList
     */
    public void updateMain(WaterShop waterShop, List<WaterShopItem> waterShopItemList);

    /**
     * 删除一对多
     *
     * @param id
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     *
     * @param idList
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

}
