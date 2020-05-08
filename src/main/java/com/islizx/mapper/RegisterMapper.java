package com.islizx.mapper;

import com.islizx.entity.Register;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizx
 * @date 2020-03-06 - 16:16
 */
@Mapper
public interface RegisterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Register record);

    int insertSelective(Register record);

    Register selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Register record);

    int updateByPrimaryKey(Register record);

    Register getRegisterByCode(String code);

    Register getRegisterByUserId(Integer userId);
}
