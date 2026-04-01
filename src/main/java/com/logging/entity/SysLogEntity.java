package com.logging.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ljt
 * @date 2026/1/21
 * @description
 */
@Data
@TableName(value = "sys_log")
public class SysLogEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 链路追踪ID
     */
    private String traceId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 模块名称
     */
    private String module;
    /**
     * 操作类型(新增/修改/删除/查询)
     */
    private String operation;
    /**
     * 方法名（类名.方法）
     */
    private String method;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 请求方式
     */
    private String httpMethod;
    /**
     * 请求参数(JSON)
     */
    private String requestParam;
    /**
     * 响应数据(JSON)
     */
    private String responseData;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 浏览器信息
     */
    private String userAgent;
    /**
     * 状态(0失败 1成功)
     */
    private String status;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 执行耗时(ms)
     */
    private Long executeTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}