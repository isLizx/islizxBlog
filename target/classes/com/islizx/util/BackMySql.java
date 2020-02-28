package com.islizx.util;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * 用于读取数据库配置文件
 * @author lizx
 * @date 2020-02-28 - 21:11
 */
public class BackMySql {
    public static void exportDataBase(){

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
