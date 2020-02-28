package com.islizx.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Attachment;
import com.islizx.mapper.AttachmentMapper;
import com.islizx.service.AttachmentService;
import com.islizx.util.MyUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizx
 * @date 2020-02-07 - 15:56
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired(required = false)
    AttachmentMapper attachmentMapper;

    @Override
    public Map<String, Object> attachUpload(MultipartFile file, HttpServletRequest request) {
        final Map<String, Object> resultMap = new HashMap<>(7);
        try {
            //用户目录
//            final StringBuilder uploadPath = new StringBuilder(System.getProperties().getProperty("user.home"));
//            uploadPath.append("/static/upload/" + DateUtil.thisYear()).append("/").append(DateUtil.thisMonth() + 1).append("/");

            final StringBuilder uploadPath = new StringBuilder(request.getSession().getServletContext().getRealPath("/static/upload/"));
            uploadPath.append(DateUtil.thisYear()).append("/").append(DateUtil.thisMonth() + 1).append("/");
            final File mediaPath = new File(uploadPath.toString());
            if (!mediaPath.exists()) {
                if (!mediaPath.mkdirs()) {
                    resultMap.put("success", "0");
                    return resultMap;
                }
            }
            String nameWithOutSuffix =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime());
            nameWithOutSuffix+= "_" + String.valueOf((int)(Math.random()*1000));

            //不带后缀
            //String nameWithOutSuffix = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')).replaceAll(" ", "_").replaceAll(",", "");

            //文件后缀
            final String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);

            //带后缀
            String fileName = nameWithOutSuffix + "." + fileSuffix;

            //判断文件名是否已存在
            File descFile = new File(mediaPath.getAbsoluteFile(), fileName.toString());
            int i = 1;
            while (descFile.exists()) {
                nameWithOutSuffix = nameWithOutSuffix + "(" + i + ")";
                descFile = new File(mediaPath.getAbsoluteFile(), nameWithOutSuffix + "." + fileSuffix);
                i++;
            }
            file.transferTo(descFile);

            //文件原路径
            final StringBuilder fullPath = new StringBuilder(mediaPath.getAbsolutePath());
            fullPath.append("/");
            fullPath.append(nameWithOutSuffix + "." + fileSuffix);

            //压缩文件路径
            final StringBuilder fullSmallPath = new StringBuilder(mediaPath.getAbsolutePath());
            fullSmallPath.append("/");
            fullSmallPath.append(nameWithOutSuffix);
            fullSmallPath.append("_small.");
            fullSmallPath.append(fileSuffix);

            //压缩图片
            Thumbnails.of(fullPath.toString()).size(256, 256).keepAspectRatio(false).toFile(fullSmallPath.toString());

            //映射路径
            final StringBuilder filePath = new StringBuilder("/upload/");
            filePath.append(DateUtil.thisYear());
            filePath.append("/");
            filePath.append(DateUtil.thisMonth() + 1);
            filePath.append("/");
            filePath.append(nameWithOutSuffix + "." + fileSuffix);

            //缩略图映射路径
            final StringBuilder fileSmallPath = new StringBuilder("/upload/");
            fileSmallPath.append(DateUtil.thisYear());
            fileSmallPath.append("/");
            fileSmallPath.append(DateUtil.thisMonth() + 1);
            fileSmallPath.append("/");
            fileSmallPath.append(nameWithOutSuffix);
            fileSmallPath.append("_small.");
            fileSmallPath.append(fileSuffix);

            Long l = new File(fullPath.toString()).length();
            final String size = MyUtils.parseSize(l);
            final String wh = MyUtils.getImageWh(new File(fullPath.toString()));
            resultMap.put("rowSize", l);
            resultMap.put("fileName", fileName.toString());
            resultMap.put("filePath", filePath.toString());
            resultMap.put("smallPath", fileSmallPath.toString());
            resultMap.put("suffix", fileSuffix);
            resultMap.put("size", size);
            resultMap.put("wh", wh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Attachment insert(Attachment attachment) {
        attachmentMapper.insert(attachment);
        return attachment;
    }

    @Override
    public Attachment update(Attachment attachment) {
        attachmentMapper.update(attachment);
        return attachment;
    }

    @Override
    public PageInfo<Attachment> pageAttachment(Integer pageIndex, Integer pageSize, HashMap<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Attachment> attachmentList = attachmentMapper.findAll(criteria);
        return new PageInfo<>(attachmentList, 5);
    }

    @Override
    public Attachment getAttachment(Integer id) {
        return attachmentMapper.getAttachmentById(id);
    }

    @Override
    public void deleteAttachment(Integer id) {
        attachmentMapper.deleteById(id);
    }

    @Override
    public Integer countAttachment() {
        return attachmentMapper.countAttachment();
    }

    @Override
    public String sumAttachmentSize() {
        List<Long> sumAttachSize = attachmentMapper.sumAttachmentSize();
        Long sum = 0l;
        for(Long size:sumAttachSize){
            sum += size;
        }
        return MyUtils.parseSize(sum);
    }

}
