package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.*;
import org.jeecg.modules.demo.water.po.CartPo;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.service.IWaterShopModelService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.CartVo;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    IWaterShopModelService modelService;
    @Autowired
    IWaterShopCartService cartService;

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
        MPJLambdaWrapper<WaterShop> shopLambdaQueryWrapper = new MPJLambdaWrapper<>();
        shopLambdaQueryWrapper.eq(WaterShop::getId, id)
                .selectAll(WaterShop.class)
                .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId)
                .select(WaterShopItem::getFromId)
                .selectCount(WaterShopItem::getReserve)
                .selectMin(WaterShopItem::getRetail)
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                .groupBy(WaterShopItem::getFromId);
        ShopVo shop = shopService.selectJoinOne(ShopVo.class, shopLambdaQueryWrapper);
//        if (DictEnum.Disable.getValue().equals(shop.getStatus())) {
//            return ThinkResult.error("该商品不存在或已下架");
//        }
        List<WaterShopModel> models = modelService.selectByMainId(id);
        JSONObject result = new JSONObject();
        int countItems = 0;
        List<WaterShopItem> items = itemService.selectByMainId(id);
        ArrayList<WaterShopItem> canUse = new ArrayList<>();
        for (WaterShopItem waterShopItem : items) {
            if (!Objects.equals(waterShopItem.getStatus(), DictEnum.Enable.getValue())) {
                continue;
            }
            String modelName = "";
            for (WaterShopModel model : models) {
                if (model.getId().equals(waterShopItem.getModel())) {
                    modelName = model.getModel();
                    model.setNumber(model.getNumber() + Integer.parseInt(waterShopItem.getReserve()));
                    break;
                }
            }
            waterShopItem.setModelName(modelName);
            canUse.add(waterShopItem);
            countItems += Integer.parseInt(waterShopItem.getReserve());
        }
        result.put("info", shop);
        result.put("count", countItems);
        result.put("products", canUse);
        result.put("model", models);
        return ThinkResult.ok(result);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("add")
    public ThinkResult addToCart(HttpServletRequest request, @RequestBody CartPo cartPo) {
//        检查是否登录
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.notLogin();
        }
        //   type  0：正常加入购物车，1:立即购买，2:再来一单
        if (cartPo.getAddType() == 0) {
            return addToCartR(username, cartPo);
        } else if (cartPo.getAddType() == 1) {
            return fastBought(username, cartPo, true);
        } else if (cartPo.getAddType() == 2) {
            // TODO: 2023/6/27  
        }
        return ThinkResult.error("错误的标识");
    }


    /**
     * 支付该商品的所有购物
     */
    public ThinkResult fastBought(String username, CartPo cartPo, boolean onlyShop) {
//        先添加到购物车
        ThinkResult thinkResult = addToCartR(username, cartPo);
        if (thinkResult.getErrno() != 0) {
            return thinkResult;
        }
//        获取用户的购物车
        MPJLambdaWrapper<WaterShopCart> carWrapper = new MPJLambdaWrapper<>(WaterShopCart.class)
                .selectAll(WaterShopCart.class);
        carWrapper.eq(WaterShopCart::getUserId, username);
        if (onlyShop) {
            carWrapper.inSql(true,
                    WaterShopCart::getShopId,
                    "select id from water_shop_item where from_id ='" + cartPo.getGoodsId() + "'");
        }
        List<WaterShopCart> list = cartService.list(carWrapper);
//        创建订单
        WaterOrder waterOrder = cartService.payOrUpdate(list, username);
        if (waterOrder == null) {
            return ThinkResult.error("商品库存不足，已调整购物车数量");
        }
        return ThinkResult.ok(waterOrder);
    }

    /**
     * 添加到购物车
     */
    private ThinkResult addToCartR(String username, CartPo cartPo) {
        ThinkResult thinkResult = checkCart(cartPo);
        if (thinkResult.getErrno() != 0) {
            return thinkResult;
        }
        // 添加到购物车
        LambdaQueryWrapper<WaterShopCart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(WaterShopCart::getShopId, cartPo.getProductId())
                .eq(WaterShopCart::getUserId, username);
        WaterShopCart one = cartService.getOne(cartQueryWrapper);
        if (one == null) {
            WaterShopCart waterShopCart = new WaterShopCart();
            waterShopCart.setShopId(cartPo.getProductId());
            waterShopCart.setUserId(username);
            waterShopCart.setNumber(cartPo.getNumber());
            cartService.save(waterShopCart);
        } else {
            // 如果已经存在购物车中，则数量增加
            int total = Integer.parseInt(cartPo.getNumber()) + Integer.parseInt(one.getNumber());
            one.setNumber(String.valueOf(total));
            cartService.updateById(one);
        }
        return ThinkResult.ok("添加成功");
    }

    /**
     * 检查购物车是否能够购买
     */
    public ThinkResult checkCart(CartPo cartPo) {
        WaterShop good = shopService.getById(cartPo.getGoodsId());
        WaterShopItem product = itemService.getById(cartPo.getProductId());
        if (good == null || good.getStatus().equals(DictEnum.Disable.getValue())
                || product == null || product.getStatus().equals(DictEnum.Disable.getValue())) {
            return ThinkResult.error("商品已下架");
        }
        int i = Integer.parseInt(product.getReserve());
        if (i <= 0 || i < Integer.parseInt(cartPo.getNumber())) {
            return ThinkResult.error("库存都不够啦");
        }
        return ThinkResult.ok(null);
    }

    /**
     * 商品页面获得购物车信息
     */
    @RequestMapping("getCart")
    public ThinkResult getCart(HttpServletRequest request) {
        // TODO: 2023/6/27
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.notLogin();
        }
        LambdaQueryWrapper<WaterShopCart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(WaterShopCart::getUserId, username);
        List<WaterShopCart> list = cartService.list();
        ArrayList<Object> resultList = new ArrayList<>(list.size());
        // 获取购物车统计信息
        int goodsCount = 0;
        int goodsAmount = 0;
//        let checkedGoodsCount = 0;
//        let checkedGoodsAmount = 0;
        int numberChange = 0;
        for (WaterShopCart waterShopCart : list) {
            WaterShopItem item = itemService.getById(waterShopCart.getShopId());
//            商品不存在
            if (Objects.equals(item.getStatus(), DictEnum.Disable.getValue()) || Objects.equals(item.getIsDelete(), "1")) {
                cartService.removeById(waterShopCart.getId());
            }
            CartVo cartVo = new CartVo();
            BeanUtils.copyProperties(item, cartVo);
//            数量不能多于库存
            cartVo.setNumber(Math.min(Integer.parseInt(item.getReserve()), Integer.parseInt(waterShopCart.getNumber())));
            if (cartVo.getNumber() != Integer.parseInt(waterShopCart.getNumber())) {
//                超过库存
                numberChange = 1;
            }
            goodsCount += cartVo.getNumber();
            goodsAmount += cartVo.getNumber() * Integer.parseInt(cartVo.getRetail());
            resultList.add(cartVo);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cartList", resultList);
        jsonObject.put("goodAmount", goodsAmount);
        jsonObject.put("goodsCount", goodsCount);
        jsonObject.put("checkedGoodsCount", goodsAmount);
        jsonObject.put("checkedGoodsAmount", goodsCount);
        jsonObject.put("numberChange", numberChange);
        return ThinkResult.ok(jsonObject);
    }
}
