package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterSetting;
import org.jeecg.modules.demo.water.service.IWaterSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app/settings")
public class SettingController {
    @Autowired
    IWaterSettingService settingService;
    @Autowired
    CommonAPI commonAPI;

    @RequestMapping("showSettings")
    public ThinkResult showSetting() {
        WaterSetting waterSetting = settingService.list().get(0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ad", waterSetting.getAd().equals("0"));
        jsonObject.put("notice", waterSetting.getNotice().equals("0"));
        return ThinkResult.ok(jsonObject);
    }

    @RequestMapping("userDetail")
    public ThinkResult getUserInfo(HttpServletRequest request) {
        LoginUser userByName = commonAPI.getUserByName(JwtUtil.getUserNameByToken(request));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userByName.getUsername());
        jsonObject.put("telephone", userByName.getTelephone());
        jsonObject.put("avatar", userByName.getAvatar());
        jsonObject.put("phone", userByName.getPhone());
        jsonObject.put("sex", userByName.getSex());
        jsonObject.put("post", userByName.getPost());
        jsonObject.put("birthDay", userByName.getBirthday());
        jsonObject.put("nickName", userByName.getRealname());
        return ThinkResult.ok(jsonObject);
    }
}
