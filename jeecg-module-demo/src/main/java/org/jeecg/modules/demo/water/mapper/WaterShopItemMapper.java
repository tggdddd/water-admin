package org.jeecg.modules.demo.water.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.water.entity.WaterShopItem;

import java.util.List;

/**
 * @Description: 实际商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Mapper
public interface WaterShopItemMapper extends MPJBaseMapper<WaterShopItem> {

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    @Delete("DELETE FROM water_shop_item where from_id = #{mainId}")
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<WaterShopItem>
     */
    @Select("SELECT * FROM water_shop_item where from_id = #{mainId}")
    public List<WaterShopItem> selectByMainId(@Param("mainId") String mainId);
}
