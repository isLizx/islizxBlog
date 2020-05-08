package com.islizx.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.islizx.entity.Attachment;
import com.islizx.mapper.AttachmentMapper;
import com.islizx.service.AttachmentService;
import com.islizx.util.ConfigManager;
import com.islizx.util.MyUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
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

    private static String ENDPOINT = null;
    private static String ACCESSKEYID = null;
    private static String ACCESSKEYSECRET = null;
    private static String BUCKETNAME = null;
    private static String URLPREFIX = null;
    private OSSClient client  = null;
    private ObjectMetadata meta = null;

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    static{
        try {
            ENDPOINT = ConfigManager.getProperty("aliyun.endpoint");
            ACCESSKEYID = ConfigManager.getProperty("aliyun.accessKeyId");
            ACCESSKEYSECRET = ConfigManager.getProperty("aliyun.accessKeySecret");
            BUCKETNAME = ConfigManager.getProperty("aliyun.bucketName");
            URLPREFIX = ConfigManager.getProperty("aliyun.urlPrefix");
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public void init(){
        // 初始化一个OSSClient
        client = new OSSClient(ENDPOINT,ACCESSKEYID, ACCESSKEYSECRET);
        meta = new ObjectMetadata();
    }

    @Override
    public Map<String, Object> attachUpload(MultipartFile file, HttpServletRequest request) {
        final Map<String, Object> resultMap = new HashMap<>(7);
        try {
            //用户目录
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

    /*public Map<String, Object> attachUpload(MultipartFile file, HttpServletRequest request) {
        final Map<String, Object> resultMap = new HashMap<>(7);
        try {
            //用户目录
            final StringBuilder uploadPath = new StringBuilder("images/");
            uploadPath.append(DateUtil.thisYear()).append("/").append(DateUtil.thisMonth() + 1).append("/");

            // 存储路径：images/2020/08/
            final File mediaPath = new File(uploadPath.toString());
            String nameWithOutSuffix =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime());
            nameWithOutSuffix+= "_" + String.valueOf((int)(Math.random()*1000));

            //文件后缀
            final String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);

            //带后缀(文件名：15564277465972939.jpg)
            String fileName = nameWithOutSuffix + "." + fileSuffix;

            init();
            // 校验图片格式
            boolean isLegal = false;
            for (String type : IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),
                        type)) {
                    isLegal = true;
                    break;
                }
            }
            // 压缩代码
            InputStream input = file.getInputStream();
            ByteArrayOutputStream byteArrayOutputStreamut = new ByteArrayOutputStream();
            Thumbnails.of(input).size(256, 256).keepAspectRatio(false).toOutputStream(byteArrayOutputStreamut);
            input = new ByteArrayInputStream(byteArrayOutputStreamut.toByteArray());

            // 上传到阿里云
            try {
                client.putObject(BUCKETNAME, uploadPath.toString() + fileName, new
                        ByteArrayInputStream(file.getBytes()));
                client.putObject(BUCKETNAME, uploadPath.toString() + nameWithOutSuffix + "_small." + fileSuffix, input);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.shutdown();
            }


            Long l = file.getSize();
            final String size = MyUtils.parseSize(l);
            final BufferedImage image = ImageIO.read(file.getInputStream());
            if (image != null) {
                resultMap.put("wh", image.getWidth() + "x" + image.getHeight());
            }
            resultMap.put("rowSize", l);
            resultMap.put("fileName", fileName.toString());
            resultMap.put("filePath", URLPREFIX + "/" + uploadPath.toString() + fileName);
            resultMap.put("smallPath", URLPREFIX + "/" + uploadPath.toString() + nameWithOutSuffix + "_small." + fileSuffix);
            resultMap.put("suffix", fileSuffix);
            resultMap.put("size", size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }*/



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
