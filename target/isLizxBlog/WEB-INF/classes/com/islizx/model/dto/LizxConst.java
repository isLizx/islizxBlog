package com.islizx.model.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lizx
 * @date 2020-02-14 - 16:19
 */
public class LizxConst {
    /**
     * 所有设置选项（key,value）
     */
    public static Map<String, String> OPTIONS;

    /**
     * OwO表情
     */
    public static Map<String, String> OWO = new HashMap<>();

    public static Map<String, String> getOPTIONS() {
        return OPTIONS;
    }

    public static void setOPTIONS(Map<String, String> OPTIONS) {
        LizxConst.OPTIONS = OPTIONS;
    }

    public static Map<String, String> getOWO() {
        return OWO;
    }

    public static void setOWO(Map<String, String> OWO) {
        LizxConst.OWO = OWO;
    }
}
