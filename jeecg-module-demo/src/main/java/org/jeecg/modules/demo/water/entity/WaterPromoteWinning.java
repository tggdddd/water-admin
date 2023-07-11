package org.jeecg.modules.demo.water.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 推广活动获奖记录
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
@Data
@TableName("water_promote_winning")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "water_promote_winning对象", description = "推广活动获奖记录")
public class WaterPromoteWinning implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 推广用户
     */
    @Excel(name = "推广用户", width = 15)
    @ApiModelProperty(value = "推广用户")
    private java.lang.String userId;
    /**
     * 活动
     */
    @Excel(name = "活动", width = 15)
    @ApiModelProperty(value = "活动")
    private java.lang.String activityId;
    /**
     * 活动商品
     */
    @Excel(name = "活动商品", width = 15, dictTable = "water_shop_item", dicText = "name", dicCode = "id")
    @Dict(dictTable = "water_shop_item", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "活动商品")
    private java.lang.String shopItemId;
    /**
     * 优惠价格
     */
    @Excel(name = "优惠价格", width = 15)
    @ApiModelProperty(value = "优惠价格")
    private java.lang.String relief;
    /**
     * 注册用户
     */
    @Excel(name = "注册用户", width = 15)
    @ApiModelProperty(value = "注册用户")
    private java.lang.String registerUserId;
    /**
     * 推广状态
     */
    @Excel(name = "推广状态", width = 15, dicCode = "promote_status")
    @Dict(dicCode = "promote_status")
    @ApiModelProperty(value = "推广状态")
    private java.lang.String status;
    /**
     * 逻辑删除
     */
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.String deleteFlag;
}
