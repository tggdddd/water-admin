package org.jeecg.modules.demo.water.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.mapper.WaterShopItemMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopMapper;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Service
public class WaterShopServiceImpl extends MPJBaseServiceImpl<WaterShopMapper, WaterShop> implements IWaterShopService {

    @Autowired
    private WaterShopMapper waterShopMapper;
    @Autowired
    private WaterShopItemMapper waterShopItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(WaterShop waterShop, List<WaterShopItem> waterShopItemList) {
        waterShopMapper.insert(waterShop);
        if (waterShopItemList != null && waterShopItemList.size() > 0) {
            for (WaterShopItem entity : waterShopItemList) {
                //外键设置
                entity.setFromId(waterShop.getId());
                waterShopItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(WaterShop waterShop, List<WaterShopItem> waterShopItemList) {
        waterShopMapper.updateById(waterShop);

        //1.先删除子表数据
        waterShopItemMapper.deleteByMainId(waterShop.getId());

        //2.子表数据重新插入
        if (waterShopItemList != null && waterShopItemList.size() > 0) {
            for (WaterShopItem entity : waterShopItemList) {
                //外键设置
                entity.setFromId(waterShop.getId());
                waterShopItemMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        waterShopItemMapper.deleteByMainId(id);
        waterShopMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            waterShopItemMapper.deleteByMainId(id.toString());
            waterShopMapper.deleteById(id);
        }
    }

}
