package org.jeecg.modules.demo.water.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.water.entity.WaterOrder;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Mapper
public interface WaterOrderMapper extends MPJBaseMapper<WaterOrder> {
    @Select("SELECT third_user_uuid from sys_third_account WHERE third_user_id = #{userId}")
    String getAppid(@Param("userId") String userId);
}
