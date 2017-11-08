package UtilTest;

import org.junit.Test;
import star.utils.BeanConvertUtil;
import star.utils.ProPertiesUtil;

import java.util.Properties;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public class TestPropertiesUtil {
    @Test
    public void get(){
        Properties configProperties = ProPertiesUtil.loadProperties("config.properties");
        String username = ProPertiesUtil.getString(configProperties,"jdbc.username");
        System.out.println(username);
    }
    @Test
    public void testBeanConvert(){
        Properties configProperties = ProPertiesUtil.loadProperties("config.properties");
        System.out.println(BeanConvertUtil.toMap(configProperties));
    }
}
