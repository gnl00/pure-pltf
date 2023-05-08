package com.pure.test;

import com.pure.entity.info.BaseInfo;
import com.pure.entity.vo.BaseInfoVo;
import com.pure.spi.InfoHandler;
import org.springframework.beans.BeanUtils;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * com.pure.test.Main
 *
 * @author gnl
 * @since 2023/5/7
 */
public class CoreMain {
//    public static void main(String[] args) {
//        // test();
//        // testSpi();
//        // testEnv();
//    }

    public static void testEnv() {
        Properties properties = new Properties();
        System.out.println(properties.getProperty("os.name"));

    }

    public static void testSpi() {
        ServiceLoader<InfoHandler> infoHandlers = ServiceLoader.load(InfoHandler.class);
        InfoHandler infoHandler = infoHandlers.findFirst().orElse(null);
        System.out.println(infoHandler);
    }

    public static void test() {
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setAppName("abc");
        BaseInfoVo vo = new BaseInfoVo();
        BeanUtils.copyProperties(baseInfo, vo);
        System.out.println(vo);
    }
}
