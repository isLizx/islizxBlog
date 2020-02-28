package com.islizx.service.impl;

import com.islizx.util.BackMySql;

/**
 * 备份数据库
 * @author lizx
 * @date 2020-02-28 - 23:04
 */

public class TriggerImpl {
    public void BackMySQL(){
        BackMySql.exportDataBase();
    }
}
