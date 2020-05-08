package com.islizx.service;

/**
 * @author lizx
 * @date 2020-03-11 - 15:17
 */
public interface IViewService {

    public void viewed(Integer postId);

    public Integer getByPostId(Integer postId);
}
