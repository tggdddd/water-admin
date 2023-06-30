package org.jeecg.modules.demo.water.appController;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.base.ThinkResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "org.jeecg.modules.demo.water.appController")
public class AppExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ThinkResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ThinkResult.error(e.getMessage());
    }

}