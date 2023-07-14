package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.RestUtil;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.demo.water.bo.SysUser;
import org.jeecg.modules.demo.water.constant.PromoteConstant;
import org.jeecg.modules.demo.water.entity.*;
import org.jeecg.modules.demo.water.service.IWaterPromoteActivityService;
import org.jeecg.modules.demo.water.service.IWaterPromoteWinningService;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/app/share")
public class ShareController {
    @Autowired
    ThirdAppConfig thirdAppConfig;
    private static final String SCENE_KEY = "wechat_scene_";
    @Autowired
    IWaterShopService shopService;
    @Autowired
    IWaterPromoteActivityService promoteActivityService;
    @Autowired
    IWaterPromoteWinningService promoteWinningService;
    @Autowired
    IWaterShopItemService itemService;
    @Autowired
    CommonAPI commonAPI;
    @Autowired
    RedisUtil redisUtil;
    @Value(value = "${jeecg.uploadType}")
    private String uploadType;

    /**
     * 领取奖励
     */
    @RequestMapping("gotGift")
    public WaterOrder getReward(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        WaterPromoteWinning record = promoteWinningService.getOne(new MPJLambdaWrapper<WaterPromoteWinning>()
                .eq(WaterPromoteWinning::getUserId, username)
                .eq(WaterPromoteWinning::getStatus, PromoteConstant.SUCCESS)
                .eq(WaterPromoteWinning::getId, id));
        if (record == null) {
            response.setStatus(500);
            return null;
        }
        WaterOrder waterOrder = promoteWinningService.generateRewardOrder(record, username);
        if (waterOrder == null) {
            response.setStatus(500);
            return null;
        }
        return waterOrder;
    }

    /**
     * 获取推广记录
     */
    @RequestMapping("myPromote")
    public List<JSONObject> getPromoteData(@RequestParam("id") String id, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        List<JSONObject> list = promoteWinningService.selectJoinList(JSONObject.class, new MPJLambdaWrapper<WaterPromoteWinning>()
                .selectAs(WaterPromoteWinning::getStatus, "status")
                .selectAs(SysUser::getAvatar, "avatar")
                .selectAs(SysUser::getRealname, "name")
                .selectAs(WaterPromoteWinning::getId, "id")
                .eq(WaterPromoteWinning::getUserId, username)
                .eq(WaterPromoteWinning::getActivityId, id)
                .leftJoin(SysUser.class, SysUser::getUsername, WaterPromoteWinning::getRegisterUserId));
        for (JSONObject jsonObject : list) {
            String s = commonAPI.translateDict("promote_status", jsonObject.getString("status"));
            jsonObject.put("statusText", s);
        }
        return list;
    }

    private String putScene(String username, String id) {
        JSONObject pa = new JSONObject();
        pa.put("username", username);
        pa.put("activeId", id);
        String jsonString = pa.toJSONString();
        int i = jsonString.hashCode();
        redisUtil.set(SCENE_KEY + i, jsonString);
        return String.valueOf(i);
    }

    private JSONObject getScene(String key) {
        return JSON.parseObject((String) redisUtil.get(SCENE_KEY + key));
    }

    /**
     * 推广分享
     */
    @RequestMapping("promote")
    public JSONObject shareToNew(HttpServletRequest request, @Param("id") String id) {
        WaterPromoteActivity activity = promoteActivityService.getById(id);
        WaterShopItem shopItem = itemService.getById(activity.getShopItemId());
        JSONObject result = new JSONObject();
        result.put("shopName", shopItem.getName());
        result.put("shopImage", shopItem.getImageString());
        result.put("shopId", shopItem.getId());
        result.put("shopCut", activity.getRelief());
        String username;
        try {
            username = JwtUtil.getUserNameByToken(request);
        } catch (Exception e) {
            username = "";
        }
        String token = getAccessToken();
        JSONObject params = new JSONObject();
        String s = putScene(username, id);
        params.put("scene", s);
        params.put("width", 280);
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(params.toJSONString().length()));
        byte[] shareImageData = RestUtil.request(url, HttpMethod.POST, headers, null, params, byte[].class).getBody();
        result.put("shareImageData", shareImageData);
        result.put("shareCode", s);
        return result;
    }

    /**
     * 商品链接分享
     */
    @RequestMapping("get")
    public byte[] share(@RequestParam("shopId") String ShopId) {
        String page = "pages/goods/goods";
        String token = getAccessToken();
        JSONObject params = new JSONObject();
        params.put("scene", ShopId);
        params.put("page", page);
        params.put("width", 200);
        params.put("check_path", "false");
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(params.toJSONString().length()));
        byte[] body = RestUtil.request(url, HttpMethod.POST, headers, null, params, byte[].class).getBody();
        return Base64.getMimeEncoder().encode(body);
    }

    /**
     * 分享商品的信息
     */
    @RequestMapping("goodShareInfo")
    public ShopVo goodDetail(@RequestParam("id") String shopId) {
        MPJLambdaWrapper<WaterShop> wrapper = new MPJLambdaWrapper<WaterShop>();
        wrapper.selectAll(WaterShop.class)
                .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId)
                .selectCount(WaterShopItem::getReserve)
                .selectMin(WaterShopItem::getRetail)
                .eq(WaterShop::getId, shopId);
        return shopService.selectJoinOne(ShopVo.class, wrapper);
    }

    /**
     * 获取token
     */
    private String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token" +
                "?grant_type=client_credential" +
                "&secret=" + thirdAppConfig.getWechatSmall().getClientSecret() +
                "&appid=" + thirdAppConfig.getWechatSmall().getClientId();
        JSONObject post = RestUtil.get(url);
        return post.getString("access_token");
    }
}
