package cn.wenhe9.pinda.core.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @description: xss属性类
 * @author: DuJinliang
 * @create: 2023/7/12
 */
@Data
@ConfigurationProperties(prefix = "pinda.xss")
public class XssProperties {
    /**
     * 过滤开关
     */
    private Boolean enabled;

    /**
     * 排除的路径
     */
    private List<String> excludes;
}
