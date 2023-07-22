package cn.wenhe9.pinda.core.log.enums;

/**
 * @description: 日志类型枚举
 * @author: DuJinliang
 * @create: 2023/7/22
 */
public enum LogTypeEnum {
    OPT("OPT"),
    EX("EX");

    private final String type;

    LogTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }
}
