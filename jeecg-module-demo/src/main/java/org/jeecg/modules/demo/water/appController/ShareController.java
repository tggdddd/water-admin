package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RestUtil;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
@RequestMapping("/app/share")
public class ShareController {
    @Autowired
    ThirdAppConfig thirdAppConfig;

    /**
     * 推广分享
     */
    @RequestMapping("/promote")
    public byte[] shareToNew(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        String sceneData = "username=" + username;
        String token = getAccessToken();
        JSONObject params = new JSONObject();
        params.put("scene", sceneData);
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(params.toJSONString().length()));
        return RestUtil.request(url, HttpMethod.POST, headers, null, params, byte[].class).getBody();
    }

    /**
     * 商品链接分享
     */
    @RequestMapping("/get")
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
