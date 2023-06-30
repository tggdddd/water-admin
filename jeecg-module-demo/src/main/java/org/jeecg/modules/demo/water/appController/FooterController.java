package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.service.IWaterFooterService;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app/footprint")
public class FooterController {
    @Autowired
    IWaterFooterService footerService;

    @RequestMapping("page")
    public ThinkResult getPage(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo, HttpServletRequest request) {
        String userNameByToken = JwtUtil.getUserNameByToken(request);
        Page<ShopVo> shopVoPage = footerService.pageByUsername(userNameByToken, new Page<>(pageNo, pageSize));
        return ThinkResult.ok(shopVoPage);
    }

    @RequestMapping("delete")
    public ThinkResult delele(@RequestParam("shopId") String shopId, HttpServletRequest request) {
        footerService.deleteByShopId(JwtUtil.getUserNameByToken(request), shopId);
        return ThinkResult.ok();
    }
}
