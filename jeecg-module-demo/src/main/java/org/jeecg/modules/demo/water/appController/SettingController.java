package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterSetting;
import org.jeecg.modules.demo.water.service.IWaterSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/settings")
public class SettingController {
    @Autowired
    IWaterSettingService settingService;

    @RequestMapping("showSettings")
    public ThinkResult showSetting() {
        WaterSetting waterSetting = settingService.list().get(0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ad", waterSetting.getAd().equals("0"));
        jsonObject.put("notice", waterSetting.getNotice().equals("0"));
        return ThinkResult.ok(jsonObject);
    }
}
