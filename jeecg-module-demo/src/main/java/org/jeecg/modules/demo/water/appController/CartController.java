package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterShopCart;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController

@RequestMapping("/app/cart")
public class CartController {
    @Autowired
    private IWaterShopCartService cartService;

    /**
     * 获取购物车数量
     */
    @RequestMapping("count")
    public ThinkResult getCount(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username == null) {
            return ThinkResult.notLogin();
        }
        LambdaQueryWrapper<WaterShopCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterShopCart::getUserId, username);
        long count = cartService.count(wrapper);
        return ThinkResult.ok(count);
    }
}
