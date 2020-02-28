package com.islizx.service;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizx
 * @date 2020-02-07 - 15:55
 */
public interface AttachmentService {

    /**
     * 将附件上传到服务器
     * @param file
     * @param request
     * @return
     */
    public Map<String, Object> attachUpload(MultipartFile file, HttpServletRequest request);

    /**
     * 添加附件
     * @param attachment
     * @return
     */
    public Attachment insert(Attachment attachment);

    /**
     * 修改附件
     * @param attachment
     * @return
     */
    public Attachment update(Attachment attachment);

    /**
     * 分页条件查询
     * @param pageIndex
     * @param pageSize
     * @param criteria
     * @return
     */
    public PageInfo<Attachment> pageAttachment(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria);

    /**
     * 根据id获得附件
     * @param id
     * @return
     */
    public Attachment getAttachment(Integer id);

    /**
     * 根据id删除附件
     * @param id
     */
    public void deleteAttachment(Integer id);


    /**
     * 获取附件总数
     *
     * @return
     */
    public Integer countAttachment();


    /**
     * 获取附件总大小
     * @return
     */
    String sumAttachmentSize();
}
