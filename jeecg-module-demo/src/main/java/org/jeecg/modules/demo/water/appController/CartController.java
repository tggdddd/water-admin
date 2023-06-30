package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterShopCart;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.vo.CartVo;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/app/cart")
public class CartController {
    @Autowired
    private IWaterShopCartService cartService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    CommonAPI commonAPI;
    @Autowired
    private IWaterShopItemService itemService;

    /**
     * 获取购物车数量
     */
    @RequestMapping("count")
    public ThinkResult getCount(HttpServletRequest request) {
        try {
            String username = JwtUtil.getUserNameByToken(request);
            long count = cartService.getCardCount(username);
            return ThinkResult.ok(count);
        } catch (Exception e) {
            return ThinkResult.ok(0);
        }
    }

    /**
     * 获得购物车信息
     */
    @RequestMapping("index")
    public ThinkResult getCart(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.notLogin();
        }
//        获取购物车
        LambdaQueryWrapper<WaterShopCart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(WaterShopCart::getUserId, username);
        List<WaterShopCart> list = cartService.list();
        ArrayList<Object> resultList = new ArrayList<>(list.size());
        // 获取购物车统计信息
        int goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0);
        int numberChange = 0;
        for (WaterShopCart waterShopCart : list) {
            WaterShopItem item = itemService.getById(waterShopCart.getShopId());
            CartVo cartVo = new CartVo();
            BeanUtils.copyProperties(item, cartVo);
//            商品数量
            cartVo.setNumber(Integer.valueOf(waterShopCart.getNumber()));
            //   商品不存在
            if (Objects.equals(item.getStatus(), DictEnum.Disable.getValue()) || Objects.equals(item.getIsDelete(), "1")) {
                cartVo.setRemove(true);
                continue;
            }
//            数量不能多于库存
            cartVo.setNumber(Math.min(Integer.parseInt(item.getReserve()), Integer.parseInt(waterShopCart.getNumber())));
            if (item.getStatus().equals(DictEnum.Enable.getValue()) && cartVo.getNumber() != Integer.parseInt(waterShopCart.getNumber())) {
//                超过库存
                numberChange = 1;
                waterShopCart.setNumber(String.valueOf(cartVo.getNumber()));
                cartService.updateById(waterShopCart);
            }
            goodsCount += cartVo.getNumber();
            goodsAmount = goodsAmount.add(new BigDecimal(cartVo.getNumber()).multiply(new BigDecimal(cartVo.getRetail())));
            //增加模型的字典翻译
            cartVo.setModelName(getTableDictText("water_shop_model,model,id", cartVo.getModel()));
            JSONObject json = (JSONObject) JSONObject.toJSON(cartVo);
            json.put("imageString", item.getImageString());
            resultList.add(json);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cartList", resultList);
        jsonObject.put("goodAmount", goodsAmount);
        jsonObject.put("goodsCount", goodsCount);
        jsonObject.put("numberChange", numberChange);
        return ThinkResult.ok(jsonObject);
    }

    private String getTableDictText(String code, String value) {
        //            字典翻译状态
        String keyString = String.format("sys:cache:dictTable::SimpleKey [%s,%s]", code, value);
        if (redisTemplate.hasKey(keyString)) {
            return oConvertUtils.getString(redisTemplate.opsForValue().get(keyString));
        } else {
            String[] split = code.split(",");
            List<DictModel> dictModels = commonAPI.translateDictFromTableByKeys(split[0], split[1], split[2], value);
            String text = "";
            if (dictModels.size() != 0) {
                text = dictModels.get(0).getText();
            }
            redisTemplate.opsForValue().set(keyString, text, 300, TimeUnit.SECONDS);
            return text;
        }
    }

    /**
     * 根据itemId 修改购物车商品数量
     */
    @RequestMapping("updateByShopId")
    public ThinkResult updateShopCartItemsById(@RequestParam("id") String shopId, @RequestParam("number") Integer number, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<WaterShopCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterShopCart::getUserId, username).eq(WaterShopCart::getShopId, shopId);
        WaterShopCart one = cartService.getOne(wrapper);
        one.setNumber(String.valueOf(number));
        cartService.updateById(one);
        return ThinkResult.ok("");
    }

    /**
     * 根据itemId 删除购物车商品
     */
    @RequestMapping("deleteByShopId")
    public ThinkResult deltehopCartItemByItId(@RequestParam("productId") String shopId, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        LambdaQueryWrapper<WaterShopCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterShopCart::getUserId, username).eq(WaterShopCart::getShopId, shopId);
        cartService.remove(wrapper);
        return ThinkResult.ok("");
    }

    /**
     * 购物车结算
     *
     * @param ids 商品item 的id    ， 分隔
     *            标识 购物车的支付商品
     */

    @RequestMapping("order")
    public ThinkResult generateOrder(@RequestParam("ids") String ids, HttpServletRequest request) {
        String userNameByToken = JwtUtil.getUserNameByToken(request);
        String[] split = ids.split(",");
//        获取用户的购物车
        LambdaQueryWrapper<WaterShopCart> carWrapper = new LambdaQueryWrapper<>(WaterShopCart.class)
                .in(WaterShopCart::getShopId, Arrays.asList(split))
                .eq(WaterShopCart::getUserId, userNameByToken);
        List<WaterShopCart> list = cartService.list(carWrapper);
//        创建订单
        WaterOrder waterOrder = cartService.payOrUpdate(list, userNameByToken);
        if (waterOrder == null) {
            return ThinkResult.error("商品库存不足，已调整购物车数量");
        }
        return ThinkResult.ok(waterOrder);
    }
}
