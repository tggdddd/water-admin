package org.jeecg.modules.demo.water.appController;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.constant.SendOrderConstant;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.entity.WaterSend;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.entity.WaterShopModel;
import org.jeecg.modules.demo.water.mapper.WaterOrderMapper;
import org.jeecg.modules.demo.water.po.SubmitOrderParamsPO;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWaterSendService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/app/order")
public class OrderController {

    @Autowired
    IWaterOrderService orderService;
    @Autowired
    WaterOrderMapper orderMapper;
    @Autowired
    IWaterShopItemService itemService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    CommonAPI commonAPI;
    @Autowired
    IWaterSendService sendService;

    /**
     * 确认收货
     */
    @RequestMapping("confirm")
    public ThinkResult confirmReceiveOrder(@RequestParam("orderId") String orderId) {
        boolean b = orderService.confirmReceipt(orderId);
        return b ? ThinkResult.ok() : ThinkResult.error();
    }

    /**
     * 支付结果手动查询
     */
    @GetMapping("checkoutPayStatus")
    public ThinkResult checkPay(@RequestParam("orderId") String orderId) {
//        更新订单状态
        boolean b = orderService.updateOrderStatusPaid(orderId);
        return b ? ThinkResult.ok() : ThinkResult.error();
    }

    /**
     * 提交订单
     */
    @PostMapping("submit")
    public ThinkResult submitOrder(@Validated @RequestBody SubmitOrderParamsPO params, HttpServletRequest request) {
//       确认订单
        boolean b = orderService.submitOrder(params, JwtUtil.getUserNameByToken(request));
        if (!b) {
            return ThinkResult.error("订单无法确认");
        }
        return ThinkResult.ok();
    }
    /**
     * 微信支付订单生成
     */
    @RequestMapping("pay")
    public ThinkResult payOrder(@RequestParam("orderId") String orderId) {
        WaterOrder order = orderService.getById(orderId);
        if (order.getPrices().equals("0")) {
            if (Objects.equals(order.getOrdreStatus(), OrderConstant.UNPAID)) {
                order.setOrdreStatus(OrderConstant.WAITING_SEND);
                orderMapper.updateById(order);
//                创建派送单
                orderService.generateSendOrder(orderId, order.getLocatonType());
            }
            return ThinkResult.error("无需付款");
        }
        PrepayWithRequestPaymentResponse prepayWithRequestPaymentResponse = orderService.generateWeChatOrder(orderId);
        if (prepayWithRequestPaymentResponse != null) {
            return ThinkResult.ok(prepayWithRequestPaymentResponse);
        }
        return ThinkResult.error("生成支付订单失败");

    }

    /**
     * 根据订单再次购买
     */
    @RequestMapping("reOrderAgain")
    public ThinkResult reOrder(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        WaterOrder waterOrder = orderService.reOrderAgain(orderId, username);
        if (waterOrder == null) {
            return ThinkResult.error("商品库存不足");
        }
        return ThinkResult.ok(waterOrder);
    }

    /**
     * 取消订单
     */
    @RequestMapping("cancel")
    public ThinkResult cancel(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        return orderService.cancelOrderByUsernameAndId(orderId, JwtUtil.getUserNameByToken(request)) ?
                ThinkResult.ok() : ThinkResult.error();
    }

    /**
     * 删除订单
     */
    @RequestMapping("delete")
    public ThinkResult delete(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        return orderService.deleteOrderByUsernameAndId(orderId, JwtUtil.getUserNameByToken(request)) == 1 ?
                ThinkResult.ok() : ThinkResult.error();
    }

    @GetMapping("checkout")
    public ThinkResult checkOut(@RequestParam("orderId") String orderId) {
        return orderService.generateOrderView(orderId);
    }

    @GetMapping("shopList")
    public ThinkResult getShopList(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        return ThinkResult.ok(orderService.getOrderShopItems(orderId, JwtUtil.getUserNameByToken(request)));
    }

    @RequestMapping("detail")
    public ThinkResult orderDetail(@RequestParam("orderId") String orderId) {
        ThinkResult thinkResult = orderService.generateOrderViewToJSON(orderId);
        if (thinkResult.getErrno() != 0) {
            return thinkResult;
        }
        JSONObject jsonObject = (JSONObject) thinkResult.getData();
        jsonObject.put("orderStatus", getDictText("order_status", jsonObject.getJSONObject("waterOrder").getString("ordreStatus")));
        WaterSend one = sendService.getOne(new LambdaQueryWrapper<WaterSend>()
                .eq(WaterSend::getOrderId, orderId)
                .ne(WaterSend::getStatus, SendOrderConstant.CANCEL));
//        派送员信息
        if (one != null && one.getUserId() != null) {
            JSONObject sendInfo = new JSONObject();
//            姓名，电话
            LoginUser userByName = commonAPI.getUserByName(one.getUserId());
            sendInfo.put("name", userByName.getRealname());
            sendInfo.put("phone", userByName.getPhone());
            jsonObject.put("sendInfo", sendInfo);
        }
        jsonObject.put("sendOrder", one);
        return ThinkResult.ok(jsonObject);
    }

    @RequestMapping("list")
    public ThinkResult listOrders(@RequestParam(value = "showType") Integer showType,
                                  @RequestParam(value = "size", defaultValue = "8") Integer size,
                                  @RequestParam(value = "page", defaultValue = "1") Integer current,
                                  HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.error("未登录");
        }
        MPJLambdaWrapper<WaterOrder> wrapper = new MPJLambdaWrapper<WaterOrder>()
                .eq(WaterOrder::getUserId, username)
                .orderByDesc(WaterOrder::getCreateTime);
        Page<JSONObject> page = new Page<JSONObject>(current, size);
//        所有
        if (showType == 0) {
//            --------------------------------
        }
//        未付款
        else if (showType == 1) {
            wrapper.in(WaterOrder::getOrdreStatus, OrderConstant.UNCERTAIN, OrderConstant.UNPAID);
        }
//        待发货
        else if (showType == 2) {
            wrapper.eq(WaterOrder::getOrdreStatus, OrderConstant.WAITING_SEND);
        }
//        待收货
        else if (showType == 3) {
            wrapper.eq(WaterOrder::getOrdreStatus, OrderConstant.SEND);
        } else {
            return ThinkResult.error("标识错误：" + showType);
        }
        Page<JSONObject> page1 = orderService.selectJoinListPage(page, JSONObject.class, wrapper);
        List<JSONObject> records = page1.getRecords();
        List<String> images = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            JSONObject order = records.get(i);
            String[] split = order.getString("shop_item_id").split(",");
            List<String> list = Arrays.asList(split);
            List<JSONObject> items = itemService.selectJoinList(JSONObject.class, new MPJLambdaWrapper<WaterShopItem>()
                    .in(WaterShopItem::getId, list)
                    .selectAs(WaterShopItem::getName, "good")
                    .leftJoin(WaterShopModel.class, WaterShopModel::getId, WaterShopItem::getModel)
                    .selectAs(WaterShopModel::getModel, "model")
                    .selectAs(WaterShopItem::getImage, "image"));
            String s = items.stream().map(e -> e.getString("good") + "：" + e.getString("model"))
                    .reduce((a, b) -> a + " " + b).orElse(null);
            order.put("imagesString", items.stream().map(e -> new String(e.getBytes("image"), StandardCharsets.UTF_8)).reduce((a, b) -> a + "," + b).orElse(null));
            order.put("goods", s);
            String text = getDictText("order_status", order.getString("ordre_status"));
            order.put("orderStatus", text);

//            物流信息

            records.set(i, order);
        }
        return ThinkResult.ok(page1);
    }

    private String getDictText(String code, String value) {
        //            字典翻译状态
        String redisKey = String.format("sys:cache:dict::%s:%s", code, value);
        if (redisTemplate.hasKey(redisKey)) {
            return oConvertUtils.getString(redisTemplate.opsForValue().get(redisKey));
        } else {
            return commonAPI.translateDict(code, value);
        }
    }

    @RequestMapping("orderCount")
    public ThinkResult orderCount(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.error("未登录");
        }
        MPJLambdaWrapper<WaterOrder> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(WaterOrder::getUserId, username)
                .select(WaterOrder::getOrdreStatus);
        List<WaterOrder> list = orderService.list(wrapper);
        String[] array = list.stream().map(WaterOrder::getOrdreStatus).toArray(String[]::new);
        int total = array.length;
        int unpaid = 0;
        int waitingSend = 0;
        int receive = 0;
//        other
        for (String s : array) {
            switch (s) {
                case OrderConstant.UNCERTAIN:
                case OrderConstant.UNPAID:
                    unpaid++;
                    break;
                case OrderConstant.WAITING_SEND:
                    waitingSend++;
                    break;
                case OrderConstant.SEND:
                    receive++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("toPay", unpaid);
        jsonObject.put("toDelivery", waitingSend);
        jsonObject.put("toReceive", receive);
        jsonObject.put("total", total);
        return ThinkResult.ok(jsonObject);
    }
}
