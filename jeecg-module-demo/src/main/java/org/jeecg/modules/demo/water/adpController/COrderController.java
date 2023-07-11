package org.jeecg.modules.demo.water.adpController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.water.entity.WaterAd;
import org.jeecg.modules.demo.water.entity.WaterPaidImage;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.po.CreateOrderBySendPO;
import org.jeecg.modules.demo.water.service.IWaterAdService;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWaterPaidImageService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.OrderSendItemVO;
import org.jeecg.modules.demo.water.vo.SaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/order")
public class COrderController {
    @Autowired
    IWaterOrderService orderService;
    @Autowired
    CommonAPI commonAPI;
    @Autowired
    IWaterAdService adService;
    @Autowired
    IWaterPaidImageService paidImageService;
    @Autowired
    ProvinceCityArea cityArea;
    @Autowired
    IWaterShopItemService itemService;

    /**
     * 创建派送单 （包括购买订单）
     */
    @PostMapping("createOrder")
    public Result<String> createOrder(@RequestBody CreateOrderBySendPO params, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        LoginUser user = commonAPI.getUserByName(username);
        if (user.getUserIdentity() != 2) {
            return Result.error("越权操作");
        }
        String orderWithOutPaid = orderService.createOrderWithOutPaid(username, params);
        return orderWithOutPaid == null ? Result.ok() : Result.error(orderWithOutPaid);
    }


    /**
     * 商品列表
     */
    @RequestMapping("shopItems/list")
    public List<WaterShopItem> getShopItemsList() {
        return itemService.list(new LambdaQueryWrapper<WaterShopItem>()
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue()));
    }

    /**
     * 收款二维码
     */
    @RequestMapping("code/list")
    public List<WaterPaidImage> gotMoneyCodeImage() {
        return paidImageService.list();
    }

    /**
     * 翻译字典
     */
    @RequestMapping("translateCode")
    public String translateSysCode(@RequestParam("code") String sysCode) {
        List<DictModel> dictModels = commonAPI.queryTableDictItemsByCode("sys_depart", "depart_name", "org_code");
        for (DictModel dictModel : dictModels) {
            if (dictModel.getValue().equals(sysCode)) {
                return dictModel.getText();
            }
        }
        return "未知";
    }

    @RequestMapping("translateSendStatus")
    public String translateSysSendStatus(@RequestParam("code") String sysCode) {
        List<DictModel> dictModels = commonAPI.queryDictItemsByCode("send_type");
        for (DictModel dictModel : dictModels) {
            if (dictModel.getValue().equals(sysCode)) {
                return dictModel.getText();
            }
        }
        return "未知";
    }

    /**
     * 首页轮播图与权限
     */
    @RequestMapping("index/init")
    public JSONObject init(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        JSONObject result = new JSONObject();
        List<WaterAd> waterAds = adService.sendCarousel();
        LoginUser userByName = commonAPI.getUserByName(username);
        result.put("images", waterAds);
        //        是上级
        result.put("isSuper", userByName.getUserIdentity() != null && userByName.getUserIdentity() == 2);
        return result;
    }

    @RequestMapping("isSuper")
    public boolean isSuper(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        LoginUser userByName = commonAPI.getUserByName(username);
        return userByName.getUserIdentity() != null && userByName.getUserIdentity() == 2;
    }

    /**
     * 每日销量
     */
    @RequestMapping("sale/list")
    public Page calculateSales(@RequestParam(value = "size", defaultValue = "10") Integer size,
                               @RequestParam(value = "current", defaultValue = "1") Integer current, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.calculateSale(new Page<SaleVO>(current, size), username);
    }

    /**
     * 每日销量详细订单
     */
    @RequestMapping("sale/detail/list")
    public Page calculateSalesDeatil(@RequestParam("time") String time, @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestParam(value = "current", defaultValue = "1") Integer current) {
        return orderService.calculateSaleDetail(time, new Page<>(current, size));
    }

    /**
     * 骑手领取订单
     */
    @RequestMapping("pick/up")
    public void pickUpOrder(@RequestParam(value = "orderId") String orderId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.pickUpOrder(orderId, username), response);
    }

    /**
     * 骑手取消订单
     */
    @RequestMapping("cancel")
    public void cancelOrder(@RequestParam("sendId") String sendId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.cancelOrder(sendId, username), response);
    }

    /**
     * 骑手完成订单
     */
    @RequestMapping("finish")
    public void finishOrder(@RequestParam(value = "sendId") String sendId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.finishOrder(sendId, username), response);
    }

    private void setResponseStatus(boolean is200, HttpServletResponse response) {
        if (!is200) {
            response.setStatus(500);
        }
    }

    /**
     * 骑手可接订单
     */
    @RequestMapping("pick/list")
    public Page<OrderSendItemVO> listReceiveOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @RequestParam(value = "address", required = false) String address,
                                                   @RequestParam(value = "name", required = false) String receiveName,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pagePaidOrderItem(new Page<>(current, size), username, address, receiveName, phone);
    }

    /**
     * 骑手已接订单
     */
    @RequestMapping("receive/list")
    public Page<OrderSendItemVO> listGotToItOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @RequestParam(value = "address", required = false) String address,
                                                   @RequestParam(value = "name", required = false) String receiveName,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnOrderItemWithOutSend(new Page<>(current, size), username, address, receiveName, phone);
    }

    /**
     * 骑手已送订单
     */
    @RequestMapping("finish/list")
    public Page<OrderSendItemVO> listFinishOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                  @RequestParam(value = "address", required = false) String address,
                                                  @RequestParam(value = "name", required = false) String receiveName,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnSendOrderItem(new Page<>(current, size), username, address, receiveName, phone);
    }

    /**
     * 骑手所有订单
     */
    @RequestMapping("own/list")
    public Page<OrderSendItemVO> listOwnOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "address", required = false) String address,
                                               @RequestParam(value = "name", required = false) String receiveName,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnOrderAndItem(new Page<>(current, size), username,address,receiveName,phone);
    }
}
