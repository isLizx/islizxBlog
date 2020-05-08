package com.islizx.controller.home;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.*;
import com.islizx.model.dto.CountDTO;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.model.enums.WidgetTypeEnum;
import com.islizx.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lizx
 * @date 2020-02-16 - 20:26
 */
@Controller
public class HomeIndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 请求首页
     *
     * @param model model
     * @return 模板路径
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
                        @RequestParam(value = "sort", defaultValue = "createTime") String sort,
                        @RequestParam(value = "order", defaultValue = "desc") String order) {
        //调用方法渲染首页
        return this.index(model, 1, pageSize, sort, order);
    }

    /**
     * 首页分页
     *
     * @param model model
     * @return 模板路径/themes/{theme}/index
     */
    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public String index(Model model,
                        @PathVariable(value = "pageNumber") Integer pageNumber,
                        @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
                        @RequestParam(value = "sort", defaultValue = "createTime") String sort,
                        @RequestParam(value = "order", defaultValue = "desc") String order) {
        //文章分页排序
        HashMap<String, Object> blogCriteria = new HashMap<>(1);
        blogCriteria.put("sort", sort);
        blogCriteria.put("order", order);
        blogCriteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
        blogCriteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageNumber, pageSize, blogCriteria);
        model.addAttribute("pageInfo", blogPageInfo);


        //2.首页的公告列表
        HashMap<String, Object> noticeCriteria = new HashMap<>(1);
        noticeCriteria.put("postType", PostTypeEnum.POST_TYPE_NOTICE.getCode());
        noticeCriteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
        PageInfo<Blog> noticePageInfo = blogService.pageBlog(0, pageSize, noticeCriteria);
        model.addAttribute("noticePageInfo", noticePageInfo);

        //3.幻灯片
//        List<Slide> slides = slideService.findAll();
//        model.addAttribute("slideList", slides);


        //4.侧边栏和底部栏组件
        //前台主要小工具
        List<Widget> postWidgets = widgetService.findWidgetByType(WidgetTypeEnum.POST_DETAIL_SIDEBAR_WIDGET.getCode());
        model.addAttribute("postWidgets", postWidgets);


        CountDTO countDTO = null;
        countDTO = (CountDTO) redisTemplate.opsForValue().get("index_countDTO");
        if(countDTO == null){
            //redis缓存中无数据，从数据库中查询，并放入redis缓存中，设置生存时间为1小时
            System.out.println("从数据库中取网站数据");
            countDTO = blogService.getAllCount();
            redisTemplate.opsForValue().set("index_countDTO", countDTO, 1, TimeUnit.HOURS);
        } else {
            System.out.println("从redis缓存中取网站数据");
        }

        //统计
        model.addAttribute("allCount", countDTO);

        //文章排名
        model.addAttribute("blogViews", blogService.listBlogByViewCount(5));


        //分类
        List<Type> types = typeService.listTypeWithCount();
        model.addAttribute("types", types);
        //标签
        List<Tag> tags = tagService.listTagWithCount(null);
        model.addAttribute("tags", tags);
        return "home/index";
    }

    /**
     * 请求首页
     *
     * @param model model
     * @return 模板路径
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(Model model,
                         @RequestParam String query,
                         @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
                         @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                         @RequestParam(value = "sort", defaultValue = "createTime") String sort,
                         @RequestParam(value = "order", defaultValue = "desc") String order) {
        //调用方法渲染首页
        return this.search(query, model, pageSize, pageIndex, sort, order);
    }

    /**
     * 分页搜索
     * @param query
     * @param model
     * @param pageSize
     * @param pageIndex
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value = "/search/{pageIndex}", method = RequestMethod.GET)
    public String search(@RequestParam String query, Model model,
                         @RequestParam(value = "size", defaultValue = "15") Integer pageSize,
                         @PathVariable(value = "pageIndex") Integer pageIndex,
                         @RequestParam(value = "sort", defaultValue = "createTime") String sort,
                         @RequestParam(value = "order", defaultValue = "desc") String order) {
        HashMap<String, Object> blogCriteria = new HashMap<>(1);
        blogCriteria.put("sort", sort);
        blogCriteria.put("order", order);
        blogCriteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
        blogCriteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        blogCriteria.put("title", query);
        PageInfo<Blog> blogPageInfo = blogService.pageBlog(pageIndex, pageSize, blogCriteria);
        model.addAttribute("blogPageInfo", blogPageInfo);
        model.addAttribute("query", query);

        return "home/search";
    }

    /**
     * 判断页面，文章，公告
     * @param postUrl
     * @return
     */
    @RequestMapping(value = {"/{postUrl}.html", "post/{postUrl}"}, method = RequestMethod.GET)
    public String blog(@PathVariable String postUrl) {
        Boolean isNumeric = StringUtils.isNumeric(postUrl);
        Blog post;
        if (isNumeric) {
            post = blogService.getBlogByPublishedAndId(BlogStatusEnum.PUBLISHED.getCode(),null, Integer.valueOf(postUrl));
            if (post == null) {
                post = blogService.getBlogByUrl(postUrl, null);
            }
        } else {
            post = blogService.getBlogByUrl(postUrl, PostTypeEnum.POST_TYPE_POST.getCode());
        }
        //文章不存在404
        if (null == post) {
            return "error/404";
        }

        //文章存在但是未发布
        if (!post.getPublished().equals(BlogStatusEnum.PUBLISHED.getCode())) {
            return "error/404";
        }

        // 如果是页面
        if (Objects.equals(post.getPostType(), PostTypeEnum.POST_TYPE_PAGE.getCode())) {
            return "redirect:/p/" + post.getUrl();
        }
        // 如果是公告
        else if (Objects.equals(post.getPostType(), PostTypeEnum.POST_TYPE_NOTICE.getCode())) {
            return "redirect:/notice/" + post.getUrl();
        }
        return "forward:/article/" + post.getId();
    }
    /**
     * 底部footer组件
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/footer", method = RequestMethod.GET)
    public String footerWidgets(Model model) {
        List<Widget> footerWidgets = null;
        footerWidgets = (List<Widget>) redisTemplate.opsForValue().get("footerWidgets");
        if(footerWidgets == null){
            System.out.println("从数据库中读取底部footer组件");
            footerWidgets = widgetService.findWidgetByType(WidgetTypeEnum.FOOTER_WIDGET.getCode());
            redisTemplate.opsForValue().set("footerWidgets", footerWidgets, 1, TimeUnit.HOURS);
        }else{
            System.out.println("从redis中读取底部footer组件");
        }
        model.addAttribute("footerWidgets", footerWidgets);

        return "home/_fragments :: widgetList";
    }

    /**
     * 顶部自定义页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/header")
    public String headerPage(Model model,@RequestParam Integer m) {
        PageInfo<Blog> pagePageInfo = null;
        pagePageInfo = (PageInfo<Blog>) redisTemplate.opsForValue().get("pagePageInfo");
        if(pagePageInfo == null){
            System.out.println("从数据库中读取顶部自定义页面");
            HashMap<String, Object> pageCriteria = new HashMap<>(1);
            pageCriteria.put("postType", PostTypeEnum.POST_TYPE_PAGE.getCode());
            pagePageInfo = blogService.pageBlog(0, 15, pageCriteria);
            redisTemplate.opsForValue().set("pagePageInfo", pagePageInfo, 1, TimeUnit.HOURS);
        }else{
            System.out.println("从redis中读取顶部自定义页面");
        }
        model.addAttribute("pagePageInfo", pagePageInfo);
        model.addAttribute("n", m);

        return "home/_fragments :: header";
    }

    /**
     * 渲染幻灯片
     * @return
     */
    @RequestMapping(value = "/getSlideListbyJson")
    @ResponseBody
    public Msg getSliesByJson(){
        List<Slide> slides = slideService.findAll();

        return Msg.success().add("slides", slides);
    }
}
