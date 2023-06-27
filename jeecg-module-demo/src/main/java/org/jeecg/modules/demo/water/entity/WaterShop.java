package org.jeecg.modules.demo.water.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@ApiModel(value = "water_shop对象", description = "商品")
@Data
@TableName("water_shop")
public class WaterShop implements Serializable {
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
     * 分类
     */
    @Excel(name = "分类", width = 15, dictTable = "water_class", dicText = "name", dicCode = "id")
    @Dict(dictTable = "water_class", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "分类")
    private java.lang.String typeId;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 简介
     */
    @Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private java.lang.String biref;
    /**
     * 详情
     */
    @Excel(name = "详情", width = 15)
    @ApiModelProperty(value = "详情")
    private java.lang.String detail;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    private transient java.lang.String imageString;

    private byte[] image;

    public byte[] getImage() {
        if (imageString == null) {
            return null;
        }
        try {
            return imageString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageString() {
        if (image == null || image.length == 0) {
            return "";
        }
        try {
            return new String(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 详情图片
     */
    @Excel(name = "详情图片", width = 15)
    private transient java.lang.String imagesString;

    private byte[] images;

    public byte[] getImages() {
        if (imagesString == null) {
            return null;
        }
        try {
            return imagesString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImagesString() {
        if (images == null || images.length == 0) {
            return "";
        }
        try {
            return new String(images, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String unit;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yes_or_no")
    @Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String status;
    /**
     * 逻辑删除
     */
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    private java.lang.String isDelete;
    /**
     * 销量
     */
    @Excel(name = "销量", width = 15)
    @ApiModelProperty(value = "销量")
    private java.lang.String sale;
}
