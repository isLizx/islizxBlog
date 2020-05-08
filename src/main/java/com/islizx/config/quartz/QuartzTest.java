package com.islizx.config.quartz;

import com.islizx.service.BlogService;
import com.islizx.util.ConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author lizx
 * @date 2020-03-11 - 17:05
 */
@Component
public class QuartzTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BlogService blogService;

    /**
     * 每15分钟执行刷新浏览量
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void refreshViews() {
        Set<Integer> keys = redisTemplate.opsForHash().keys("postViews");
        for (Integer key : keys) {
            int i = (int) redisTemplate.opsForHash().get("postViews", key);
            blogService.updateBlogView(key, i);
            redisTemplate.opsForHash().delete("postViews", key);
        }
    }

    /**
     * 每周一上午10：15备份一次数据库
     */
    @Scheduled(cron = "0 15 10 ? * MON")
    public void backMySql(){
        Date now = new Date();
        DateFormat df= DateFormat.getDateTimeInstance();
        String dbName = df.format(now)+".sql";
        dbName=dbName.replaceAll(":", "_");

        String user = ConfigManager.getProperty("mysql.username");
        String password = ConfigManager.getProperty("mysql.password");
        String database = ConfigManager.getProperty("databaseName");
        String filepath = ConfigManager.getProperty("databasePath")+dbName;
        //System.out.println(filepath);

        String stmt1 = "mysqldump  -u "+user+" -p"+password+" --set-charset=utf8 "+database;
        try{
            Process process = Runtime.getRuntime().exec(stmt1);
            InputStream in = process.getInputStream();
            InputStreamReader xx = new InputStreamReader(in, "utf8");
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            FileOutputStream fout = new FileOutputStream(filepath);
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
