package cn.wenhe9.pinda.core.result;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @description: 统一返回结果
 * @author: DuJinliang
 * @create: 2023/1/26
 */
@Getter
@Setter
public class ResultResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultResponse(){
    }

    protected static <T> ResultResponse<T> build(T data){
        ResultResponse<T> resultResponse = new ResultResponse<>();
        resultResponse.setData(data);
        return resultResponse;
    }

    public static <T> ResultResponse<T> build(T data, ResultResponseEnum responseEnum){
        ResultResponse<T> resultResponse = build(data);
        resultResponse.setCode(responseEnum.getCode());
        resultResponse.setMessage(responseEnum.getMessage());
        return resultResponse;
    }


    public static <T> ResultResponse<T> success(T data){
        return build(data, ResultResponseEnum.SUCCESS);
    }

    public static <T> ResultResponse<T> success(){
        return build(null, ResultResponseEnum.SUCCESS);
    }

    public static <T> ResultResponse<T> fail(T data){
        return build(data, ResultResponseEnum.FAIL);
    }

    public static <T> ResultResponse<T> fail(){
        return build(null, ResultResponseEnum.FAIL);
    }

    public ResultResponse<T> code(Integer code){
        this.code = code;
        return this;
    }

    public ResultResponse<T> message(String message){
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return Objects.equals(this.code, ResultResponseEnum.SUCCESS.getCode());
    }
}
