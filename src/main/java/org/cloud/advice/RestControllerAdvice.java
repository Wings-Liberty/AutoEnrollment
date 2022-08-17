package org.cloud.advice;

import lombok.extern.slf4j.Slf4j;
import org.common.utils.R;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("【统一异常处理吃掉异常】");
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.info("【统一异常处理吃掉异常】");

//        System.out.println(e);
        e.printStackTrace();

        return R.error(e.getMessage());
    }


}
