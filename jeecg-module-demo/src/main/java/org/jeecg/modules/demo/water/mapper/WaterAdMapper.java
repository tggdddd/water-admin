package org.jeecg.modules.demo.water.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.water.entity.WaterAd;

/**
 * @Description: 轮播图
 * @Author: jeecg-boot
 * @Date: 2023-07-05
 * @Version: V1.0
 */
public interface WaterAdMapper extends BaseMapper<WaterAd> {
    @Select("SELECT realname FROM sys_user WHERE username = #{username}")
    String getRealName(@Param("username") String username);
}
