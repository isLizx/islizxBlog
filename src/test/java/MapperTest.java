import com.islizx.controller.admin.CommentController;
import com.islizx.entity.Comment;
import com.islizx.exception.ControllerExceptionHandler;
import com.islizx.mapper.BlogMapper;
import com.islizx.mapper.BlogTagRefMapper;
import com.islizx.mapper.CommentMapper;
import com.islizx.mapper.UserMapper;
import com.islizx.model.dto.LizxConst;
import com.islizx.service.CommentService;
import com.islizx.service.MailService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author lizx
 * @date 2020-01-31 - 01:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-*.xml")
public class MapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogTagRefMapper blogTagRefMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SqlSession sqlSession;

    @Autowired
    CommentService commentService;

    @Autowired
    LizxConst lizxConst;

    @Autowired
    CommentController commentController;

    @Autowired
    MailService mailService;

    @Autowired
    ControllerExceptionHandler controllerExceptionHandler;



    @Test
    public void test1(){
//       commentController.moveToPublish(71);
//        System.out.println(lizxConst.OPTIONS);
//        System.out.println(lizxConst.OPTIONS.get("mail_smtp_password"));
//        mailService.sendMail("isLizx@163.com", "这是一封测试TEXT邮件2", "信件内容2");
        System.out.println(controllerExceptionHandler);
    }

    @Test
    public void testCRUD(){
/*

        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        HashMap<String, Object> criteria = new HashMap<>();
        criteria.put("keywords", "14");
        criteria.put("published", true);
        List<Blog> blogList = blogMapper.findAll(criteria);
        for(Blog blog:blogList){
            System.out.println(blog.getId());
        }
*/
        /*List<Comment> commentList = commentService.getListCommentByBlogId(55);
        System.out.println("===================================/n===================================");
        for(Comment comment:commentList){
            System.out.println("主评论:" + comment.getNickname() + comment.getContent());
            for(Comment childComment:comment.getReplyComments()){
                System.out.println("回复评论:" + childComment.getNickname() + childComment.getContent());
            }
        }*/
    }
}
