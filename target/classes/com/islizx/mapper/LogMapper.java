package com.islizx.mapper;

import com.islizx.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-21 - 14:37
 */
@Mapper
public interface LogMapper {


    Integer deleteAll();

    Integer insert(Log log);

    Integer update(Log log);

    Integer delete(Integer id);

    List<Log> getAll();

    Log getById(Integer id);

    List<Log> selectByIds(@Param("ids") List<Integer> ids);
}
