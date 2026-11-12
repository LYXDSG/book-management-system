package org.example.demo08.common;

public class BusinessException extends RuntimeException{

    private final Integer code;

    public BusinessException(Integer code,String msg){
        super(msg);
        this.code=code;
    }
    //用super调用父类Throwable的构造方法

    public BusinessException(String msg){
        this(500,msg);
        //调用刚刚写的那条构造方法
    }

    public Integer getCode(){
        return this.code;
    }
    //final code,无需修改，所以只用get不用set方法
}
