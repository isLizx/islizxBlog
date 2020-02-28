package com.islizx.controller.home;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Tag;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 15:04
 */
@Controller
@RequestMapping(value = "/tag")
public class HomeTagController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogService blogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String tags(Model model){
        return this.tags(model, -1, 1, 8, "updateTime", "desc");
    }

    /**
     * 根据标签路径查询文章
     *
     * @param model model
     * @param id    标签ID
     * @return string
     */
    @RequestMapping(value = "/{tagId}/page/{page}", method = RequestMethod.GET)
    public String tags(Model model,
                             @PathVariable("tagId") Integer id,
                             @PathVariable(value = "page") Integer pageNumber,
                             @RequestParam(value = "size", defaultValue = "8") Integer pageSize,
                             @RequestParam(value = "sort", defaultValue = "updateTime") String sort,
                             @RequestParam(value = "order", defaultValue = "desc") String order) {
        List<Tag> tags = tagService.listTagWithCountAtHome(null);
        if (id == -1) {
            for(Tag tag:tags){
                if(tag.getBlogCount() != 0){
                    id = tag.getId();
                    break;
                }
            }
        }

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        criteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
        criteria.put("tagId", id);
        criteria.put("sort", sort);
        criteria.put("order", order);

        PageInfo pageInfo = blogService.pageBlog(pageNumber, pageSize, criteria);
        model.addAttribute("tags", tags);
        model.addAttribute("page", pageInfo);
        model.addAttribute("activeTagId", id);

        return "home/tags";
    }
}
