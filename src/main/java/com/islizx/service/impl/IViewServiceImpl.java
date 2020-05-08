package com.islizx.service.impl;

import com.islizx.service.IViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author lizx
 * @date 2020-03-11 - 15:19
 */
@Service
public class IViewServiceImpl implements IViewService {

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Override
    public void viewed(Integer postId) {
        Boolean viewed = redisTemplate.opsForHash().hasKey("postViews", postId);
        if(viewed){
            Integer i = (Integer) redisTemplate.opsForHash().get("postViews", postId);
            redisTemplate.opsForHash().put("postViews", postId, ++i);
        }else{
            redisTemplate.opsForHash().put("postViews", postId, 1);
        }
    }

    @Override
    public Integer getByPostId(Integer postId) {
        Boolean viewed = redisTemplate.opsForHash().hasKey("postViews", postId);
        if(viewed){
            return (Integer) redisTemplate.opsForHash().get("postViews", postId);
        }else{
            return 1;
        }
    }
}
