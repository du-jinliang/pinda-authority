package cn.wenhe9.pinda.core.swagger.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 认证信息
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basic {
    /**
     * 是否开启认证
     */
    private Boolean enable = false;

    /**
     * 用户名
     */
    private String username = "wenhe9";

    /**
     * 密码
     */
    private String password = "wenhe9";
}
