package com.islizx.service;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Log;

import java.util.List;


/**
 * @author lizx
 * @date 2020-02-21 - 14:36
 */
public interface LogService {
    /**
     * 移除所有日志
     */
    void removeAllLog();

    PageInfo<Log> getAll(Integer pageIndex, Integer pageSize);

    Integer delete(Integer id);

    Log insertOrUpdate(Log log);

    Log getById(Integer id);

    List<Log> findByBatchIds(List<Integer> ids);
}
