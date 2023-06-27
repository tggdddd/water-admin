package org.jeecg.modules.demo.water.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.water.entity.WaterShopModel;

import java.util.List;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
public interface WaterShopModelMapper extends BaseMapper<WaterShopModel> {

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<WaterShopModel>
     */
    public List<WaterShopModel> selectByMainId(@Param("mainId") String mainId);
}
