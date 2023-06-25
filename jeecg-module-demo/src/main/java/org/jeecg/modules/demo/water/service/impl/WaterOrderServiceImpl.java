package org.jeecg.modules.demo.water.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.springframework.stereotype.Service;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2023-06-25
 * @Version: V1.0
 */
@Service
public class WaterOrderServiceImpl extends ServiceImpl<WaterOrderMapper, WaterOrder> implements IWaterOrderService {

}
