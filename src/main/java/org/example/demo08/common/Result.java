package org.example.demo08.common;
//统一响应体
public class Result<T> {

    private Integer code;
    //业务状态码：200成功，400参数错，401未登录，404不存在，500服务器错

    private String msg;
    //给人看的报错信息

    private T data;
    //真正返回的数据，失败时为NULL

    public Result(){}
    //空参构造要么不写要么就public，
    //下面的全参构造是有特殊要求才用private（用了静态方法）

    private Result(Integer code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    //构造器用private，强调使用下面的静态工厂方法
    //Q：这个构造器到底有啥用，为啥设成private还要写出来，外面访问不到怎么用这个
    //A：主要给本类下方的静态方法使用，其他地方不需要用，所以给private
    //Q：这个类为什么不需要空参构造而PageResult需要空参构造
    //A：这里写漏了是，加上更保险
    //Q：为啥下面的方法全部要用静态方法
    //A：静态方法可以直接用类调用，不用造对象，调用更短，语义更清楚

    public static <T> Result<T> ok(T data){
        return new Result(200,"成功",data);
    }
    //成功，用默认提示语

    public static <T> Result<T> ok(String msg,T data){
        return new Result(200,msg,data);
    }
    //成功，自己写提示语

    public static <T> Result<T> fail(Integer code,String msg){
        return new Result(code,msg,null);
    }
    //失败，业务代码里一般不直接调它，而是抛BusinessException，由全局异常统一处理


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
