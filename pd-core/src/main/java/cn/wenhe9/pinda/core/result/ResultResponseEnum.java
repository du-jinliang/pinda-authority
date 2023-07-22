package cn.wenhe9.pinda.core.result;

import lombok.Getter;

/**
 * @description: 统一返回状态枚举
 * @author: DuJinliang
 * @create: 2023/1/26
 */
@Getter
public enum ResultResponseEnum {

    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    INTERNAL_SERVER_ERROR(40000, "系统出现异常"),
    BUSINESS_EXCEPTION(40001, "业务出现异常"),
    VALIDATE_FAILED(40002, "请求参数验证失败"),
    KNOWN_LOCK_BEAN_NAME(40003, "未知的加锁类型"),
    DIR_NOT_FOUND(30400, "保存的文件夹不存在"),
    USER_NOT_FOUND(30500, "该用户不存在"),
    CODE_ERROR(30600, "验证码错误或失效"),
    LOGIN_AUTH(30600, "尚未登录或登录过期"),
    INVALID_TOKEN(30600, "token不合法"),
    CAN_NOT_GET_LOCK(306001, "获取锁失败"),
    FORBIDDEN(403, "没有相关权限");

    private final Integer code;
    private final String message;

    ResultResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
