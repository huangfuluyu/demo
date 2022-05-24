package com.example.demo.exceptions;

import com.example.demo.enums.ExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : HuangFu
 * @Description : BaseException 自定义异常类
 * @date : 2022-05-24 13:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
    private Integer code;
    private String message;

    public BaseException(ExceptionEnum status) {
        super(status.getMsg());
        this.code = status.getCode();
        this.message = status.getMsg();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
