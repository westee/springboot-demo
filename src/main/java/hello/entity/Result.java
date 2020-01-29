package hello.entity;

// JSON序列化的结果取决于getter方法,和字段无关.
public abstract class Result<T> {
    String status;
    String message;
    T data;

//    public static Result failResult(String message) {
//        return new Result("fail", message, false);
//    }
//
//    public static Result successResult(String message) {
//        return new Result("ok", message, true);
//    }

    public Object getData() {
        return data;
    }

    protected Result(String status, String message) {
        this(status, message, null);
    }

    protected Result(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

