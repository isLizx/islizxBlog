package com.islizx.service.impl;

import com.islizx.model.dto.LizxConst;
import com.islizx.model.enums.BlogPropertiesEnum;
import com.islizx.service.MailService;
import com.islizx.util.MyUtils;
import io.github.biezhi.ome.OhMyEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

/**
 * @author lizx
 * @date 2020-02-14 - 16:29
 */
@Service
public class MailServiceImpl implements MailService {
    //template模板引擎
    @Autowired(required = false)
    private TemplateEngine templateEngine;

    /**
     * 发送邮件
     *
     * @param to      to 接收者
     * @param subject subject 标题
     * @param content content 内容
     */
    @Override
    public void sendMail(String to, String subject, String content) {
        //配置邮件服务器
        MyUtils.configMail(
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_USERNAME.getProp()),
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_PASSWORD.getProp()));
        try {
            OhMyEmail.subject(subject)
                    .from(LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_FROM_NAME.getProp()))
                    .to(to)
                    .text(content)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送模板邮件
     *
     * @param to           接收者
     * @param subject      主题
     * @param content      内容
     * @param templateName 模板路径
     */
    @Async
    @Override
    public void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName) {
        //配置邮件服务器
        MyUtils.configMail(
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_USERNAME.getProp()),
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_PASSWORD.getProp()));
        String text = "";
        try {
            /*Template template = freeMarker.getConfiguration().getTemplate(templateName);
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);*/
            //使用模板thymeleaf
            Context context = new Context();
            context.setVariables(content);
            //获取thymeleaf的html模板
            text = templateEngine.process(templateName,context); //指定模板路径
            OhMyEmail.subject(subject)
                    .from(LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_FROM_NAME.getProp()))
                    .to(to)
                    .html(text)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送带有附件的邮件
     *
     * @param to           接收者
     * @param subject      主题
     * @param content      内容
     * @param templateName 模板路径
     * @param attachSrc    附件路径
     */
    @Async
    @Override
    public void sendAttachMail(String to, String subject, Map<String, Object> content, String templateName, String attachSrc) {
        //配置邮件服务器
        MyUtils.configMail(
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_USERNAME.getProp()),
                LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_SMTP_PASSWORD.getProp()));
        File file = new File(attachSrc);
        String text = "";
        try {
            /*Template template = freeMarker.getConfiguration().getTemplate(templateName);
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);*/
            //使用模板thymeleaf
            Context context = new Context();
            context.setVariables(content);
            //获取thymeleaf的html模板
            text = templateEngine.process(templateName,context); //指定模板路径
            OhMyEmail.subject(subject)
                    .from(LizxConst.OPTIONS.get(BlogPropertiesEnum.MAIL_FROM_NAME.getProp()))
                    .to(to)
                    .html(text)
                    .attach(file, file.getName())
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
