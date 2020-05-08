import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lizx
 * @date 2020-02-07 - 22:48
 */
public class test {
    // 该邮箱修改为你需要测试的邮箱地址
    private static final String TO_EMAIL = "isLizx@163.com";

    //@Before
    public void before() {
        // 配置，一次即可
        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), "2967007822@qq.com", "osognegihhwfdgde");
    }

    @Test
    public void testSendText() throws SendMailException {
        OhMyEmail.subject("这是一封测试TEXT邮件")
                .from("小姐姐的邮箱")
                .to(TO_EMAIL)
                .text("信件内容")
                .send();
        Assert.assertTrue(true);
    }

    @Test
    public void test1(){
        String objectName = "https://islizx.oss-cn-hangzhou.aliyuncs.com/images/2020/3/20200312_131644_983.jpg";
        Integer urlPrefixLength = "https://islizx.oss-cn-hangzhou.aliyuncs.com".length();
        objectName = objectName.substring(urlPrefixLength+1, objectName.length());
        System.out.println(objectName);
    }
}
