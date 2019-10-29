/**
 * 名词解释:
 * 饱和度是指色彩的鲜艳程度，也称色彩的纯度。
 * 灰度：使用黑色调表示物体，即用黑色为基准色，不同的饱和度的黑色来显示图像。
 * 像素：如同摄影的相片一样，数码影像也具有连续性的浓淡阶调，我们若把影像放大数倍，会发现这些连续色调其实是由许多色彩相近的小方点所组成，
 * 这些小方点就是构成影像的最小单元——像素。是分辨率的尺寸单位。
 * 像素是基本原色素及其灰度的基本编码。我们看到的数字图片是有一个二维的像素矩阵组成。
 * 像素在计算机中通常用3个字节24位保存，如16-23 位表示红色（R）分量，8-15 位表示绿色(G)分量，0-7 位表示蓝色(B)分量；
 * 当图片尺寸以像素为单位时，每一厘米等于28像素，比如15*15厘米长度的图片，等于420*420像素的长度。
 * 一个像素所能表达的不同颜色数取决于比特每像素(BPP)。如8bpp[2^8=256色， 灰度图像]、16bpp[2^16=65536色，称为高彩色]、24bpps[2^24=16777216色，称为真彩色]。
 * 分辨率：图像总像素的多少，称为图像分辨率。
 * RGB： 颜色模型，是将颜色表示成数字形式的模型，或者说是一种记录图像颜色的方式。详情百度
 * <p>
 * 下列代码是将一个图片分解成R,G,B三种色彩灰度图片的算法
 */
package star.utils;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageConvertUtil {

    private static final int WHITE = -1;

    private static final int BLACK = -16777216;

    private static final int RED = -65536;

    private static final String PNG = "png";


    /**
     * getRGB public int[] getRGB(int startX, int startY, int w, int h,
     * int[] rgbArray, int offset, int scansize)从图像数据的某一部分返回默认 RGB 颜色模型
     * (TYPE_INT_ARGB) 和默认 sRGB 颜色空间中整数像素数组。如果该默认模型与该图像的 ColorModel
     * 不匹配，则发生颜色转换。在使用此方法所返回的数据中，每个颜色分量只有 8 位精度。通过图像中指定的坐标 (x, y)，ARGB
     * 像素可以按如下方式访问：
     *
     * pixel = rgbArray[offset + (y-startY)*scansize + (x-startX)];
     * 如果该区域不在边界内部，则抛出 ArrayOutOfBoundsException。但是，不保证进行显式的边界检查。
     *
     *
     * 参数：
     *  startX - 起始 X 坐标
     *  startY - 起始 Y 坐标
     *  w - 区域的宽度
     *  h - 区域的高度
     * rgbArray - 如果不为 null，则在此写入 rgb 像素
     * offset - rgbArray 中的偏移量
     * scansize - rgbArray 的扫描行间距
     * 返回： RGB 像素数组。
     */
    public static byte[] convertImage(InputStream inputStream) {
        try {
            BufferedImage img = ImageIO.read(inputStream);
            int width = img.getWidth();
            int height = img.getHeight();
            int startX = 0;
            int startY = 0;
            int offset = 0;
            // 被遍历的宽度间距
            int dd = width - startX;
            // 被遍历的高度间距
            int hh = height - startY;

            // rgb的数组，保存像素，用一维数组表示二位图像像素数组  
            int[] rgbArray = new int[offset + hh * width + dd];

            int[] newArray = new int[offset + hh * width + dd];

            // 把像素存到固定的数组里面去
            img.getRGB(startX, startY, width, height, rgbArray, offset, width);

            for (int i = 0; i < height - startY; i++) {
                for (int j = 0; j < width - startX; j++) {
                    Color c = new Color(rgbArray[offset + i * width + j]);
                    newArray[i * dd + j] = convertColor(c);
                }
            }

            BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            imgOut.setRGB(startX, startY, width, height, newArray, offset, width);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(imgOut, PNG, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private static int convertColor(Color c) {
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        if (red > 150 && green < 100 && blue < 100) {
            return RED;
        }

        if (red > 150 && green > 150 && blue > 150) {
            return WHITE;
        }

        if (red < 150 && green < 150 && blue < 150) {
            return BLACK;
        }

        int avg = (red + green + blue) / 3;

        if (avg < 100) {
            return BLACK;
        } else {
            return WHITE;
        }

    }


    public static void main(String[] args) throws Exception {
        String path = "/Users/fankaixiang/Downloads/";
        String source = path + "xiaofeiyang.jpg";
        String target = path + "xiaofeiyang33.png";

        byte[] bytes = convertImage(new FileInputStream(source));

        // 新建一个图像
        FileOutputStream fileOutputStream = new FileOutputStream(target);

        IOUtils.copy(new ByteArrayInputStream(bytes), fileOutputStream);
    }
}