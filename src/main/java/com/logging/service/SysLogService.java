package com.logging.service;

import com.logging.entity.SysLogEntity;
import com.logging.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Async("logExecutor")
    public void asyncSave(SysLogEntity log) {
        sysLogMapper.insert(log);
    }
}