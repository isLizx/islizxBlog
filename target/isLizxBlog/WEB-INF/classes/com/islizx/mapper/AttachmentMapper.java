package com.islizx.mapper;

import com.islizx.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-07 - 20:16
 */
@Mapper
public interface AttachmentMapper {

    /**
     * 根据id删除附件
     * @param id
     * @return
     */
    Integer deleteById(Integer id);

    /**
     * 添加附件
     * @param attachment
     * @return
     */
    Integer insert(Attachment attachment);

    /**
     * 修改附件
     * @param attachment
     * @return
     */
    Integer update(Attachment attachment);

    /**
     * 条件查询所有附件
     * @param criteria
     * @return
     */
    List<Attachment> findAll(HashMap<String, Object> criteria);

    /**
     * 查询所有附件
     * @return
     */
    List<Attachment> listAttachment();

    /**
     * 根据id查询附件
     * @param id
     * @return
     */
    Attachment getAttachmentById(@Param(value = "id") Integer id);

    /**
     * 附件总数
     *
     * @return
     */
    Integer countAttachment();

    /**
     * 获取附件总大小
     * @return
     */
    List<Long> sumAttachmentSize();
}
