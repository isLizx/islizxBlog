package com.islizx.mapper;

import com.islizx.entity.Link;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 21:00
 */
@Mapper
public interface LinkMapper {

    Integer insert(Link link);

    Integer delete(Integer id);

    Integer update(Link link);

    List<Link> getAll();

    Link getById(Integer id);
}
