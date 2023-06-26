package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.water.entity.WaterShopCart;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.jeecg.modules.demo.water.vo.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/app/cart")
public class CartController {
    @Autowired
    private IWaterShopCartService cartService;

    /**
     * 获取购物车数量
     */
    @RequestMapping("count")
    public ThinkResult getCount() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null) {
            return ThinkResult.error("未登录");
        }
        LambdaQueryWrapper<WaterShopCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterShopCart::getUserId, sysUser.getUsername());
        long count = cartService.count(wrapper);
        return ThinkResult.ok(count);
    }
}
