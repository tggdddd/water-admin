package org.jeecg.modules.demo.water.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.water.entity.WaterAd;

import java.util.List;

/**
 * @Description: 轮播图
 * @Author: jeecg-boot
 * @Date: 2023-07-05
 * @Version: V1.0
 */
public interface IWaterAdService extends IService<WaterAd> {
    /**
     * 派送端轮播图
     */
    List<WaterAd> sendCarousel();
}
