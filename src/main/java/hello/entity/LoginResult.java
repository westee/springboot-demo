package hello.entity;

public class LoginResult extends Result<User> {
    boolean isLogin;

    protected LoginResult(String status, String message, User user, boolean isLogin) {
        super(status, message, user);
        this.isLogin = isLogin;
    }

    public static Result success( String msg, boolean isLogin) {
        return new LoginResult("ok",msg, null, isLogin);
    }

    public static Result success( String msg, boolean isLogin,User user) {
        return new LoginResult("ok",msg, user, isLogin);
    }

    public static Result fail(String msg) {
        return new LoginResult("fail",msg, null, false);
    }


    public boolean isLogin() {
        return isLogin;
    }

}
