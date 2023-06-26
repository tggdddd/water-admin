package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/app/shop")
public class ShopController {
    @Autowired
    IWaterShopService shopService;
    @Autowired
    IWaterShopItemService itemService;

    /**
     * 统计在售商品数
     */
    @RequestMapping("count")
    public ThinkResult countShopNumber() {
        long count = shopService.count(
                new LambdaQueryWrapper<WaterShop>()
                        .eq(WaterShop::getStatus, DictEnum.Enable.getValue()));
        return ThinkResult.ok(count);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("detail")
    public ThinkResult shopDetail(@RequestParam("id") String id) {
        WaterShop shop = shopService.getById(id);
        if (DictEnum.Disable.getValue().equals(shop.getStatus())) {
            return ThinkResult.error("该商品不存在或已下架");
        }
        JSONObject result = new JSONObject();
        int countItems = 0;
        List<WaterShopItem> waterShopItems = itemService.selectByMainId(id);
        ArrayList<WaterShopItem> canUse = new ArrayList<>();
        for (WaterShopItem waterShopItem : waterShopItems) {
            if (!Objects.equals(waterShopItem.getStatus(), DictEnum.Enable.getValue())) {
                continue;
            }
            canUse.add(waterShopItem);
            countItems += Integer.parseInt(waterShopItem.getReserve());
        }
        result.put("info", shop);
        result.put("count", countItems);
        result.put("products", canUse);
        return ThinkResult.ok(result);
    }
}
