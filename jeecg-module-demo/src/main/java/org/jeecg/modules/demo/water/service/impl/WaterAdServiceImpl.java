package org.jeecg.modules.demo.water.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.water.constant.AdConstant;
import org.jeecg.modules.demo.water.entity.WaterAd;
import org.jeecg.modules.demo.water.mapper.WaterAdMapper;
import org.jeecg.modules.demo.water.service.IWaterAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 轮播图
 * @Author: jeecg-boot
 * @Date: 2023-07-05
 * @Version: V1.0
 */
@Service
public class WaterAdServiceImpl extends ServiceImpl<WaterAdMapper, WaterAd> implements IWaterAdService {
    @Autowired
    WaterAdMapper adMapper;

    @Override
    public List<WaterAd> sendCarousel() {
        LambdaQueryWrapper<WaterAd> adWrapper = new LambdaQueryWrapper<WaterAd>()
                .eq(WaterAd::getType, AdConstant.SEND)
                .orderByAsc(WaterAd::getSort);
        return adMapper.selectList(adWrapper);
    }
}
