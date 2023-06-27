package org.jeecg.modules.demo.water.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.water.entity.WaterShopModel;

import java.util.List;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface IWaterShopModelService extends IService<WaterShopModel> {

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<WaterShopModel>
     */
    public List<WaterShopModel> selectByMainId(String mainId);
}
