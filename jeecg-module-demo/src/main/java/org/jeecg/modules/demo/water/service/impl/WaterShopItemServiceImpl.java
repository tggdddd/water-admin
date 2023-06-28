package org.jeecg.modules.demo.water.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 售卖商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Service
public class WaterShopItemServiceImpl extends MPJBaseServiceImpl<WaterShopItemMapper, WaterShopItem> implements IWaterShopItemService {

    @Autowired
    private WaterShopItemMapper waterShopItemMapper;

    @Override
    public List<WaterShopItem> selectByMainId(String mainId) {
        return waterShopItemMapper.selectByMainId(mainId);
    }
}
