package cn.wenhe9.pinda.core.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @description: 日志打印工具类
 * @author: DuJinliang
 * @create: 2022/12/23
 */
public class LogInfoUtils {
    public static String errInfo(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
