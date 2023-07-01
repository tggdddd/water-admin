package org.jeecg.modules.demo.water.appController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterClass;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.service.IWaterClassService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/app/classify")
public class ClassController {
    @Autowired
    private IWaterClassService waterClassService;
    @Autowired
    private IWaterShopService shopService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 获取最末部门
     * 用于 订单站点派送 选择
     */
    @GetMapping("depart")
    public ThinkResult getDepart() {
        List<SysDepartModel> allSysDepart = sysBaseAPI.getAllSysDepart();
        LinkedList<SysDepartModel> tailNode = new LinkedList<>();
        for (SysDepartModel sysDepartModel : allSysDepart) {
            boolean flag = true;
            for (SysDepartModel departModel : allSysDepart) {
                if (Objects.equals(departModel.getParentId(), sysDepartModel.getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                tailNode.add(sysDepartModel);
            }
        }
        return ThinkResult.ok(tailNode);
    }

    /**
     * 获取所有分类
     */
    @RequestMapping("index")
    public ThinkResult getClassify() {
        List<WaterClass> list = waterClassService.list();
        return ThinkResult.ok(list);
    }

    /**
     * 获取当前分类
     */
    @RequestMapping("getById")
    public ThinkResult getClassifyById(@RequestParam("id") String id) {
        WaterClass byId = waterClassService.getById(id);
        return ThinkResult.ok(byId);
    }

    /**
     * 分页列表查询商品数据
     */
    @RequestMapping(value = "list")
    public ThinkResult queryPageList(@RequestParam(name = "id", defaultValue = "0") String classId,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        MPJLambdaWrapper<WaterShop> shopLambdaQueryWrapper = new MPJLambdaWrapper<WaterShop>();
        // 使用 eq 防止模糊查询
        if (!Objects.equals(classId, "0")) {
            shopLambdaQueryWrapper.eq(WaterShop::getTypeId, classId);
        }
        shopLambdaQueryWrapper.eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                .selectAll(WaterShop.class)
                .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId).select(WaterShopItem::getFromId).selectCount(WaterShopItem::getReserve).selectMin(WaterShopItem::getRetail)
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue()).groupBy(WaterShopItem::getFromId);
        Page<ShopVo> page = new Page<>(pageNo, pageSize);
        IPage<ShopVo> pageList = shopService.selectJoinListPage(page, ShopVo.class, shopLambdaQueryWrapper);
        return ThinkResult.ok(pageList);
    }

}
