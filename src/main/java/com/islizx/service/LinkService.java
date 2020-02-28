package com.islizx.service;

import com.islizx.entity.Link;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 21:09
 */
public interface LinkService {
    Integer insert(Link link);

    Integer delete(Integer id);

    Integer update(Link link);

    List<Link> getAll();

    Link getById(Integer id);
}
