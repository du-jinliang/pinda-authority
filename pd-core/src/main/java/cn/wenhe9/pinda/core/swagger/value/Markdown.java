package cn.wenhe9.pinda.core.swagger.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 文档
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Markdown {
    /**
     * 是否启用 markdown
     */
    private Boolean enable = false;

    /**
     * 文档地址
     */
    private String basePath = "classpath:markdown/*";
}
