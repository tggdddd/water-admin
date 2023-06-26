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
 * @Description: 分类
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Data
@TableName("water_class")
@ApiModel(value = "water_class对象", description = "分类")
public class WaterClass implements Serializable {
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
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    private transient java.lang.String imageString;

    private byte[] image;
    /**
     * 图标
     */
    @Excel(name = "图标", width = 15)
    private transient java.lang.String iconString;
    private byte[] icon;
    /**
     * 简介
     */
    @Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private java.lang.String brief;
    /**
     * 分类级别
     */
    @Excel(name = "分类级别", width = 15)
    @ApiModelProperty(value = "分类级别")
    private java.lang.String level;
    /**
     * 父级节点
     */
    @Excel(name = "父级节点", width = 15, dictTable = "water_class", dicText = "name", dicCode = "id")
    @Dict(dictTable = "water_class", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "父级节点")
    private java.lang.String pid;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;

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

    public byte[] getIcon() {
        if (iconString == null) {
            return null;
        }
        try {
            return iconString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getIconString() {
        if (icon == null || icon.length == 0) {
            return "";
        }
        try {
            return new String(icon, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
