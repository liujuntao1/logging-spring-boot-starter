package com.logging.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logging.annotation.SysLog;
import com.logging.entity.SysLogEntity;
import com.logging.service.SysLogService;
import com.logging.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author ljt
 * @date 2026/1/21
 * @description
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    @Around("@annotation(sysLog)")
    public Object doAround(ProceedingJoinPoint point, SysLog sysLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        SysLogEntity logEntity = new SysLogEntity();
        try {
            // ===== 基础信息 =====
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            logEntity.setModule(sysLog.module());
            logEntity.setOperation(sysLog.operation());
            logEntity.setMethod(point.getTarget().getClass().getName() + "." + method.getName());
            // ===== 请求信息 =====
            HttpServletRequest request = getRequest();

            if (request != null) {
                logEntity.setRequestUri(request.getRequestURI());
                logEntity.setHttpMethod(request.getMethod());
                String ipAddr = IpUtils.getIpAddr(request);
                logEntity.setIp(ipAddr);
                logEntity.setUserAgent(request.getHeader("User-Agent"));
                logEntity.setUsername(request.getHeader("username"));
            }
            // ===== 参数 =====
            Object[] args = point.getArgs();
            logEntity.setRequestParam(argsToJson(args));

            // ===== traceId =====
            String traceId = MDC.get("traceId");
            logEntity.setTraceId(traceId);
            log.info("======traceId:{},请求ip：{}，请求用户：{}，请求接口：{}，请求参数：{}", logEntity.getTraceId(),
                    logEntity.getIp(), logEntity.getUsername(), logEntity.getRequestUri(), logEntity.getRequestParam());
            // ===== 执行方法 =====
            Object result = point.proceed();

            // ===== 返回值 =====
            logEntity.setResponseData(toJson(result));

            logEntity.setStatus("1");

            return result;
        } catch (Exception e) {
            logEntity.setStatus("0");
            logEntity.setErrorMsg(e.getMessage());

            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            logEntity.setExecuteTime(endTime - startTime);
            logEntity.setCreateTime(LocalDateTime.now());
            log.info("******traceId:{},请求ip：{}，请求用户：{}，请求接口：{}，耗时：{}，返回结果：{}", logEntity.getTraceId(),
                    logEntity.getIp(), logEntity.getUsername(), logEntity.getRequestUri(), logEntity.getExecuteTime(),
                    logEntity.getResponseData());
            // ===== 异步入库 =====
            sysLogService.asyncSave(logEntity);
        }
    }

    // ================= 工具方法 =================

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr == null ? null : attr.getRequest();
    }

    private String argsToJson(Object[] args) {
        try {
            return toJson(args);
        } catch (Exception e) {
            return "参数解析失败";
        }
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "JSON序列化失败";
        }
    }
}