package com.islizx.controller.admin;

import com.github.pagehelper.PageInfo;
import com.islizx.config.annotation.SystemLog;
import com.islizx.entity.Log;
import com.islizx.model.dto.Msg;
import com.islizx.model.enums.LogTypeEnum;
import com.islizx.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lizx
 * @date 2020-02-22 - 13:00
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String getLogbyIndex(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model){
        // 获取日志
        PageInfo<Log> pageInfo = logService.getAll(pageIndex, pageSize);
        model.addAttribute("pageInfo", pageInfo);

        return "admin/log/logs";
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public String getLogbyPage(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model){
        // 获取日志
        PageInfo<Log> pageInfo = logService.getAll(pageIndex, pageSize);
        model.addAttribute("pageInfo", pageInfo);

        return "admin/log/logs :: logList";
    }

    @RequestMapping(value = "/log/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteLog(@PathVariable("id") Integer id){
        logService.delete(id);

        return Msg.success().add("msg", "日志已删除");
    }

    @RequestMapping(value = "/log/batchDelete", method = RequestMethod.DELETE)
    @ResponseBody
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

        List<Log> logList = logService.findByBatchIds(ids);

        for(Log log:logList){
            logService.delete(log.getId());
        }

        return Msg.success().add("msg", "删除成功!");
    }
}
