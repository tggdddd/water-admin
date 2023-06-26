package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.service.IWaterClassService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.jeecg.modules.demo.water.vo.ThinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController("/classify")
public class ClassController {
    @Autowired
    private IWaterClassService waterClassService;
    @Autowired
    private IWaterShopService shopService;

    /**
     * 分页列表查询商品数据
     */
    @GetMapping(value = "/list")
    public ThinkResult queryPageList(@RequestParam(name = "id", defaultValue = "0") String classId,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        MPJLambdaQueryWrapper<WaterShop> shopLambdaQueryWrapper = new MPJLambdaQueryWrapper<WaterShop>();
        // 使用 eq 防止模糊查询
        if (Objects.equals(classId, "0")) {
            shopLambdaQueryWrapper.eq(WaterShop::getTypeId, classId);
        }
        shopLambdaQueryWrapper.eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                .leftJoin("select retail from water_shop_item order by retail asc limit 1");
        Page<ShopVo> page = new Page<>(pageNo, pageSize);
        IPage<ShopVo> pageList = shopService.selectJoinListPage(page, ShopVo.class, shopLambdaQueryWrapper);
        return ThinkResult.ok(pageList);
    }

}
