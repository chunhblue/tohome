package cn.com.bbut.iy.itemmaster.util.common;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码
 *
 * @auther LCH
 */
@Component
public class Code {
    // 图片的宽度
    private int width = 160;
    // 图片的高度
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 5;
    // 验证码
    private String code = null;
    // 图片对象
    private BufferedImage bufferedImage = null;

    // 验证码范围，去掉0(数字)和o（拼音），容易混淆（小写的l也去掉，大写的L就不用了）
    private char[] codeSequence = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q',
        'R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9'};
    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code = code;
    }
    /**
     * 生成验证码
     */
    public void createCode(){
        // 声明画验证码所需要的几个变量
        int x = 0;
        int red = 0,green = 0,blue = 0;
        // 设置字符间的间距
        x = width / (codeCount + 2);
        // 创建一个设置rgb颜色对象，设置这个图片的像素是width*height，采用rgb颜色来设置颜色
        bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        // 创建一个画2D图对象
        Graphics2D g = bufferedImage.createGraphics();
        // 画一个矩形作为验证码图片背景颜色，参数0，0是起始坐标
        g.setColor(Color.green);
        g.fillRect(0,0,width,height);

        // 干扰线
        Random random = new Random();
        for(int i = 0; i<lineCount; i++){
            // 生成干扰线的随机坐标
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);

            // 设置干扰线的随机颜色
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue =  random.nextInt(255);
            g.setColor(new Color(red,green,blue));
            // 画出干扰线
            g.drawLine(xs,ys,xe,ye);
        }

            // 四个字体对象，字体，字体风格，字体大小
            g.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 40));

            StringBuffer randomCode = new StringBuffer();
            for(int j = 0; j < codeCount; j++){
                String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);
                g.setColor(new Color(red,green,blue));
                /*// 将认证码显示到图象中
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110),
                        20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成*/

                // 画出字符，参数分别是画的字，字的x坐标，y坐标
                g.drawString(strRand,(j + 1)*x,height - 4);
                randomCode.append(strRand);
            }
            code = new String(randomCode);

    }
    /**
     * 生成图片
     */
    public void write(OutputStream sos){
        try {
            ImageIO.write(bufferedImage,"png",sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
