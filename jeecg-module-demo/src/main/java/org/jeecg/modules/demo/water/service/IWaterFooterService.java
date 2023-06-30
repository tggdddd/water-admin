package org.jeecg.modules.demo.water.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.water.entity.WaterFooter;
import org.jeecg.modules.demo.water.vo.ShopVo;

/**
 * @Description: 足迹
 * @Author: jeecg-boot
 * @Date: 2023-06-30
 * @Version: V1.0
 */
public interface IWaterFooterService extends IService<WaterFooter> {
    void updateOrSaveFooter(String username, String shopId);

    Page<ShopVo> pageByUsername(String username, Page<ShopVo> page);

    void deleteByShopId(String username, String shopId);
}
