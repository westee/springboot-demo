package hello.entity;
// JSON序列化的结果取决于getter方法,和字段无关.
public  class Result {
    String status;
    String message;
    boolean isLogin;
    Object data;

    public static Result failResult(String message){
        return new Result("fail", message, false);
    }

    public static Result successResult(String message){
        return new Result("ok", message, true);
    }

    public Object getData() {
        return data;
    }

    public Result(String status, String message, boolean isLogin) {
        this(status, message, isLogin, null);
    }

    public Result(String status, String message, boolean isLogin, Object data) {
        this.status = status;
        this.message = message;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLogin() {
        return isLogin;
    }

}

