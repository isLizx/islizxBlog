package com.islizx.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author lizx
 * @date 2020-01-30 - 16:36
 */
public class VerifyCodeUtil {
    private int width = 100;
    private int height = 40;
    private int result = 0;

    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    private BufferedImage image = null;// 图像
    private String rnds;// 验证码
    /*
     * 取得图片的验证码
     */
    public String getRnds(){
        return rnds;
    }

    /*
     *  取得验证码图片
     */

    public BufferedImage getImage(){
        return this.image;
    }

    private VerifyCodeUtil(){
        try {
            image = createImage();
        } catch (IOException e) {
            System.out.println("验证码图片产生出现错误：" + e.toString());
        }
    }

    /*
     * 取得ValidationCodeUtil实例
     */
    public static VerifyCodeUtil Instance(){
        return new VerifyCodeUtil();
    }


    private BufferedImage createImage() throws IOException{
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();

        createRandom();

        drawBackground(g);

        drawRnds(g,rnds);

        g.dispose();

        return image;
    }

    private void drawRnds(Graphics g, String rnds){
        g.setColor(new Color(0xFFFFFF));
        g.setFont(new Font(null,Font.ITALIC | Font.BOLD, 18));

        g.drawString(rnds.charAt(0) + "", 1, width/4-1);
        g.drawString(rnds.charAt(1) + "", width/4+1, width /4+1);
        g.drawString(rnds.charAt(2) + "", width/2+1, width /4-1);
        g.drawString(rnds.charAt(3) + "", width*3/4+1, width /4+1);

    }

    /*背景*/
    private void drawBackground(Graphics g){
        g.setColor(new Color(0x00B5AD));
        g.fillRect(0, 0, width, height);
        //噪点
        for(int i = 0; i< 120;i++){
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            int red = (int)(Math.random() * 256);
            int green = (int)(Math.random() * 256);
            int blue = (int)(Math.random() * 256);
            g.setColor(new Color(red, green, blue));
            g.drawOval(x, y, 1, 1);
        }
    }

    /*数字验证码*/
    private void createRandom(){
        String str = "0123456789";
        String str1 = "+-*";
        String str2 = "=";
        char[] rndChars = new char[4];
        Random random = new Random();
        rndChars[0] = str.charAt(random.nextInt(str.length()));
        rndChars[1] = str1.charAt(random.nextInt(str1.length()));
        rndChars[2] = str.charAt(random.nextInt(str.length()));
        rndChars[3] = str2.charAt(random.nextInt(str2.length()));

        Character strNumber1 = rndChars[0];
        Character strNumber2 = rndChars[2];
        Character strIcon = rndChars[1];
        int number1 = Integer.parseInt(strNumber1.toString());
        int number2 = Integer.parseInt(strNumber2.toString());
        if((strIcon.toString()).equals("+")){
            result = number1 + number2;
        }else if((strIcon.toString()).equals("-")){
            result = number1 - number2;
        }else if((strIcon.toString()).equals("*")){
            result = number1 * number2;
        }else{
            System.out.println("验证码结果输出错误！");
        }
        rnds = new String(rndChars);
    }
}
