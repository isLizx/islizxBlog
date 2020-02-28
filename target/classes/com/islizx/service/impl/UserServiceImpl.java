package com.islizx.service.impl;

import com.islizx.entity.User;
import com.islizx.mapper.BlogMapper;
import com.islizx.mapper.UserMapper;
import com.islizx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户管理
 * @author lizx
 * @date 2020-01-30 - 15:46
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private BlogMapper blogMapper;

    /**
     * 获取所有用户并查询该用户所写的文章数
     * @return List<User>
     */
    @Override
    public List<User> listUser() {
        List<User> userList = userMapper.listUser();
        for (int i = 0; i < userList.size(); i++) {
            Integer blogCount = blogMapper.countBlogByUser(userList.get(i).getId());
            userList.get(i).setBlogCount(blogCount);
        }
        return userList;
    }

    /**
     * 根据用户id查询
     * @param id 用户ID
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    /**
     * 修改用户
     * @param user 用户
     */
    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    /**
     * 删除用户
     * @param id 用户ID
     */
    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 添加一位用户
     * @param user 用户
     * @return
     */
    @Override
    public User insertUser(User user) {
        user.setRegisterTime(new Date());
        userMapper.insert(user);
        return user;
    }

    /**
     * 根据用户名或Email查询用户
     * @param str 用户名或Email
     * @return
     */
    @Override
    public User getUserByNameOrEmail(String str) {
        return userMapper.getUserByNameOrEmail(str);
    }

    /**
     * 更具用户名查询用户
     * @param name 用户名
     * @return
     */
    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    /**
     * 根据Email查询用户
     * @param email Email
     * @return
     */
    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

}
