package com.islizx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Log;
import com.islizx.mapper.LogMapper;
import com.islizx.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-21 - 14:37
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired(required = false)
    private LogMapper logMapper;

    public PageInfo<Log> getAll(Integer pageIndex, Integer pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        return new PageInfo<>(logMapper.getAll(), 5);
    }

    public Integer delete(Integer id){
        return logMapper.delete(id);
    }
    /**
     * 移除所有日志
     */
    @Override
    public void removeAllLog() {
        logMapper.deleteAll();
    }



    @Override
    public Log insertOrUpdate(Log entity) {
        if (entity.getId() == null) {
            logMapper.insert(entity);
        } else {
            logMapper.update(entity);
        }
        return entity;
    }

    @Override
    public Log getById(Integer id) {
        return logMapper.getById(id);
    }

    @Override
    public List<Log> findByBatchIds(List<Integer> ids) {
        return logMapper.selectByIds(ids);
    }


}
