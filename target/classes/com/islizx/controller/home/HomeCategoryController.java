package com.islizx.controller.home;

import com.github.pagehelper.PageInfo;
import com.islizx.entity.Type;
import com.islizx.model.enums.BlogStatusEnum;
import com.islizx.model.enums.PostTypeEnum;
import com.islizx.service.BlogService;
import com.islizx.service.TypeService;
import com.islizx.vo.BlogQuery;
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
 * @date 2020-02-19 - 13:28
 */
@Controller
@RequestMapping(value = "/category")
public class HomeCategoryController {

    @Autowired
    TypeService typeService;

    @Autowired
    BlogService blogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String categories(Model model){
        return this.categories(model, -1, 1, 8, "updateTime", "desc");
    }

    /**
     * 根据分类路径查询文章
     *
     * @param model model
     * @param id    分类ID
     * @return string
     */
    @RequestMapping(value = "/{cateId}/page/{page}", method = RequestMethod.GET)
    public String categories(Model model,
                             @PathVariable("cateId") Integer id,
                             @PathVariable(value = "page") Integer pageNumber,
                             @RequestParam(value = "size", defaultValue = "8") Integer pageSize,
                             @RequestParam(value = "sort", defaultValue = "updateTime") String sort,
                             @RequestParam(value = "order", defaultValue = "desc") String order) {
        List<Type> types = typeService.listTypeWithCountAtHome();
        if (id == -1) {
            for(Type type:types){
                if(type.getBlogCount() != 0){
                    id = type.getId();
                    break;
                }
            }
        }

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("postType", PostTypeEnum.POST_TYPE_POST.getCode());
        criteria.put("published", BlogStatusEnum.PUBLISHED.getCode());
        criteria.put("typeId", id);
        criteria.put("sort", sort);
        criteria.put("order", order);

        PageInfo pageInfo = blogService.pageBlog(pageNumber, pageSize, criteria);
        model.addAttribute("types", types);
        model.addAttribute("page", pageInfo);
        model.addAttribute("activeTypeId", id);

        return "home/types";
    }
}
