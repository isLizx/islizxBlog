package com.islizx.controller.admin;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Attachment;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台附件控制器
 *
 * @author lizx
 * @date 2020-02-07 - 14:39
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

//    public final String allowSuffix = ".bmp.jpg.jpeg.png.gif.pdf.doc.zip.rar.gz";

    /**
     * 获取所有附件
     * @param pageIndex
     * @param pageSize
     * @param keywords
     * @param model
     * @return
     */
    @RequestMapping(value = "/attachments")
    public String attachments(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                              @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                              @RequestParam(required = false) String keywords,
                              Model model, HttpSession session){
        HashMap<String, Object> criteria = new HashMap<>(1);
        if(keywords != null && !StringUtils.isBlank(keywords)) criteria.put("keywords", keywords);
        PageInfo<Attachment> attachmentPageInfo = attachmentService.pageAttachment(pageIndex, pageSize, criteria);

        model.addAttribute("keywords", keywords);
        model.addAttribute("pageInfo", attachmentPageInfo);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/attachment/attachments";
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/attachments/upload")
    @ResponseBody
    @SystemLog(description = "上传文件", type = LogTypeEnum.ATTACHMENT)
    public Map<String, Object> uploadFile(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request) {
        final Map<String, Object> result = new HashMap<>(3);
        if (!file.isEmpty()) {
            try {
                final Map<String, Object> resultMap = attachmentService.attachUpload(file, request);
                if (resultMap == null || resultMap.isEmpty()) {
                    log.error("File upload failed");
                    result.put("success", 0);
                    result.put("message", "upload-failed");
                    return result;
                }
                //保存在数据库
                Attachment attachment = new Attachment();
                attachment.setAttachName((String) resultMap.get("fileName"));
                attachment.setAttachPath((String) resultMap.get("filePath"));
                attachment.setAttachSmallPath((String) resultMap.get("smallPath"));
                attachment.setAttachType(file.getContentType());
                attachment.setAttachSuffix((String) resultMap.get("suffix"));
                attachment.setCreateTime(DateUtil.date());
                attachment.setAttachSize((String) resultMap.get("size"));
                attachment.setAttachWh((String) resultMap.get("wh"));
                attachment.setRawSize((Long) resultMap.get("rowSize"));
                attachmentService.insert(attachment);
                log.info("Upload file {} to {} successfully", resultMap.get("fileName"), resultMap.get("filePath"));
                result.put("success", 1);
                result.put("message", "upload-success");
                result.put("url", attachment.getAttachPath());
                result.put("filename", resultMap.get("filePath"));
            } catch (Exception e) {
                log.error("Upload file failed:{}", e.getMessage());
                result.put("success", 0);
                result.put("message", "upload-failed");
            }
        } else {
            log.error("File cannot be empty!");
        }

        return result;
    }

    /**
     * 删除附件
     * @param attachId
     * @param request
     * @return
     */
    @RequestMapping(value = "/attachments/delete")
    @SystemLog(description = "删除附件", type = LogTypeEnum.ATTACHMENT)
    public String removeAttachment(@RequestParam("id") Integer attachId,
                                       HttpServletRequest request, HttpSession session){
        Attachment attachment = attachmentService.getAttachment(attachId);

        String delFileName = attachment.getAttachName();
        String delSmallFileName = attachment.getAttachName();
        boolean flag = true;
        try {
            //删除数据库中的内容
            attachmentService.deleteAttachment(attachId);
            //删除文件
            String userPath = request.getSession().getServletContext().getRealPath("") + "/static";
            File mediaPath = new File(userPath, attachment.getAttachPath().substring(0, attachment.getAttachPath().lastIndexOf('/')));
            File delFile = new File(new StringBuffer(mediaPath.getAbsolutePath()).append("/").append(delFileName).toString());
            File delSmallFile = new File(new StringBuffer(mediaPath.getAbsolutePath()).append("/").append(delSmallFileName).toString());
            if (delFile.exists() && delFile.isFile()) {
                flag = delFile.delete() && delSmallFile.delete();
            }

            if (flag) {
                log.info("Delete file {} successfully!", delFileName);
            } else {
                log.error("Deleting attachment {} failed!", delFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Deleting attachment {} failed: {}", delFileName, e.getMessage());
        }
        session.setAttribute("msg", "附件删除成功");
        return "redirect:/admin/attachments";
    }

    /**
     * 处理获取附件详情的请求
     *
     * @param attachId 附件编号
     * @return 模板路径admin/widget/_attachment-detail
     */
    @RequestMapping(value = "/attachments/detail",method = RequestMethod.GET)
    @ResponseBody
    public Msg attachmentDetail(@RequestParam("id") Integer attachId) {
        Attachment attachment = attachmentService.getAttachment(attachId);

        return Msg.success().add("attachment", attachment);
    }

}
