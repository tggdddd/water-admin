package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.constant.AdConstant;
import org.jeecg.modules.demo.water.entity.*;
import org.jeecg.modules.demo.water.service.*;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/app/index")
public class IndexController {
    @Autowired
    IWaterShopService shopService;
    @Autowired
    IWaterShopItemService itemService;
    @Autowired
    IWaterClassService classService;
    @Autowired
    IWaterAdService adService;
    @Autowired
    ISysBaseAPI sysBaseAPI;
    @Autowired
    IWaterShopCartService cartService;
    @Autowired
    IWaterPromoteActivityService activityService;

    @RequestMapping("init")
    public ThinkResult getIndex(HttpServletRequest request) {
        JSONObject result = new JSONObject();
//      获取用于显示的分类标签
//        LambdaQueryWrapper<WaterClass> classWrapper = new LambdaQueryWrapper<>();
//        classWrapper.eq(WaterClass::getShowIndex, DictEnum.Enable.getValue());
//        classService.list()
//        获取用于显示的分类标签下的商品
        ThinkResult shop = getShop(1, 8);
        result.put("shop", shop.getData());
//        轮播图
        LambdaQueryWrapper<WaterAd> adWrapper = new LambdaQueryWrapper<WaterAd>()
                .eq(WaterAd::getType, AdConstant.USER)
                .orderByAsc(WaterAd::getSort);
        List<WaterAd> list = adService.list(adWrapper);
        result.put("ad", list);
//        通知  todo
//        购物车数量
        try {
            String username = JwtUtil.getUserNameByToken(request);
            long count = cartService.getCardCount(username);
            result.put("cartCount", count);
        } catch (Exception e) {
            result.put("cartCount", 0);
        }
//        推广活动

        List<WaterPromoteActivity> activities = activityService.list(new LambdaQueryWrapper<WaterPromoteActivity>()
                .eq(WaterPromoteActivity::getStatus, DictEnum.Enable.getValue())
                .orderByDesc(WaterPromoteActivity::getSort));
        JSONArray shareJSON = new JSONArray();
        for (WaterPromoteActivity activity : activities) {
            JSONObject tempJSON = new JSONObject();
            tempJSON.put("id", activity.getId());
            tempJSON.put("imageString", activity.getImage());
            shareJSON.add(tempJSON);
        }
        result.put("share", shareJSON);
        return ThinkResult.ok(result);
    }

    /**
     * 获取用于首页展示的标签下的商品
     */
    @RequestMapping("shop")
    public ThinkResult getShop(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize) {
        MPJLambdaWrapper<WaterShop> wrapper = new MPJLambdaWrapper<WaterShop>();
//        商品查询语句
        wrapper.eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                .selectAll(WaterShop.class)
                .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId)
                .select(WaterShopItem::getFromId).selectCount(WaterShopItem::getReserve)
                .selectMin(WaterShopItem::getRetail)
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                .groupBy(WaterShopItem::getFromId);
//        标签筛选语句
        wrapper.innerJoin(WaterClass.class, WaterClass::getId, WaterShop::getTypeId)
                .eq(WaterClass::getShowIndex, DictEnum.Enable.getValue());
        Page<ShopVo> page = new Page<>(pageNo, pageSize);
        IPage<ShopVo> pageList = shopService.selectJoinListPage(page, ShopVo.class, wrapper);
        return ThinkResult.ok(pageList);
    }
}
