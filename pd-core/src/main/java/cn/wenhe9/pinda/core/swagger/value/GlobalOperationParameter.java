package cn.wenhe9.pinda.core.swagger.value;

import lombok.Data;

/**
 * @description: 全局参数
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@Data
public class GlobalOperationParameter {
    /**
     * 参数名
     **/
    private String name;

    /**
     * 描述信息
     **/
    private String description = "全局参数";

    /**
     * 指定参数类型
     **/
    @Deprecated
    private String modelRef = "String";

    /**
     * 参数放在哪个地方:header,query,path,body.form
     **/
    private String parameterType = "header";

    /**
     * 参数是否必须传
     **/
    private Boolean required = false;
    /**
     * 默认值
     */
    private String defaultValue = "";
    /**
     * 允许为空
     */
    private Boolean allowEmptyValue = true;
    /**
     * 排序
     */
    @Deprecated
    private int order = 1;
}
