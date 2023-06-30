package org.jeecg.modules.demo.water.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.modules.demo.water.entity.WaterFooter;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.mapper.WaterFooterMapper;
import org.jeecg.modules.demo.water.mapper.WaterShopMapper;
import org.jeecg.modules.demo.water.service.IWaterFooterService;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 足迹
 * @Author: jeecg-boot
 * @Date: 2023-06-30
 * @Version: V1.0
 */
@Service
public class WaterFooterServiceImpl extends ServiceImpl<WaterFooterMapper, WaterFooter> implements IWaterFooterService {
    @Autowired
    WaterFooterMapper footerMapper;
    @Autowired
    WaterShopMapper shopMapper;

    @Override
    public void deleteByShopId(String username, String shopId) {
        footerMapper.delete(new LambdaQueryWrapper<WaterFooter>().eq(WaterFooter::getUserid, username)
                .eq(WaterFooter::getShopid, shopId));
    }

    @Override
    public Page<ShopVo> pageByUsername(String username, Page<ShopVo> page) {
        MPJLambdaWrapper<WaterFooter> wrapper = new MPJLambdaWrapper<WaterFooter>().eq(WaterFooter::getUserid, username)
                .orderByDesc(WaterFooter::getUpdateTime)
                .leftJoin(WaterShop.class, WaterShop::getId, WaterFooter::getShopid)
                .selectFilter(WaterShop.class, e -> !e.getColumn().equals("update_time"))
                .select(WaterFooter::getUpdateTime);
        Page<ShopVo> shopVoPage = footerMapper.selectJoinPage(page, ShopVo.class, wrapper);
        List<ShopVo> records = shopVoPage.getRecords();
        for (ShopVo record : records) {
            ShopVo shopVo = shopMapper.selectJoinOne(ShopVo.class, new MPJLambdaWrapper<WaterShop>()
                    .eq(WaterShop::getId, record.getId())
                    .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId)
                    .selectCount(WaterShopItem::getReserve)
                    .selectMin(WaterShopItem::getRetail)
                    .groupBy(WaterShopItem::getFromId));
            record.setReserve(shopVo.getReserve());
            record.setRetail(shopVo.getRetail());
        }
        return shopVoPage;
    }

    @Override
    public void updateOrSaveFooter(String username, String shopId) {

        WaterFooter waterFooter = footerMapper.selectOne(new LambdaQueryWrapper<WaterFooter>().eq(WaterFooter::getUserid, username)
                .eq(WaterFooter::getShopid, shopId));
        if (waterFooter == null) {
            WaterFooter waterFooter1 = new WaterFooter();
            waterFooter1.setUserid(username);
            waterFooter1.setShopid(shopId);
            waterFooter1.setUpdateTime(LocalDateTime.now());
            footerMapper.insert(waterFooter1);
            return;
        }
        footerMapper.updateById(waterFooter);
    }

}
