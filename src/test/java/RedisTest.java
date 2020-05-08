import com.islizx.service.IViewService;
import com.islizx.util.OssUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lizx
 * @date 2020-03-11 - 11:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-*.xml")
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IViewService iViewService;

    @Autowired
    OssUtil ossUtil;

    @Test
    public void test1() throws Exception{
        String strUrl = "F:\\1.jpg";
        File file = new File(strUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        ossUtil.upload(multipartFile);
    }
}
