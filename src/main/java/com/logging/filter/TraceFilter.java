package com.logging.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceFilter implements Filter {

    private static final String TRACE_ID = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest req = (HttpServletRequest) request;

            // 1. 优先从Header获取（跨服务）
            String traceId = req.getHeader(TRACE_ID);

            // 2. 没有则生成
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString().replace("-", "");
            }

            MDC.put("traceId", traceId);

            chain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}