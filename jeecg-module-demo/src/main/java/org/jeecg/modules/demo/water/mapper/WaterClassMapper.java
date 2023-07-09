package org.jeecg.modules.demo.water.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.demo.water.dashboardController.Bean.TotalOrderBean;
import org.jeecg.modules.demo.water.entity.WaterClass;

import java.util.List;
import java.util.Map;

/**
 * @Description: 分类
 * @Author: jeecg-boot
 * @Date: 2023-06-29
 * @Version: V1.0
 */
public interface WaterClassMapper extends BaseMapper<WaterClass> {

    /**
     * 编辑节点状态
     *
     * @param id
     * @param status
     */
    void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 【vue3专用】根据父级ID查询树节点数据
     *
     * @param pid
     * @param query
     * @return
     */
    List<SelectTreeModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

    @Select("SELECT COUNT(DISTINCT userid) " +
            "FROM water_footer")
    Long appUserTotal();

    @Select("SELECT COUNT(DISTINCT userid) " +
            "FROM water_footer " +
            "WHERE YEAR(update_time) = YEAR(CURRENT_DATE) " +
            "  AND MONTH(update_time) = MONTH(CURRENT_DATE);")
    Long appUserTotalCurrentMonth();

    @Select("SELECT COUNT(prices) " +
            "FROM water_order " +
            "WHERE ordre_status = 5 " +
            "AND YEAR(update_time) = YEAR(CURRENT_DATE) " +
            "AND MONTH(update_time) = MONTH(CURRENT_DATE)")
    Long userPurchaseTotalCurrentMonth();

    @Select("SELECT COUNT(prices) " +
            "FROM water_order ")
    Long userPurchaseTotal();

    @Select("SELECT COUNT(*) FROM water_order " +
            "WHERE ordre_status > 0 " +
            "AND YEAR(update_time) = YEAR(CURRENT_DATE) " +
            "AND MONTH(update_time) = MONTH(CURRENT_DATE) " +
            "AND WEEK(update_time) = WEEK(CURRENT_DATE)")
    Long orderTotalCurrentWeek();

    @Select("SELECT COUNT(*) FROM water_order ")
    Long orderTotal();

    @Select("SELECT COUNT(*) FROM water_order " +
            "WHERE ordre_status = 5 " +
            "AND YEAR(update_time) = YEAR(CURRENT_DATE) ")
    Long orderFinishYearTotal();

    @Select("SELECT COUNT(*) FROM water_order " +
            "WHERE ordre_status = 5 ")
    Long orderFinishTotal();

    List<TotalOrderBean> totalOrderByDate(@Param("create_time_begin") String beginTime, @Param("create_time_end") String endTime);
}
