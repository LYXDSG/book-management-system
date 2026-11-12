package org.example.demo08.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
//全局异常处理中心
public class GlobalExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //导入log

    //1.业务异常：我们自己抛的，属于"预期内的失败"，只记warn，不需要堆栈
    //不用改状态行，只是警告
    //Q：啥叫堆栈
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e){
        log.warn("业务异常：code={},msg={}",e.getCode(),e.getMessage());
        return Result.fail(e.getCode(),e.getMessage());
    }

    //2.处理参数校验异常，BindException要导SpringFramwork那个
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBind(BindException e){
        //Q:这里这个返回参数类型是啥意思，为啥ResponseEntity里面还要套一层，不套会怎么样
        String detail = e.getBindingResult()
                         .getFieldErrors()
                         .stream()
                         .map(fe -> fe.getField()+" "+fe.getDefaultMessage() )
                         .collect(Collectors.joining(";"));
        //Q:这段最后不是转换成集合吗，为什么又成String了，精辨析一下map和collect在这里的用法
        log.warn("参数校验失败：{}",detail);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Result.fail(400,"参数校验失败"+detail));
    }

    //3.兜底，上面的异常都没接住就触发
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        log.error("系统异常",e);
        return Result.fail(500,"服务器开小差了，请稍后再试");
    }
    //Q:这里这个e为啥不用e.getCode(),而是直接手写500。这里要是用e.getCode()会怎么样,
    //Q:为啥Exception没有getCode，只有自己写的异常才带吗
}
