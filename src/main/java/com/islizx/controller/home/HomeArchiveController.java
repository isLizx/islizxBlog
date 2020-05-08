package com.islizx.controller.home;

import com.islizx.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lizx
 * @date 2020-02-19 - 16:37
 */
@Controller
public class HomeArchiveController {
    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public String archives(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.getAllCount().getBlogCount());
        return "home/archives";
    }
}
