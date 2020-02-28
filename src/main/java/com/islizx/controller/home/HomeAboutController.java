package com.islizx.controller.home;

import com.islizx.entity.Link;
import com.islizx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author lizx
 * @date 2020-02-19 - 21:12
 */
@Controller
@RequestMapping(value = "/about")
public class HomeAboutController {

    @Autowired
    LinkService linkService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String link(Model model){
        List<Link> linkList = linkService.getAll();

        model.addAttribute("linkList", linkList);
        return "home/about";
    }
}
