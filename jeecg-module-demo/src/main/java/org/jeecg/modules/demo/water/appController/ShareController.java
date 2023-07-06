package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.RestUtil;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.base.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/share")
public class ShareController {
    @Autowired
    ThirdAppConfig thirdAppConfig;

    @RequestMapping("/get")
    public ThinkResult share(@RequestParam("shopId") String ShopId) {
        String page = "pages/goods/goods";
        String sceneData = ShopId; //抽奖id ？？？？
        String url = "https://api.weixin.qq.com/cgi-bin/token" +
                "?grant_type=client_credential" +
                "&secret=" + thirdAppConfig.getWechatSmall().getClientSecret() +
                "&appid=" + thirdAppConfig.getWechatSmall().getClientId();

        JSONObject post = RestUtil.get(url);
        String token = post.getString("access_token");
        JSONObject params = new JSONObject();
        params.put("scene", sceneData);
        params.put("page", page);
        params.put("width", 200);
        JSONObject result = RestUtil.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token, params);
        if (result.get("errcode").equals("0")) {
            Object o = result.get("buffer");
            return ThinkResult.ok(o);
        }
        return ThinkResult.error();
    }
}
