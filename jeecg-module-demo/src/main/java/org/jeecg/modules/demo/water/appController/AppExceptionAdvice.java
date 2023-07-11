package org.jeecg.modules.demo.water.appController;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.base.ThinkResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "org.jeecg.modules.demo.water.appController")
public class AppExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ThinkResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ThinkResult.error(e.getMessage());
    }

    /*
  Jackson参数绑定异常
   */
    @ExceptionHandler(InvalidFormatException.class)
    public ThinkResult handleInvalidFormatException(InvalidFormatException e) {
        return ThinkResult.error("参数" + e.getValue() + "格式错误");
    }

    /*
    @RequestParam参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ThinkResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ThinkResult.error("参数不能为空：" + e.getParameterName());
    }

    /*
    JSR-303参数校验失败
    */
    @ExceptionHandler(BindException.class)
    public ThinkResult handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errMsg = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + fieldError.getDefaultMessage())
                .collect(Collectors.joining(","));
        return ThinkResult.error("参数校验失败：" + errMsg);
    }

    /*
    JSR-303标准功能增强的额外参数校验（@Validated注解提供）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ThinkResult handleConstraintViolationException(ConstraintViolationException e) {
        return ThinkResult.error("参数校验失败：" + e.getMessage());
    }
}