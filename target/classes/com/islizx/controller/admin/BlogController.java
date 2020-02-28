package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Blog;
import com.islizx.model.dto.Msg;
import com.islizx.entity.Type;
import com.islizx.entity.User;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.TagService;
import com.islizx.service.TypeService;
import com.islizx.vo.BlogParam;
import com.islizx.vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.islizx.entity.Tag;

/**
 * @author lizx
 * @date 2020-02-01 - 14:57
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TypeService typeService;

    public static final String TITLE = "title";

    public static final String CONTENT = "content";


    @RequestMapping("/blogs/getpublishedsize")
    @ResponseBody
    public Msg getpublishedsize(){
        // 三种文章类型个数
        Integer publish = blogService.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer draft = blogService.countBlog(BlogStatusEnum.DRAFT.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer trash =blogService.countBlog(BlogStatusEnum.TRASH.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());

        return Msg.success().add("publish", publish).add("draft", draft).add("trash", trash);
    }

    /**
     * 后台文章列表显示
     *
     * @return modelAndView
     */
    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "0") Integer published,
                        HttpSession session, Model model) {
        // 三种文章类型个数
        Integer publish = blogService.countBlog(BlogStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer draft = blogService.countBlog(BlogStatusEnum.DRAFT.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());
        Integer trash =blogService.countBlog(BlogStatusEnum.TRASH.getCode(), PostTypeEnum.POST_TYPE_POST.getCode());

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        criteria.put("published", published);
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);
        List<Type> typeList = typeService.listType();

        model.addAttribute("pageInfo", blogPageInfo);
        model.addAttribute("typeList", typeList);

        model.addAttribute("publish", publish);
        model.addAttribute("draft", draft);
        model.addAttribute("trash", trash);

        model.addAttribute("published", published);

        String msg = (String) session.getAttribute("msg");
        if(msg != null && !msg.equals("")){
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/article/blogs";
    }

    /**
     * 分页条件查询
     *
     * @param pageIndex 页数
     * @param pageSize
     * @param blogQuery
     * @param model
     * @return
     */
    @RequestMapping("/blogs/search")
    public String search(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                         @RequestParam(required = false, defaultValue = "15") Integer pageSize, BlogQuery blogQuery, Model model){
        HashMap<String, Object> criteria = new HashMap<>();

        criteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        String keywords = blogQuery.getKeywords();
        String flag = blogQuery.getBlogSource();
        Integer published = blogQuery.getPublished();
        String sort = blogQuery.getSort();
        String order = blogQuery.getOrder();
        if(!StringUtils.isBlank(keywords)){
            if (CONTENT.equals(blogQuery.getSearchType())) {
                criteria.put("content", keywords);
            } else {
                criteria.put("title", keywords);
            }
        }
        if(flag != null && !StringUtils.isBlank(flag)) criteria.put("flag", flag);
        if(published != null){
            criteria.put("published", published);
        }else{
            criteria.put("published", 0);
        }
        if(sort != null && !StringUtils.isBlank(sort)) criteria.put("sort", sort);
        if(order != null && !StringUtils.isBlank(order)) criteria.put("order", order);
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", blogPageInfo);

        return "admin/article/blogs :: blogList";
    }

    /**
     * 删除博客文章
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "删除文章", type = LogTypeEnum.OPERATION)
    public Msg delete(@PathVariable("id") Integer id){
        Blog blogDB = blogService.getBlogByPublishedAndId(null ,PostTypeEnum.POST_TYPE_POST.getCode(),id);
        Integer published = blogDB.getPublished();
        if(published == BlogStatusEnum.PUBLISHED.getCode() || BlogStatusEnum.DRAFT.getCode() == 1){
            blogDB.setPublished(BlogStatusEnum.TRASH.getCode());
            blogService.updateBlog(blogDB);
            return Msg.success().add("msg", "已仍入回收站");
        }else{
            blogService.deleteBlog(id);
            return Msg.success().add("msg", "文章已删除");
        }
    }

    /**
     * 草稿、回收站转发布
     * @param blogId
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/blogs/revert", method = RequestMethod.PUT)
    @SystemLog(description = "将文章改为已发布", type = LogTypeEnum.OPERATION)
    public Msg moveToPublish(@RequestParam("id") Integer blogId, HttpSession session){
        Blog blogDB = blogService.getBlogByPublishedAndId(null ,PostTypeEnum.POST_TYPE_POST.getCode(),blogId);
        blogDB.setPublished(BlogStatusEnum.PUBLISHED.getCode());
        blogService.updateBlog(blogDB);

        return Msg.success().add("msg", "文章已发布!");
    }


    /**
     * 批量删除
     *
     * @param del_idstr 文章D列表
     * @return
     */
    @RequestMapping(value = "/blogs/batchDelete", method = RequestMethod.DELETE)
    @ResponseBody
    @SystemLog(description = "批量删除文章", type = LogTypeEnum.OPERATION)
    public Msg batchDelete(@RequestParam("del_idstr") String del_idstr){
        List<Integer> ids = new ArrayList<>();
        if(del_idstr.contains("-")){
            String[] str_ids = del_idstr.split("-");
            for(String string:str_ids){
                ids.add(Integer.parseInt(string));
            }
        }else{
            // 单个删除
            Integer id = Integer.parseInt(del_idstr);
            ids.add(id);
        }

        //1、防止恶意操作
        if (ids == null || ids.size() == 0 || ids.size() >= 100) {
            return Msg.fail().add("msg", "参数不合法!");
        }

        List<Blog> blogList = blogService.findByBatchIds(ids);

        //3、如果当前状态为回收站，则删除；否则，移到回收站
        for(Blog blog:blogList){
            if(Objects.equals(blog.getPublished(), BlogStatusEnum.TRASH.getCode())){
                blogService.deleteBlog(blog.getId());
            }else{
                blog.setPublished(BlogStatusEnum.TRASH.getCode());
                blogService.updateBlog(blog);
            }
        }

        return Msg.success().add("msg", "删除成功!");
    }

    /**
     * 初始化Type和Tag数据
     *
     * @param model
     */
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     *  后台添加页面显示
     * @param model
     * @return
     */
    @RequestMapping(value = "/blogs/input", method = RequestMethod.GET)
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return "admin/article/blogs-input";
    }

    /**
     * 进行修改和添加的提交
     * @param blogParam
     * @param session
     * @return
     */
    @RequestMapping(value = "/blogs", method = RequestMethod.POST)
    @SystemLog(description = "更新文章", type = LogTypeEnum.OPERATION)
    public String post(BlogParam blogParam, HttpSession session) {
        Blog blog = new Blog();
        // 用户ID
        User user = (User) session.getAttribute("user");
        if (user != null) {
            blog.setUserId(user.getId());
        }
        // flag
        blog.setFlag(blogParam.getFlag());
        // 允许赞赏
        blog.setAppreciation(blogParam.getAppreciation());
        // 允许评论
        blog.setCommentabled(blogParam.getCommentabled());
        // 首图
        blog.setFirstPicture(blogParam.getFirstPicture());
        // 是否推荐
        blog.setRecommend(blogParam.getRecommend());
        // 是否允许转载
        blog.setShareStatement(blogParam.getShareStatement());
        // 标题
        blog.setTitle(blogParam.getTitle());
        // 文章摘要
        blog.setDescription(blogParam.getDescription());
        // 文章内容
        blog.setContent(blogParam.getContent());
        // 发布状态
        blog.setPublished(blogParam.getPublished());
        // 填充分类
        blog.setTypeId(blogParam.getTypeId());
        blog.setType(typeService.getTypeById(blogParam.getTypeId()));
        blog.setPostType(PostTypeEnum.POST_TYPE_POST.getCode());
        // 填充标签
        List<Tag> tagList = new ArrayList<>();
        if (blogParam.getTagIdList() != null) {
            for (int i = 0; i < blogParam.getTagIdList().size(); i++) {
                Tag tag = new Tag(blogParam.getTagIdList().get(i));
                tagList.add(tag);
            }
        }
        blog.setTags(tagList);
        if(blogParam.getId() == null){
            blogService.insertBlog(blog);
        }else{
            blog.setId(blogParam.getId());
            blogService.updateBlogDetail(blog);
        }
        session.setAttribute("msg", "文章更新成功");
        return "redirect:/admin/blogs";
    }

    /**
     * 进入修改页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/blogs/input/{id}", method = RequestMethod.GET)
    public String editInput(@PathVariable("id") Integer id, Model model) {
        setTypeAndTag(model);

        Blog blog = blogService.getBlogByPublishedAndId(null,PostTypeEnum.POST_TYPE_POST.getCode(), id);
        blog.init();
        model.addAttribute("blog",blog);

        return "admin/article/blogs-input";
    }


}
