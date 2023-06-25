package org.jeecg.modules.demo.water.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.demo.water.entity.WaterType;
import org.jeecg.modules.demo.water.mapper.WaterTypeMapper;
import org.jeecg.modules.demo.water.service.IWaterTypeService;
import org.springframework.stereotype.Service;

/**
 * @Description: 水的类型
 * @Author: jeecg-boot
 * @Date: 2023-06-25
 * @Version: V1.0
 */
@Service
public class WaterTypeServiceImpl extends ServiceImpl<WaterTypeMapper, WaterType> implements IWaterTypeService {

}
