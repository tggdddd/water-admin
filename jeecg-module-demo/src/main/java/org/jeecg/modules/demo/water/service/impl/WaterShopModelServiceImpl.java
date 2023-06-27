package org.jeecg.modules.demo.water.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.water.entity.WaterShopModel;
import org.jeecg.modules.demo.water.mapper.WaterShopModelMapper;
import org.jeecg.modules.demo.water.service.IWaterShopModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Service
public class WaterShopModelServiceImpl extends ServiceImpl<WaterShopModelMapper, WaterShopModel> implements IWaterShopModelService {

    @Autowired
    private WaterShopModelMapper waterShopModelMapper;

    @Override
    public List<WaterShopModel> selectByMainId(String mainId) {
        return waterShopModelMapper.selectByMainId(mainId);
    }
}
