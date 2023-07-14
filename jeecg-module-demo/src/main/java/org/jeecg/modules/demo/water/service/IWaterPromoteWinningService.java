package org.jeecg.modules.demo.water.service;

import com.github.yulichang.base.MPJBaseService;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterPromoteWinning;

/**
 * @Description: 推广活动获奖记录
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
public interface IWaterPromoteWinningService extends MPJBaseService<WaterPromoteWinning> {
    /**
     * 领取奖品
     */
    WaterOrder generateRewardOrder(WaterPromoteWinning record, String username);
}
