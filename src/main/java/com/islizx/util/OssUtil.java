package com.islizx.util;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.islizx.model.dto.FileUploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author lizx
 * @date 2020-03-11 - 22:13
 */
@Component
public class OssUtil {

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

    /**
     * 文件上传
     * @param uploadFile
     * @return
     */
    public FileUploadResult upload(MultipartFile uploadFile) {
        init();
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        //封装Result对象，并且将文件的byte数组放置到result对象中
        FileUploadResult fileUploadResult = new FileUploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        // 上传到阿里云
        try {
            client.putObject(BUCKETNAME, filePath, new
                    ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        } finally {
            client.shutdown();
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setResponse("success");
        fileUploadResult.setName(URLPREFIX + filePath);
        fileUploadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return fileUploadResult;
    }

    /**
     * 生成路径以及文件名 例如：//images/2019/08/15564277465972939.jpg
     *
     * @param sourceFileName
     * @return
     */
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    /**
     * 查看文件列表
     *
     * @return
     */
    public List<OSSObjectSummary> list() {
        init();
        // 设置最大个数。
        final int maxKeys = 200;
        // 列举文件。
        ObjectListing objectListing = client.listObjects(new ListObjectsRequest(BUCKETNAME).withMaxKeys(maxKeys));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        client.shutdown();
        return sums;
    }

    /**
     * 删除文件
     *
     * @param objectName
     * @return
     */
    public FileUploadResult delete(String objectName) {
        init();
        // 根据BucketName,objectName删除文件
        Integer urlPrefixLength = URLPREFIX.length();
        objectName = objectName.substring(urlPrefixLength+1, objectName.length());
        client.deleteObject(BUCKETNAME, objectName);
        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setName(objectName);
        fileUploadResult.setStatus("removed");
        fileUploadResult.setResponse("success");
        client.shutdown();
        return fileUploadResult;
    }

    /**
     * 下载文件
     *
     * @param os
     * @param objectName
     * @throws IOException
     */
    public void exportOssFile(OutputStream os, String objectName) throws IOException {
        init();
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = client.getObject(BUCKETNAME, objectName);
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }
}
