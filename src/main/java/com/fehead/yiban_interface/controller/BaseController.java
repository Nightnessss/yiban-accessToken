package com.fehead.yiban_interface.controller;


import com.fehead.yiban_interface.error.BusinessException;
import com.fehead.yiban_interface.error.EmBusinessError;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/7/10 11:50
 */
public class BaseController {

    Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    // 定义exceptionHandler来解决controller层中未被吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errorCode", businessException.getErrorCode());
            responseData.put("errorMsg", businessException.getErrorMsg());

            logger.info(new ReflectionToStringBuilder(responseData).toString());
        } else if(ex instanceof DataAccessException){ //数据库连接错误
            responseData.put("errorCode", EmBusinessError.DATARESOURCE_CONNECT_FAILURE.getErrorCode());
            responseData.put("errorMsg", EmBusinessError.DATARESOURCE_CONNECT_FAILURE.getErrorMsg());
            logger.info(new ReflectionToStringBuilder(responseData).toString());
        }else{
            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errorMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
            logger.info(new ReflectionToStringBuilder(responseData).toString());
        }

    }
}
