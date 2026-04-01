CREATE TABLE `sys_log`
(
    `id`            varchar(64) NOT NULL COMMENT '主键ID',
    `trace_id`      varchar(64)                                              DEFAULT NULL COMMENT '链路追踪ID',
    `user_id`       varchar(64)                                              DEFAULT NULL COMMENT '用户ID',
    `username`      varchar(100)                                             DEFAULT NULL COMMENT '用户名',
    `module`        varchar(100)                                             DEFAULT NULL COMMENT '模块名称',
    `operation`     varchar(100)                                             DEFAULT NULL COMMENT '操作类型(新增/修改/删除/查询)',
    `method`        varchar(255)                                             DEFAULT NULL COMMENT '方法名（类名.方法）',
    `request_uri`   varchar(255)                                             DEFAULT NULL COMMENT '请求URI',
    `http_method`   varchar(10)                                              DEFAULT NULL COMMENT '请求方式',
    `request_param` text COMMENT '请求参数(JSON)',
    `response_data` text COMMENT '响应数据(JSON)',
    `ip`            varchar(64)                                              DEFAULT NULL COMMENT 'IP地址',
    `user_agent`    varchar(255)                                             DEFAULT NULL COMMENT '浏览器信息',
    `status`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态(0失败 1成功)',
    `error_msg`     text COMMENT '错误信息',
    `execute_time`  bigint                                                   DEFAULT NULL COMMENT '执行耗时(ms)',
    `create_time`   datetime                                                 DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志表';