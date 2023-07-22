package cn.wenhe9.pinda.core.swagger.value;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 文档信息
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@Data
public class DocketInfo {
    /**
     * 标题
     **/
    private String title = "在线文档";
    /**
     * 自定义组名
     */
    private String group = "";
    /**
     * 描述
     **/
    private String description = "在线文档";
    /**
     * 版本
     **/
    private String version = "1.0";
    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";

    private Contact contact = new Contact();

    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    private List<GlobalOperationParameter> globalOperationParameters;

    /**
     * 排序
     */
    @Deprecated
    private Integer order = 1;

    public String getGroup() {
        if (group == null || "".equals(group)) {
            return title;
        }
        return group;
    }
}
