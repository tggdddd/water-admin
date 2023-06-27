package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.entity.WaterShopModel;
import org.jeecg.modules.demo.water.po.CartPo;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.service.IWaterShopModelService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<WaterShopModel> waterShopModels = modelService.selectByMainId(id);
        result.put("info", shop);
        result.put("count", countItems);
        result.put("products", canUse);
        result.put("model", waterShopModels);
        return ThinkResult.ok(result);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("add")
    public ThinkResult addToCart(@RequestBody CartPo cartPo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            return ThinkResult.error("未登录");
        }
        WaterShop good = shopService.getById(cartPo.getGoodsId());
        if (good == null || good.getStatus().equals(DictEnum.Disable.getValue())) {
            return ThinkResult.error("商品已下架");
        }
//        const currentTime = parseInt(new Date().getTime() / 1000);
//
//        // 取得规格的信息,判断规格库存
//        // const productInfo = await this.model('product').where({goods_id: goodsId, id: productId}).find();
//        const productInfo = await this.model('product').where({
//                id: productId
//        }).find();
//        // let productId = productInfo.id;
//        if (think.isEmpty(productInfo) || productInfo.goods_number < number) {
//            return this.fail(400, '库存不足');
//        }
//        // 判断购物车中是否存在此规格商品
//        const cartInfo = await this.model('cart').where({
//                user_id: userId,
//                product_id: productId,
//                is_delete: 0
//        }).find();
//        let retail_price = productInfo.retail_price;
//        if (addType == 1) {
//            await this.model('cart').where({
//                    is_delete: 0,
//                    user_id: userId
//            }).update({
//                    checked: 0
//            });
//            let goodsSepcifitionValue = [];
//            if (!think.isEmpty(productInfo.goods_specification_ids)) {
//                goodsSepcifitionValue = await this.model('goods_specification').where({
//                        goods_id: productInfo.goods_id,
//                        is_delete: 0,
//                        id: {
//                    'in': productInfo.goods_specification_ids.split('_')
//                }
//                }).getField('value');
//            }
//            // 添加到购物车
//            const cartData = {
//                    goods_id: productInfo.goods_id,
//                    product_id: productId,
//                    goods_sn: productInfo.goods_sn,
//                    goods_name: goodsInfo.name,
//                    goods_aka: productInfo.goods_name,
//                    goods_weight: productInfo.goods_weight,
//                    freight_template_id: goodsInfo.freight_template_id,
//                    list_pic_url: goodsInfo.list_pic_url,
//                    number: number,
//                    user_id: userId,
//                    retail_price: retail_price,
//                    add_price: retail_price,
//                    goods_specifition_name_value: goodsSepcifitionValue.join(';'),
//                    goods_specifition_ids: productInfo.goods_specification_ids,
//                    checked: 1,
//                    add_time: currentTime,
//                    is_fast: 1
//            };
//            await this.model('cart').add(cartData);
//            return this.success(await this.getCart(1));
//        } else {
//            if (think.isEmpty(cartInfo)) {
//                // 添加操作
//                // 添加规格名和值
//                let goodsSepcifitionValue = [];
//                if (!think.isEmpty(productInfo.goods_specification_ids)) {
//                    goodsSepcifitionValue = await this.model('goods_specification').where({
//                            goods_id: productInfo.goods_id,
//                            is_delete: 0,
//                            id: {
//                        'in': productInfo.goods_specification_ids.split('_')
//                    }
//                    }).getField('value');
//                }
//                // 添加到购物车
//                const cartData = {
//                        goods_id: productInfo.goods_id,
//                        product_id: productId,
//                        goods_sn: productInfo.goods_sn,
//                        goods_name: goodsInfo.name,
//                        goods_aka: productInfo.goods_name,
//                        goods_weight: productInfo.goods_weight,
//                        freight_template_id: goodsInfo.freight_template_id,
//                        list_pic_url: goodsInfo.list_pic_url,
//                        number: number,
//                        user_id: userId,
//                        retail_price: retail_price,
//                        add_price: retail_price,
//                        goods_specifition_name_value: goodsSepcifitionValue.join(';'),
//                        goods_specifition_ids: productInfo.goods_specification_ids,
//                        checked: 1,
//                        add_time: currentTime
//                };
//                await this.model('cart').add(cartData);
//            } else {
//                // 如果已经存在购物车中，则数量增加
//                if (productInfo.goods_number < (number + cartInfo.number)) {
//                    return this.fail(400, '库存都不够啦');
//                }
//                await this.model('cart').where({
//                        user_id: userId,
//                        product_id: productId,
//                        is_delete: 0,
//                        id: cartInfo.id
//                }).update({
//                        retail_price: retail_price
//                });
//                await this.model('cart').where({
//                        user_id: userId,
//                        product_id: productId,
//                        is_delete: 0,
//                        id: cartInfo.id
//                }).increment('number', number);
//            }
//            return this.success(await this.getCart(0));
        return null;
    }
}
