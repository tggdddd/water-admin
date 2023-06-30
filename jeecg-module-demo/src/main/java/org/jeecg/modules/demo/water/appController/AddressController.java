package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterAddress;
import org.jeecg.modules.demo.water.po.WeChatAddress;
import org.jeecg.modules.demo.water.service.IWaterAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/app/address")
public class AddressController {
    @Autowired
    IWaterAddressService addressService;
    @Autowired
    ProvinceCityArea cityArea;

    @GetMapping("detail")
    public ThinkResult getDetail(@RequestParam("id") String addressId) {
        WaterAddress byId = addressService.getById(addressId);
        transformCodeToText(byId);
        return ThinkResult.ok(byId);
    }

    private void transformCodeToText(List<WaterAddress> list) {
        for (WaterAddress waterAddress : list) {
            transformCodeToText(waterAddress);
        }
    }

    private void transformCodeToText(WaterAddress waterAddress) {
        String area = waterAddress.getArea();
        if (area != null) {
            String[] split = area.split(",");
            waterAddress.setArea(cityArea.getText(split[split.length - 1]));
        }
    }

    @PostMapping("save")
    public ThinkResult save(@RequestBody WeChatAddress address, HttpServletRequest request) {
        String userName = JwtUtil.getUserNameByToken(request);
        String[] codes = cityArea.getCode(address.getProvinceName(), address.getCityName(), address.getCountyName());
        String telNumber = address.getTelNumber();
        WaterAddress build = new WaterAddress();
        build.setName(address.getUserName());
        build.setArea(StringUtils.join(codes, ","));
        build.setAddress(address.getDetailInfo());
        build.setPhone(telNumber);
        build.setUserId(userName);
        addressService.save(build);
        return ThinkResult.ok(null);
    }

    @DeleteMapping("delete")
    public ThinkResult delete(HttpServletRequest request, @RequestParam("id") String addressId) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.error("未登录");
        }
        boolean i = addressService.remove(new LambdaQueryWrapper<WaterAddress>().eq(WaterAddress::getUserId, username)
                .eq(WaterAddress::getId, addressId));
        return i ? ThinkResult.ok(null) : ThinkResult.error("删除失败！");
    }

    @RequestMapping("list")
    public ThinkResult list(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.notLogin();
        }
        List<WaterAddress> list = addressService.list(new LambdaQueryWrapper<WaterAddress>().eq(WaterAddress::getUserId, username));
        transformCodeToText(list);
        return ThinkResult.ok(list);
    }
}
