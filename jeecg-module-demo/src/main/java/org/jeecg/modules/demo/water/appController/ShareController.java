package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.RestUtil;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.base.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/app/share")
public class ShareController {
    @Autowired
    ThirdAppConfig thirdAppConfig;

    @RequestMapping("/get")
    public ThinkResult share(@RequestParam("shopId") String ShopId) {
        String page = "pages/goods/goods";
        String sceneData = ShopId; //抽奖id ？？？？
        JSONObject json = new JSONObject();
        json.put("grant_type", "client_credential");
        json.put("secret", thirdAppConfig.getWechatSmall().getClientSecret());
        json.put("appid", thirdAppConfig.getWechatSmall().getClientId());
        JSONObject post = RestUtil.post("https://api.weixin.qq.com/cgi-bin/token", json);
        String token = post.getString("access_token");
        JSONObject params = new JSONObject();
        json.put("scene", sceneData);
        json.put("page", page);
        json.put("width", 200);
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(json.toJSONString().length()));
        JSONObject result = RestUtil.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token, json);
        if (result.get("errcode").equals("0")) {
            Object o = result.get("buffer");
            return ThinkResult.ok(o);
        }
        return ThinkResult.error();
    }
}
