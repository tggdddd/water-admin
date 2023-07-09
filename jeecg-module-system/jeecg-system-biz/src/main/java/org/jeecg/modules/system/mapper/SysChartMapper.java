package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date: 2019-05-29
 * @Version: V1.0
 */
public interface SysChartMapper extends BaseMapper {
    @Select("SELECT query_sql FROM onl_drag_dataset_head WHERE id= #{id}")
    String getApiById(@Param("id") String id);
}
