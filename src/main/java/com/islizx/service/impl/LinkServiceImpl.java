package com.islizx.service.impl;

import com.islizx.entity.Link;
import com.islizx.mapper.LinkMapper;
import com.islizx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 21:10
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired(required = false)
    LinkMapper linkMapper;

    @Override
    public Integer insert(Link link) {
        return linkMapper.insert(link);
    }

    @Override
    public Integer delete(Integer id) {
        return linkMapper.delete(id);
    }

    @Override
    public Integer update(Link link) {
        return linkMapper.update(link);
    }

    @Override
    public List<Link> getAll() {
        return linkMapper.getAll();
    }

    @Override
    public Link getById(Integer id) {
        return linkMapper.getById(id);
    }
}
