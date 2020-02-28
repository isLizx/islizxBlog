package com.islizx.model.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息类
 * @author lizx
 * @date 2020-01-30 - 16:54
 */
public class Msg implements Serializable {
    private String msg;
    private Integer code;


    private Map<String, Object> extend = new HashMap<String, Object>();

    public Msg add(String key, Object obj){
        this.getExtend().put(key,obj);
        return this;
    }

    public static Msg success(){
        Msg msg = new Msg();
        msg.setCode(100);
        msg.setMsg("操作成功!");
        return msg;
    }

    public static Msg fail(){
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("操作失败!");
        return msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
